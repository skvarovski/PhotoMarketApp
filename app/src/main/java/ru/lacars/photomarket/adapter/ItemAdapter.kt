package ru.lacars.photomarket.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import ru.lacars.photomarket.data.entity.Item
import ru.lacars.photomarket.databinding.ListItemBinding
import ru.lacars.photomarket.ui.gallery.GalleryFragment
import javax.inject.Inject

class ItemAdapter @Inject constructor(
    private var glide: RequestManager,
    // private val listener: OnClickListener
) : ListAdapter<Item, ItemViewHolder>(ItemsDiffItemCallback) {

    // private val differ = AsyncListDiffer(this, ItemsDiffItemCallback)
    var items: List<Item>
        get() = currentList
        set(value) = submitList(value)

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(items[position].id!!)
    }

    private lateinit var listener: CallbackListener
    interface CallbackListener {
        fun onClickBarcode(item: Item)
        fun onClickClear(item: Item)
        fun onClickDelete(item: Item)
    }
    fun initCallbackListener(fragment: GalleryFragment) {
        this.listener = fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(glide, binding, this.listener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private object ItemsDiffItemCallback : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            /*override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.url == newItem.url &&
                    oldItem.title == newItem.title &&
                    oldItem.barcode == newItem.barcode &&
                    oldItem.clear == newItem.clear &&
                    oldItem.id == newItem.id
            }*/

            override fun getChangePayload(oldItem: Item, newItem: Item) = Any()
        }
    }
}
