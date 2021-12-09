package ru.lacars.photomarket.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.lacars.photomarket.R
import ru.lacars.photomarket.adapter.ItemAdapter
import ru.lacars.photomarket.base.BaseFragment
import ru.lacars.photomarket.data.entity.Item
import ru.lacars.photomarket.databinding.GalleryFragmentBinding
import ru.lacars.photomarket.ui.barcode.BarcodeFragment
import javax.inject.Inject

class GalleryFragment : BaseFragment(), ItemAdapter.CallbackListener {

    @Inject
    lateinit var itemAdapter: ItemAdapter
    private lateinit var viewModel: GalleryViewModel
    private var _binding: GalleryFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    // var items = mutableListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GalleryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelProvider)[GalleryViewModel::class.java]
        setupView()
        initObserver()
        viewModel.init()
    }

    private fun setupView() {
        itemAdapter.initCallbackListener(this)
        binding.rvAdapter.adapter = itemAdapter
        binding.rvAdapter.layoutManager = LinearLayoutManager(context)
    }
    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoadingState -> {
                            Log.d("TEST", "GalleryFragment LoadState")
                        }
                        is ContentState -> {

                            // val list = mutableListOf<Item>()
                            // list.addAll(viewModel.galleries.value?.toList()!!)
                            // itemAdapter.submitList(list)
                            // itemAdapter.items = viewModel.galleries.value?.toList()!!
                            // itemAdapter.items =
                            // items.clear()

                            viewModel.galleries.collect {
                                if (it != null) {
                                    val list = mutableListOf<Item>()
                                    list.addAll(it)
                                    itemAdapter.submitList(list)
                                    Log.d("TEST", "GalleryFragment Adapter Submit $it")
                                }

                                Log.d("TEST", "GalleryFragment data = ${itemAdapter.items}")
                            }
                        }
                        else -> { Log.d("TEST", "Unknown UI status") }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun toastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickBarcode(item: Item) {
        setFragmentResultListener(BarcodeFragment.REQUEST_KEY) { key, bundle ->
            // toastMessage(bundle.toString())
            Log.d("TEST", "DETEKT LOG = ${bundle["data"]}")
        }
        findNavController().navigate(R.id.barcodeFragment, bundleOf("id" to item.id.toString()))
    }

    override fun onClickClear(item: Item) {
        viewModel.clearByImage(item)
    }

    override fun onClickDelete(item: Item) {
        viewModel.deleteByItem(item)
    }

    companion object {
        private const val TAG = "GalleryFragment"
        val REQUEST_KEY = "GalleryFragment_REQUEST_KEY"
    }
}
