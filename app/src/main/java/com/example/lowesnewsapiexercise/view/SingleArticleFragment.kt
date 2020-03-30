package com.example.lowesnewsapiexercise.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lowesnewsapiexercise.R
import com.example.lowesnewsapiexercise.model.Article
import com.example.lowesnewsapiexercise.util.parseDate
import kotlinx.android.synthetic.main.single_article_fragment.*

class SingleArticleFragment(private val article: Article) : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.single_article_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            Glide.with(it).load(article.urlToImage).placeholder(R.drawable.newspaper).into(sourceImageView)
        }
        titleTextViewFrag.text=article.title
        if(article.author==null||article.author.equals("")) {
           authorTextViewFrag.visibility=View.GONE
        }else{
            authorTextViewFrag.text = getString(R.string.author_string,article.author)
        }
        descriptionTextViewFragment.visibility = View.VISIBLE
        descriptionTextViewFragment.text = article.description?.trim()
        dateTextViewFrag.apply{
            text = article.publishedAt?.parseDate()
            text?.let{
                visibility=View.VISIBLE
            }
        }
        takeMeThereButton.setOnClickListener{
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(webIntent)
        }
    }

}