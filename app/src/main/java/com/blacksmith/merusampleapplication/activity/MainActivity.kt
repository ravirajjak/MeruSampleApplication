package com.blacksmith.merusampleapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.*
import com.blacksmith.merusampleapplication.R
import com.blacksmith.merusampleapplication.adapter.SearchAdapter
import com.blacksmith.merusampleapplication.databinding.ActivityMainBinding
import com.blacksmith.merusampleapplication.interfaces.IOnRecylerItemClickListener
import com.blacksmith.merusampleapplication.repository.`interface`.ApiService
import com.blacksmith.merusampleapplication.repository.model.Recipes
import com.blacksmith.merusampleapplication.repository.model.Search
import com.blacksmith.merusampleapplication.utils.AppConst
import com.blacksmith.merusampleapplication.utils.RecyclerItemTouchHelper
import com.miguelcatalan.materialsearchview.MaterialSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.sugarbox.sbapp.interfaces.OnLoadMoreListener


class MainActivity : BaseActivity(), IOnRecylerItemClickListener, OnLoadMoreListener,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    lateinit var binding: ActivityMainBinding
    private var disposable: Disposable? = null
    lateinit var mAdapter: SearchAdapter
    var mStrSearch: String = ""

    companion object {
        fun newInstance(): Class<MainActivity> {
            return MainActivity::class.java
        }
    }

    private val apiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)
        bindAdapter()
        getData(AppConst.CHICKEN)
        setSwipeRefreshListener()
        setSearchListener()

    }

    private fun setSearchListener() {
        binding.actMainSearchView.setOnQueryTextListener(object :
            MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                newText?.let {
                    if (it.length > 2) {
                        getData(it)
                        mStrSearch = it
                    }
                }
                return false
            }

        })
    }

    private fun setSwipeRefreshListener() {
        binding.actMainSwipeRefresh.setOnRefreshListener({
            getData(mStrSearch)
            binding.actMainSwipeRefresh.setRefreshing(false)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        val item: MenuItem = menu!!.findItem(R.id.action_search)
        binding.actMainSearchView.setMenuItem(item)
        return true
    }

    private fun getData(search: String) {
        if (mUtil.isInternetAvailable(applicationContext)) {
            disposable = apiService.getUserSearchQuery(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        onSuccess(result)
                    },
                    { error -> error.message?.let { showToast(it) } }
                )
        } else {
            val mSearchData = mPref.getStringPreference(AppConst.DATA)
            if (mSearchData != null) {
                val mSearch = gson.fromJson(mSearchData, Search::class.java)
                mAdapter.mDataList = mSearch.recipes as ArrayList<Recipes>
            }
        }
    }

    private fun onSuccess(response: Search) {
        response.let {
            mAdapter.setData(it.recipes)
            mAdapter.setOnLoadMoreListener(this)
            mPref.setStringPreference(AppConst.DATA, gson.toJson(response))
        }
    }

    private fun bindAdapter() {
        binding.actMainRecylerview.layoutManager = LinearLayoutManager(this)
        binding.actMainRecylerview.setItemAnimator(DefaultItemAnimator())
        binding.actMainRecylerview.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter = SearchAdapter(applicationContext, this)
        binding.actMainRecylerview.adapter = mAdapter
        mAdapter.setRecylerView(binding.actMainRecylerview)


        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.actMainRecylerview)


        val itemTouchHelperCallback1: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }

        ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(binding.actMainRecylerview)


    }

    override fun onItemClick(objects: Any) {
        if (objects is Recipes) {
            showToast(objects.title)
        }
    }

    override fun onLoadMore() {
        getData(mStrSearch)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        Log.d("SWIPE_DIRECTION ", direction.toString())
        if (viewHolder is SearchAdapter.MyViewHolder) {
            when (direction) {

                4 ->
                    mAdapter.removeItem(position)
                8 -> {
                    val mStrUrl = mAdapter.mDataList.get(position).publisher_url
                    openIntent(WebViewActivity.newInstance(), mStrUrl)
                }

            }

        }

    }

    private fun openIntent(mClass: Class<*>, mStrUrl: String) {
        val intent = Intent(this, mClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppConst.PUT_EXTRA_URL, mStrUrl)
        startActivity(intent)
    }
}
