package com.blacksmith.merusampleapplication.repository.model

import com.google.gson.annotations.SerializedName

data class Recipes(
    @SerializedName("image_url") val image_url: String,
    @SerializedName("social_url") val social_url: Float,
    @SerializedName("_id") val _id: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("source_url") val source_url: String,
    @SerializedName("recipe_id") val recipe_id: String,
    @SerializedName("publisher_url") val publisher_url: String,
    @SerializedName("title") val title: String
)
