package com.blacksmith.merusampleapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blacksmith.merusampleapplication.R
import com.blacksmith.merusampleapplication.databinding.RowItemRecipesBinding
import com.blacksmith.merusampleapplication.interfaces.IOnRecylerItemClickListener
import com.blacksmith.merusampleapplication.repository.model.Recipes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.sugarbox.sbapp.interfaces.OnLoadMoreListener


class SearchAdapter(
    val context: Context,
    val iOnRecylerItemClickListener: IOnRecylerItemClickListener
) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {


    private lateinit var onLoadMoreListener: OnLoadMoreListener
    private var loading: Boolean = false
    private val visibleThreshold = 10
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0


    lateinit var mDataList: ArrayList<Recipes>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)


        val binding = DataBindingUtil.inflate<RowItemRecipesBinding>(
            layoutInflater,
            R.layout.row_item_recipes,
            parent,
            false
        )
        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {

        if (!::mDataList.isInitialized)
            return 0
        return mDataList.size
//        return 10
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mDataList.get(position))
    }

    fun setData(mDataList: List<Recipes>) {
        this.mDataList = mDataList as ArrayList<Recipes>
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: RowItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var viewForeground: CardView
        var viewBackground: RelativeLayout? = null


        fun bind(recipe: Recipes) {
            binding.recipe = recipe
            viewForeground = binding.rowItemRecipeCard

            Glide.with(context).load(recipe.image_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.rowItemRecipeImgDisplay)

            binding.rowItemRecipeCard.setOnClickListener({
                iOnRecylerItemClickListener.onItemClick(recipe)
            })
        }

    }


    public fun setRecylerView(mRecylerView: RecyclerView) {


        val linearLayoutManager = mRecylerView.layoutManager!! as LinearLayoutManager
        mRecylerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = linearLayoutManager.getItemCount()
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    // End has been reached
                    // Do something
                    onLoadMoreListener.onLoadMore()
                    loading = true
                }
            }
        })
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun setLoaded() {
        loading = false
    }

    fun removeItem(position: Int) {
        mDataList.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mDataList.size)
    }

}