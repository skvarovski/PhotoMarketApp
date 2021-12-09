package ru.lacars.photomarket.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.lacars.photomarket.base.BaseFragment
import ru.lacars.photomarket.databinding.DashboardFragmentBinding
import ru.lacars.photomarket.util.gone
import ru.lacars.photomarket.util.visible

class DashboardFragment : BaseFragment() {

/*    companion object {
        fun newInstance() = DashboardFragment()
    }*/

    private lateinit var viewModel: DashboardViewModel
    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
        // return inflater.inflate(R.layout.dashboard_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelProvider)[DashboardViewModel::class.java]
        setupView()
        initObserver()
        viewModel.init()
    }
    private fun setupView() {}
    private fun initObserver() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                /*viewModel.dashboardData2.collect { data ->
                    data?.let {
                        binding.apply {
                            textView.text = data.lastUpdate
                        }
                    }
                }*/

                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoadingState -> {
                            Log.d("TEST", "is Visible = true")
                            binding.progress.visible()
                        }
                        is ContentState -> {
                            binding.progress.gone()
                            viewModel.dashboardData2.collect { data ->
                                data?.let {
                                    binding.apply {
                                        textView1.text = "Всего фото: ${data.countTotal}"
                                        textView2.text = "Всего штрихкодов: ${data.countBarcode}"
                                        textView3.text = "Всего без фона: ${data.countClear}"
                                        textView4.text = "${data.lastUpdate}"
                                    }
                                }
                            }
                        }

                        else -> {
                            Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        /*viewLifecycleOwner.lifecycleScope.launch {
            // viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is LoadingState -> {
                        Log.d("TEST", "is Visible = true")
                        binding.progress.visible()
                    }
                    is ContentState -> {
                        binding.progress.gone()
                    }
                }
            }
            *//*viewModel.dashboardData.observe(viewLifecycleOwner) { data ->
                binding.apply {
                    textView.text = data.lastUpdate
                }

            }*//*

            // }
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
