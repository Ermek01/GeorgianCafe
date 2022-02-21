package kg.smartpost.georgiancafe.ui.dishes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint
import kg.smartpost.georgiancafe.data.local.UserPreferencesViewModel
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.data.network.dishes.model.ModelDishes
import kg.smartpost.georgiancafe.databinding.FragmentDishesBinding
import kg.smartpost.georgiancafe.ui.category.viewmodels.CategoryViewModel
import kg.smartpost.georgiancafe.ui.dishes.utils.CategoryRecyclerViewAdapter
import kg.smartpost.georgiancafe.ui.dishes.utils.DishesRecyclerViewAdapter
import kg.smartpost.georgiancafe.ui.dishes.viewmodels.DishesViewModel

@AndroidEntryPoint
class DishesFragment : Fragment(), CategoryRecyclerViewAdapter.CategoryClickListener {


    private val categoryViewModel by viewModels<CategoryViewModel>()
    private val dishesViewModel by viewModels<DishesViewModel>()
    private var _binding: FragmentDishesBinding? = null
    private val binding get() = _binding!!

    private val page = "menu"

    var dishes = mutableListOf<ModelDishes.CatDish.Dishes>()

    private var hochu_cat: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            hochu_cat = arguments?.getInt("hochu_cat")
        }

        getDishes()

        binding.swipeRefresh.setOnRefreshListener {
            getDishes()
        }

    }

    private fun getDishes() {
        dishesViewModel.getDishes(page)
        dishesViewModel.dishes.observe(this) { dishes ->
            when (dishes) {
                is NetworkResponse.Success -> {
                    dishes.data?.let { dishes ->
                        this.dishes.clear()
                        this.dishes.addAll(dishes.cat_dish.dishes)

                        val categoryAdapter = CategoryRecyclerViewAdapter(hochu_cat, this)
                        val layoutManager = FlexboxLayoutManager(requireContext())
                        layoutManager.justifyContent = JustifyContent.CENTER
                        layoutManager.alignItems = AlignItems.CENTER
                        layoutManager.flexDirection = FlexDirection.ROW
                        layoutManager.flexWrap = FlexWrap.WRAP
                        binding.categoryList.layoutManager = layoutManager
                        binding.categoryList.adapter = categoryAdapter
                        categoryAdapter.submitList(dishes.cat_dish.category)

                        val dishesAdapter = DishesRecyclerViewAdapter()
                        binding.dishesList.adapter = dishesAdapter
                        dishesAdapter.submitList(dishes.cat_dish.dishes.filter { it.id_menu.toInt()==hochu_cat})
                        binding.swipeRefresh.isRefreshing = false

                    }
                }

                is NetworkResponse.Error -> {
                    Toast.makeText(
                        requireContext(),
                        dishes.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResponse.Loading -> {
                }
            }
        }
    }

    override fun onCategoryClick(position: Int, id: Int) {
        hochu_cat = id
        val adapter = DishesRecyclerViewAdapter()
        binding.dishesList.adapter = adapter
        adapter.submitList(dishes.filter { it.id_menu.toInt()==id})
    }
}