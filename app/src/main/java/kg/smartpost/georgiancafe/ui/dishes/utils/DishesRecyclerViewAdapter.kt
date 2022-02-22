package kg.smartpost.georgiancafe.ui.dishes.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kg.smartpost.georgiancafe.R
import kg.smartpost.georgiancafe.data.network.category.model.ModelCategory
import kg.smartpost.georgiancafe.data.network.dishes.model.ModelDishes
import kg.smartpost.georgiancafe.databinding.ItemCategoryBinding
import kg.smartpost.georgiancafe.databinding.ItemDishesBinding
import kg.smartpost.georgiancafe.utils.API_BASE_URL


class DishesRecyclerViewAdapter :
    ListAdapter<ModelDishes.CatDish.Dishes, DishesRecyclerViewAdapter.ViewHolderCat>(DIFF) {


    fun getItemAtPos(position: Int): ModelDishes.CatDish.Dishes {
        return getItem(position)
    }

    private var _binding: ItemDishesBinding? = null

    inner class ViewHolderCat(private val binding: ItemDishesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor", "ResourceType",
            "SetTextI18n"
        )
        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            if (!current.link_photo.isNullOrEmpty())
                Glide.with(binding.root).load("${API_BASE_URL}admin/${current.link_photo}.jpg")
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                    .into(binding.imgDishes)

            binding.txtDishes.text = "${current.name}"
            binding.txtWeight.text = "${current.weight} ${current.edinic}"
            binding.txtCost.text = "${current.cost}"

            binding.btnPlus.setOnClickListener {
                var total = binding.txtTotal.text.toString().toInt()
                if (total == 0) {
                    total += current.min.toInt()
                } else {
                    total++
                }
                var cost = current.cost.toInt()
                cost *= total
                binding.txtCost.text = "$cost"
                binding.txtTotal.text = total.toString()
            }

            binding.btnMinus.setOnClickListener {
                var total = binding.txtTotal.text.toString().toInt()
                var cost = current.cost.toInt()
                if (total == current.min.toInt()) {
                    total = 0
                } else {
                    if (total != 0) {
                        total--
                        cost *= total
                    }
                }
                binding.txtCost.text = cost.toString()
                binding.txtTotal.text = total.toString()

            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCat {
        _binding = ItemDishesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderCat(_binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolderCat, position: Int) {
        holder.onBind(position)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ModelDishes.CatDish.Dishes>() {
            override fun areItemsTheSame(
                oldItem: ModelDishes.CatDish.Dishes,
                newItem: ModelDishes.CatDish.Dishes
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ModelDishes.CatDish.Dishes,
                newItem: ModelDishes.CatDish.Dishes
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}