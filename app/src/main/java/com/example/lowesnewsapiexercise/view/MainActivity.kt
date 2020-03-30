package com.example.lowesnewsapiexercise.view

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.lowesnewsapiexercise.R
import com.example.lowesnewsapiexercise.adapter.NewsRVAdapter
import com.example.lowesnewsapiexercise.adapter.NewsRVAdapter.OpenWebViewDelegate
import com.example.lowesnewsapiexercise.model.Article
import com.example.lowesnewsapiexercise.viewmodel.NewsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), OpenWebViewDelegate {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsRVAdapter: NewsRVAdapter
    var articles = ArrayList<Article>()
    var showingLatestNews = true
    var pageNumber = 1
    var query = ""

    //keep track of if the recyclerview hit the bottom, to avoid sending multiple requests back-to-back
    var atBottomOnce = false
    var countDownTimer = object : CountDownTimer(5000, 5000) {
        override fun onFinish() {
            atBottomOnce = false
        }

        override fun onTick(millisUntilFinished: Long) {}
    }

    val progressBarTimer = object : CountDownTimer(1000, 1000) {
        override fun onFinish() {
            progressBar.visibility = View.GONE
        }

        override fun onTick(millisUntilFinished: Long) {}
    }

    private var singleArticleFragment: SingleArticleFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpRecyclerView()
        setUpButtons()
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.articlesLiveData.observe(this, Observer { articleResults ->
            articleResults?.let {
                updateList(articleResults)
                progressBarTimer.start()
            }
        })
        newsViewModel.noResultsLiveData.observe(this, Observer {
            showAlertDialog()
        })
        newsViewModel.getLatestNews(1)
        progressBar.visibility = View.VISIBLE
    }

    fun showAlertDialog() {
        MaterialAlertDialogBuilder(ContextThemeWrapper(this, R.style.AppTheme))
            .setTitle(R.string.no_content_header)
            .setMessage(R.string.no_content_content)
            .setNegativeButton(R.string.close_application) { _, _ ->
                exitProcess(0)
            }.setNeutralButton(R.string.cancel_alert) { dialog, _ ->
                dialog.dismiss()
            }.create()
            .show()
    }

    private fun setUpButtons() {
        newsRVAdapter = NewsRVAdapter(applicationContext, this, articles)
        newsRecyclerView.adapter = newsRVAdapter
        //tell when the user hits the bottom of the rv, signifying that more results are needed
        val onScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && !atBottomOnce) {
                        if (showingLatestNews) {
                            newsViewModel.getLatestNews(++pageNumber)
                        } else {
                            newsViewModel.searchNews(
                                query,
                                ++pageNumber
                            )
                        }
                        atBottomOnce = true
                        countDownTimer.start()
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        newsRecyclerView.addOnScrollListener(onScrollListener)
    }

    private fun setUpRecyclerView() {
        searchImageView.setOnClickListener {
            if (queryEditText.text.toString().trim() != "") {
                query = queryEditText.text.toString().trim()
                uiUpdatesFromButtons()
                newsViewModel.searchNews(query, pageNumber)
                topNewsButton.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    this@MainActivity,
                    R.string.input_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        topNewsButton.setOnClickListener {
            uiUpdatesFromButtons()
            newsViewModel.getLatestNews(pageNumber)
            topNewsButton.visibility = View.GONE
        }
    }

    private fun uiUpdatesFromButtons() {
        showingLatestNews = !showingLatestNews
        pageNumber = 1
        newsViewModel.articlesLiveData.value = ArrayList()
        articles = ArrayList()
        queryEditText.text?.clear()
        progressBar.visibility = View.VISIBLE
    }

    private fun updateList(list: ArrayList<Article>) {
        if (list.size > articles.size) {
            for (i in articles.size until list.size) {
                articles.add(list[i])
            }
            newsRVAdapter.notifyDataSetChanged()
        } else {
            articles = list
            newsRVAdapter = NewsRVAdapter(applicationContext, this, articles)
            newsRecyclerView.adapter = newsRVAdapter
        }
    }

    override fun openWebView(article: Article) {
        singleArticleFragment = SingleArticleFragment(article)
        singleArticleFragment?.let { fragment ->
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_right,
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_right
                )
                .addToBackStack(fragment.tag)
                .add(R.id.frameLayoutMain, fragment)
                .commit()
        }
    }

    override fun onRestart() {
        super.onRestart()
        //in case the user exited and reentered before a timer was up
        atBottomOnce = false
        progressBarTimer.start()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer.cancel()
        progressBarTimer.cancel()
    }

    override fun onDestroy() {
        newsViewModel.articlesLiveData.removeObservers(this)
        newsViewModel.noResultsLiveData.removeObservers(this)
        super.onDestroy()
    }
}
