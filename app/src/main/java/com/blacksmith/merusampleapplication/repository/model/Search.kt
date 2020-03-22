package com.blacksmith.merusampleapplication.repository.model

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("count") val count: Int,
    @SerializedName("recipes") val recipes: List<Recipes>
)