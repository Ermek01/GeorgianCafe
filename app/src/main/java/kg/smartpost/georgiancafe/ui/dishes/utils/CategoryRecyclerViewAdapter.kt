package kg.smartpost.georgiancafe.ui.dishes.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kg.smartpost.georgiancafe.R
import kg.smartpost.georgiancafe.data.network.category.model.ModelCategory
import kg.smartpost.georgiancafe.data.network.dishes.model.ModelDishes
import kg.smartpost.georgiancafe.databinding.ItemCategoryBinding
import kg.smartpost.georgiancafe.ui.category.utils.MenuCategoriesRecyclerViewAdapter


class CategoryRecyclerViewAdapter(private var hochu_cat: Int?, private val listener: CategoryClickListener) :
    ListAdapter<ModelDishes.CatDish.Category, CategoryRecyclerViewAdapter.ViewHolderCat>(DIFF) {

    private val lastClickedPosition = -1
    private var selectedItem = -1

    fun getItemAtPos(position: Int): ModelDishes.CatDish.Category {
        return getItem(position)
    }

    private var _binding: ItemCategoryBinding? = null

    inner class ViewHolderCat(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "ResourceType")
        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            if (hochu_cat == current.id.toInt() || selectedItem == position) {
                selectedItem = position
                if (selectedItem == position) {
                    binding.root.setBackgroundResource(R.drawable.bg_category_selected)
                    binding.txtCategory.setTextColor(binding.root.context.resources.getColor(R.color.textColorCategory))
                    hochu_cat = -1
                }
            }

            binding.txtCategory.text= current.name

            binding.root.setOnClickListener {
                val previousItem = selectedItem
                selectedItem = position
                notifyItemChanged(previousItem)
                notifyItemChanged(position)
                listener.onCategoryClick(position, current.id.toInt())
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCat {
        _binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderCat(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderCat, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelDishes.CatDish.Category>() {
            override fun areItemsTheSame(oldItem: ModelDishes.CatDish.Category, newItem:ModelDishes.CatDish.Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ModelDishes.CatDish.Category,
                newItem: ModelDishes.CatDish.Category
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface CategoryClickListener {
        fun onCategoryClick(position: Int, id: Int)
    }

}