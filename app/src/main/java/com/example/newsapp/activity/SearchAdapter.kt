package com.example.newsapp.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.SearchItemBinding


class SearchAdapter(
    private val context: Context, private var itemList: ArrayList<String>,
    private val listener: onSearchAdapterClicked,
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    /* private var itemList: ArrayList<String> = ArrayList()
     init {
         itemList=list

     }*/

    fun addFilterList(filterList: ArrayList<String>) {
        //   itemList.clear()
        itemList = filterList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = itemList.get(position)
        holder.binding.txtSearch.text=item
        holder.itemView.setOnClickListener {
            listener.onItemClicked(item)
        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SearchItemBinding = SearchItemBinding.bind(itemView)

    }


    interface onSearchAdapterClicked {
        fun onItemClicked(item: String)
    }


}