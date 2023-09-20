package uz.frodo.bootcampnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.bootcampnews.ItemClick
import uz.frodo.bootcampnews.News
import uz.frodo.bootcampnews.databinding.ItemBinding

class RvAdapter(var itemClick: ItemClick):ListAdapter<News, ViewHolder>(MyDiffUtil()) {

    inner class VH(val binding: ItemBinding):ViewHolder(binding.root)
    inner class VH1(val binding1: ItemBinding):ViewHolder(binding1.root)

    class MyDiffUtil:DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 0){
            val binding1 = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return VH1(binding1)
        }
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is VH1){
            holder.binding1.itemTitle.text = getItem(position).title
            holder.binding1.itemBody.text = getItem(position).body
            holder.binding1.itemMore.setOnClickListener {
                itemClick.onMoreClick(holder.binding1.root,getItem(position))
            }
            holder.binding1.card.setOnClickListener {
                itemClick.onItemClick(getItem(position))
            }

            holder.binding1.itemContainer.setPadding(0,32,0,0)
        }

        if (holder is VH){
            holder.binding.itemTitle.text = getItem(position).title
            holder.binding.itemBody.text = getItem(position).body
            holder.binding.itemMore.setOnClickListener {
                itemClick.onMoreClick(holder.binding.root,getItem(position))
            }
            holder.binding.card.setOnClickListener {
                itemClick.onItemClick(getItem(position))
            }
        }
    }
}