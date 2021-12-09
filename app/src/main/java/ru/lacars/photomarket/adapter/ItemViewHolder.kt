package ru.lacars.photomarket.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.lacars.photomarket.data.entity.Item
import ru.lacars.photomarket.databinding.ListItemBinding
import javax.inject.Inject

class ItemViewHolder @Inject constructor(
    private val glide: RequestManager,
    private val binding: ListItemBinding,
    private val listener: ItemAdapter.CallbackListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
        // with(binding) {
        binding.imageTitle.text = item.title
        binding.imageText.text = item.barcode
        glide.load("http://photo-api.webzap.ru" + item.url).into(binding.itemImage)
        binding.button1.setOnClickListener {
            listener.onClickBarcode(item)
        }
        binding.button2.setOnClickListener {
            listener.onClickClear(item)
        }
        binding.button3.setOnClickListener {
            listener.onClickDelete(item)
        }
        // }
    }
}
