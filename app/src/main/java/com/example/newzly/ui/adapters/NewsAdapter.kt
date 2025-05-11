package com.example.newzly.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newzly.R
import com.example.newzly.data.model.Article
import com.example.newzly.databinding.ArticleListItemBinding

class NewsAdapter(
    private val articles: List<Article>,
    private val onItemClick: (Article) -> Unit,
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    init {
        setHasStableIds(true)
    }

    inner class NewsViewHolder(val binding: ArticleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.articleTitle.text = articles[position].title
        Glide.with(holder.binding.articleImage.context).load(articles[position].urlToImage).error(R.drawable.ic_broken).transition(
            DrawableTransitionOptions.withCrossFade(1000)
        ).into(holder.binding.articleImage)

        holder.itemView.setOnClickListener { onItemClick(articles[position]) }
    }

    override fun getItemCount(): Int = articles.size

    override fun getItemId(position: Int): Long = articles[position].url.hashCode().toLong()
}