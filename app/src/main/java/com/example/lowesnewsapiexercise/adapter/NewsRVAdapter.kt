package com.example.lowesnewsapiexercise.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lowesnewsapiexercise.R
import com.example.lowesnewsapiexercise.model.Article
import kotlinx.android.synthetic.main.recyclerview_news_snippet.view.*

class NewsRVAdapter(
    private val applicationContext: Context,
    private val webViewDelegate: OpenWebViewDelegate,
    private val articles: ArrayList<Article>
) : RecyclerView.Adapter<NewsRVAdapter.NewsViewHolder>() {

    interface OpenWebViewDelegate {
        fun openWebView(article: Article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_news_snippet, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        if (article.author.isNullOrEmpty()) {
            holder.itemView.authorTextView.visibility = View.GONE
        } else {
            holder.itemView.authorTextView.text =
                applicationContext.getString(R.string.author_string, article.author)
        }
        holder.itemView.titleTextView.text = article.title
        holder.itemView.sourceTextView.text = article.source?.name
        Glide.with(applicationContext)
            .load(article.urlToImage)
            .centerCrop()
            .placeholder(R.drawable.newspaper)
            .into(holder.itemView.articleImageView)
        holder.itemView.setOnClickListener {
            article.url?.let { url ->
                webViewDelegate.openWebView(article)
            }
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}