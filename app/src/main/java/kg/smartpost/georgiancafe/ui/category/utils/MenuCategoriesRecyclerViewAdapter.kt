package kg.smartpost.georgiancafe.ui.category.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.smartpost.georgiancafe.data.network.category.model.ModelCategory
import kg.smartpost.georgiancafe.databinding.ItemCategoryMenuBinding


class MenuCategoriesRecyclerViewAdapter(private val listener: CategoryClickListener) :
    ListAdapter<ModelCategory.CatDish.Category, MenuCategoriesRecyclerViewAdapter.ViewHolderCat>(DIFF) {

    fun getItemAtPos(position: Int): ModelCategory.CatDish.Category {
        return getItem(position)
    }

    private var _binding: ItemCategoryMenuBinding? = null

    inner class ViewHolderCat(private val binding: ItemCategoryMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            binding.txtCategory.text= current.name

            binding.root.setOnClickListener {
                listener.onCategoryClick(position, current.id.toInt())
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCat {
        _binding = ItemCategoryMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderCat(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderCat, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelCategory.CatDish.Category>() {
            override fun areItemsTheSame(oldItem: ModelCategory.CatDish.Category, newItem: ModelCategory.CatDish.Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ModelCategory.CatDish.Category,
                newItem: ModelCategory.CatDish.Category
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface CategoryClickListener {
        fun onCategoryClick(position: Int, id: Int)
    }

}