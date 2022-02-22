package kg.smartpost.georgiancafe.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.smartpost.georgiancafe.R
import kg.smartpost.georgiancafe.data.local.UserPreferencesViewModel
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.databinding.FragmentHomeBinding
import kg.smartpost.georgiancafe.ui.utils.EventListener
import kg.smartpost.georgiancafe.ui.viewmodel.DataViewModel
import kg.smartpost.georgiancafe.utils.MyState
import kg.smartpost.georgiancafe.utils.NetworkStatusTracker
import kg.smartpost.georgiancafe.utils.NetworkStatusViewModel
import kg.smartpost.georgiancafe.utils.hasInternetConnection

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val dataViewModel by viewModels<DataViewModel>()
    private val userPreferencesViewModel by viewModels<UserPreferencesViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val page = "start"

    lateinit var eventListener: EventListener

    private var hochu_cat = -1
    private var phone = ""

    private val viewModel: NetworkStatusViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val networkStatusTracker = NetworkStatusTracker(requireContext())
                    return NetworkStatusViewModel(networkStatusTracker) as T
                }
            },
        ).get(NetworkStatusViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(this) { state ->
            when (state) {
                MyState.Fetched -> getData()
                MyState.Error -> Toast.makeText(
                    requireContext(),
                    "Нет подключения к интернету!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.state.observe(this) { state ->
                when (state) {
                    MyState.Fetched -> getData()
                    MyState.Error -> Toast.makeText(
                        requireContext(),
                        "Нет подключения к интернету!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.btnHochuEst.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("hochu_cat", hochu_cat)
            findNavController().navigate(R.id.dishesFragment, bundle)
            eventListener.onEventInHomeFragment(phone)
        }

        binding.txtPhone.setOnClickListener {
            val phone = binding.txtPhone.text.toString()
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${phone}")
            startActivity(intent)
        }

    }

    private fun getData() {
        dataViewModel.getData(page)
        binding.pbDog.visibility = View.VISIBLE
        dataViewModel.response.observe(this) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    response.data?.let { data ->
                        hochu_cat = data.user.cat_hochu
                        phone = data.user.phone
                        eventListener.onEventPhoneInCategoryClick(phone)
                        binding.txtDiscount.text = "${data.user.personal_discount}%"
                        binding.txtDiscount.textSize = "${data.user.size}".toFloat()
                        binding.txtCode.text = "${data.user.title_kod + " " + data.user.code}"
                        binding.txtPhone.text = data.user.phone.toString()
                        binding.txtCurrentDiscount.text = data.user.title
                        binding.txtEda.text = data.user.eda
                        binding.txtBonus.text = data.user.bonusi
                        binding.txtEda.textSize = data.user.size_hochu.toFloat()
                        binding.txtBonus.textSize = data.user.size_bonusi.toFloat()
                        binding.swipeRefresh.isRefreshing = false
                        binding.text.text = data.user.text
                        binding.text.textSize = data.user.text_size.toFloat()
                    }
                    binding.pbDog.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                }

                is NetworkResponse.Error -> {
                    binding.pbDog.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.swipeRefresh.isRefreshing = false
                }

                is NetworkResponse.Loading -> {
                    binding.pbDog.visibility = View.VISIBLE
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        eventListener = context as EventListener
    }

}