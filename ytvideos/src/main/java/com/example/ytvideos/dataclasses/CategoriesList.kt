package com.example.ytvideos.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoriesList(


    @SerializedName("data")
    @Expose
    var data: List<Category>): Serializable {

    data class Category(

        @SerializedName("category_name")
        @Expose
        var categoryName: String = "",

        @SerializedName("category_icon")
        @Expose
        var categoryIcon: String = "",

        @SerializedName("view_type")
        @Expose
        var viewType: Int=1,

        @SerializedName("hasSubCategories")
        @Expose
        val hasSubCategories: Boolean = false,

        @SerializedName("isPremium")
        @Expose
        var isPremium: Boolean = false,

        @SerializedName("subCategories")
        @Expose
        val subCategories: List<SubCategory> = emptyList()


    ): Serializable

    data class SubCategory(

        @SerializedName("country")
        @Expose
        val country: String?,

        @SerializedName("sub_category_name")
        @Expose
        val subCategoryName: String?,



        @SerializedName("sub_category_img")
        @Expose
        val subCategoryImage: String?
    ) : Serializable
}
