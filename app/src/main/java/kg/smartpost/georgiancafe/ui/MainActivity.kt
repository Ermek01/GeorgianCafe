package kg.smartpost.georgiancafe.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kg.smartpost.georgiancafe.BuildConfig
import kg.smartpost.georgiancafe.R
import kg.smartpost.georgiancafe.data.local.UserPreferencesViewModel
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.databinding.ActivityMainBinding
import kg.smartpost.georgiancafe.ui.category.utils.MenuCategoriesRecyclerViewAdapter
import kg.smartpost.georgiancafe.ui.category.viewmodels.CategoryViewModel
import kg.smartpost.georgiancafe.ui.utils.EventListener
import kg.smartpost.georgiancafe.utils.MyState
import kg.smartpost.georgiancafe.utils.NetworkStatusTracker
import kg.smartpost.georgiancafe.utils.NetworkStatusViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuCategoriesRecyclerViewAdapter.CategoryClickListener, EventListener{

    private lateinit var binding: ActivityMainBinding

    private val userPreferencesViewModel by viewModels<UserPreferencesViewModel>()
    private val categoryViewModel by viewModels<CategoryViewModel>()

    private val page = "menu"

    private val viewModel: NetworkStatusViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val networkStatusTracker = NetworkStatusTracker(this@MainActivity)
                    return NetworkStatusViewModel(networkStatusTracker) as T
                }
            },
        ).get(NetworkStatusViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val versionName = BuildConfig.VERSION_NAME
        binding.txtVersion.text = "Версия: $versionName"

        viewModel.state.observe(this) { state ->
            when (state) {
                MyState.Fetched -> getCategories()
                MyState.Error -> "Error"
            }
        }



        binding.appBarMain.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.txtLogout.setOnClickListener {

        }

        binding.btnHome.setOnClickListener {
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
            if (navController().currentDestination?.id != R.id.homeFragment) {
                navController.popBackStack()
            }
            Handler().postDelayed({
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }, 200)
        }

        binding.categoryList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.appBarMain.txtPhone.setOnClickListener {
            val phone = binding.appBarMain.txtPhone.text.toString()
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${phone}")
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (navController().currentDestination?.id == R.id.dishesFragment) {
            if (binding.appBarMain.txtPhone.visibility == View.VISIBLE) {
                binding.appBarMain.txtPhone.visibility = View.GONE
            }
        }
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()

        }
    }

    private fun getCategories() {
        categoryViewModel.getCategory(page)
        categoryViewModel.categories.observe(this) { categories ->
            when (categories) {
                is NetworkResponse.Success -> {
                    categories.data?.let { categories ->
                        val adapter = MenuCategoriesRecyclerViewAdapter(this)
                        binding.categoryList.adapter = adapter
                        adapter.submitList(categories.cat_dish.category)
                    }
                }

                is NetworkResponse.Error -> {
                    Toast.makeText(
                        this,
                        categories.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResponse.Loading -> {
                }
            }
        }
    }

    override fun onCategoryClick(position: Int, id: Int) {
        val bundle = Bundle()
        bundle.putInt("hochu_cat", id)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
        if (navController().currentDestination?.id == R.id.dishesFragment) {
            navController.navigate(R.id.dishesFragment, bundle, NavOptions.Builder()
                .setPopUpTo(R.id.dishesFragment, true)
                .build())
        }
        else {
            navController.navigate(R.id.dishesFragment, bundle)
            binding.appBarMain.txtPhone.visibility = View.VISIBLE
        }
        Handler().postDelayed({
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }, 200)

    }

    private fun navController() = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)

    override fun onEventPhoneInCategoryClick(phone: String) {
        binding.appBarMain.txtPhone.text = phone
    }

    override fun onEventInHomeFragment(phone: String) {
        binding.appBarMain.txtPhone.text = phone
        binding.appBarMain.txtPhone.visibility = View.VISIBLE
    }

}