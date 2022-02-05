package com.lab49.taptosnap.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Edwin S. Cowart on 05 February, 2022
 * Lab49 Job Application
 * Tap To Snap
 */
fun <Binding : ViewBinding, Item> RecyclerView.newAdapter(
    items: ArrayList<Item>,
    inflate: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> Binding,
    bind: (binding: Binding, item: Item) -> Unit
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
            bind(holder.binding, items[position])
        }
    }
}

class BindingViewHolder<Binding : ViewBinding>(val binding: Binding) : RecyclerView.ViewHolder(binding.root)
