package com.example.newsapp.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.activity.WebView
import com.example.newsapp.databinding.HeadlineItemBinding
import com.example.newsapp.models.Article
import com.example.newsapp.utility.*
import java.util.*


class HeadlineAdapter(
    private val context: Context,
    private val listener: onHeadlineAdapterClicked
) :
    RecyclerView.Adapter<HeadlineAdapter.HeadlineViewHolder>() {
    private val itemList: ArrayList<Article> = ArrayList()
    fun addList(list: List<Article>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.headline_item, parent, false)
        return HeadlineViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val item = itemList.get(position)


        val index: Int = position % Utility().colorArray.size
        val color = Utility().colorArray[index]
        holder.binding.headlineImg.setBackgroundResource(color)
        holder.binding.headlineImg.imageFromUrl(context, item.urlToImage)
        holder.binding.txtTitle.text = item.title
        holder.binding.txtNewsAuditor.text = item.source?.name
        item.description?.let {
            holder.binding.txtDescrn.text = it
            holder.binding.txtDescrn.isVisible = true

        }

        holder.binding.headlineImg.setOnLongClickListener {
            context.saveImage(item.urlToImage.toString())
            true
        }



        item.publishedAt?.let { holder.binding.txtDate.timeConvertFromString(it) }

        holder.itemView.setOnClickListener {
            val url = item.url
            if (url != null) {
                val intent = Intent(context, WebView::class.java)
                intent.putExtra(Constants.URL, item.url)
                context.startActivity(intent)
            } else context.toastMsg("Unable to loadUrl")

        }
        holder.binding.shareBtn.setOnClickListener {

            item.url?.let { it1 -> listener.onShareButtonClicked(it1) }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class HeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: HeadlineItemBinding = HeadlineItemBinding.bind(itemView)

    }


    interface onHeadlineAdapterClicked {
        fun onShareButtonClicked(url: String)

    }


}