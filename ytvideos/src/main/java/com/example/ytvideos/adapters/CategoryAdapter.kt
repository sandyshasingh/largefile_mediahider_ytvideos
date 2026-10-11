package com.example.ytvideos.adapters


import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ytvideos.HomeUiItem
import com.example.ytvideos.databinding.CategoryItemBinding
import com.example.ytvideos.databinding.ItemYoutubeBinding
import com.example.ytvideos.dataclasses.CategoriesList
import com.example.ytvideos.dataclasses.YoutubeItem
import com.example.ytvideos.listeners.CategoryclickedListener
import com.example.ytvideos.listeners.ItemClickedListener

class CategoryAdapter(var mContext: Activity,
                      var listener: CategoryclickedListener,
                      var itmListener: ItemClickedListener,
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    private val categoryData = ArrayList<YoutubeItem>()
//    private val categoryItemsMap = LinkedHashMap<String, List<HomeUiItem>>()

    fun submitList(data: List<YoutubeItem> ) {
//        categoryTitles.clear()
        categoryData.clear()

        categoryData.addAll(data)
//        categoryItemsMap.putAll(data)

        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {


            binding.viewAllBtn.setOnClickListener {

                listener.categoryItem(categoryData.get(absoluteAdapterPosition).subCategories)

            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)


    }



    override fun getItemCount(): Int {
        return categoryData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = categoryData[position].title
        val items = categoryData[position].subCategories
        holder.binding.categoryTitle.text = title

        holder.binding.subCategoryItems.adapter = SubCategoryAdapter(mContext,items,itmListener,categoryData[position].viewType, !categoryData[position].showViewAll)

        val showViewAll = !categoryData[position].showViewAll
        holder.binding.viewAllBtn.visibility =
            if (showViewAll) View.VISIBLE else View.GONE
        Glide.with(mContext)
            .load(categoryData[position].categoryIcon)
//                .apply(requestOptions)
            .into(holder.binding.categoryIcon)


    }

}