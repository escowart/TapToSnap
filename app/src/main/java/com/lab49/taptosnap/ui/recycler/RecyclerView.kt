package com.lab49.taptosnap.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding

/**
 * Created by Edwin S. Cowart on 05 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 * TODO add support for a multiple viewType binding adapter
 */
fun <Binding : ViewBinding, Item> RecyclerView.setup(
    items: List<Item>,
    inflate: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> Binding,
    @RecyclerView.Orientation orientation: Int,
    spanCount: Int,
    spacing: SpacingItemDecorationOptions? = null,
    update: (binding: Binding, item: Item) -> Unit
) {

    adapter = object : RecyclerView.Adapter<BindingViewHolder<Binding>>() {
        override fun getItemCount(): Int = items.size
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BindingViewHolder<Binding> {
            val inflater = LayoutInflater.from(context)
            return BindingViewHolder(inflate(inflater, parent, false))
        }
        override fun onBindViewHolder(holder: BindingViewHolder<Binding>, position: Int) {
            update(holder.binding, items[position])
        }
    }
    layoutManager = when (spanCount) {
        1 -> LinearLayoutManager(context, orientation, false)
        else -> StaggeredGridLayoutManager(spanCount, orientation)
    }
    spacing?.let {
        addItemDecoration(SpacingItemDecoration(orientation, spanCount, spacing))
    }
}

class BindingViewHolder<Binding : ViewBinding>(val binding: Binding) : RecyclerView.ViewHolder(binding.root)

