package com.example.news_app

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_app.databinding.ActivityMainBinding
import com.example.news_app.view_model.MainViewModel
import com.example.news_app.view_model_factory.MainViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NewsItemClicked {
    //Your API key is: a90f70f467b94d1e9e374055383ed45c
    //https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=a90f70f467b94d1e9e374055383ed45c

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mAdapter: NewsAdapter
    private var articles = mutableListOf<Article>()
    var pageNum =1

    private var flagMain: Int = -1

    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this@MainActivity)

        mainBinding.newsList.layoutManager = layoutManager
        mAdapter = NewsAdapter(this@MainActivity,articles,this)
        mainBinding.newsList.adapter = mAdapter
        //getNews()

        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(application)).get(MainViewModel::class.java)


        mainViewModel.flag.observe(this, Observer { newFlag->
            flagMain = newFlag
            Log.d("Main_Flag_Observer","flagMain value is $flagMain")
            layoutStatus()
        })

        mainViewModel.vmArticles.observe(this, Observer { vmArt->

            if(flagMain==1)
            {
                Log.d("Main_Observing","Hello")
                /*first clearing the previous list and then adding all the updated list items*/
                articles.clear()
                articles.addAll(vmArt)
                Log.d("After_Adding","Articles are $articles")
                mAdapter.notifyDataSetChanged()
            }

        })

        if(flagMain==-1)
        {
            Log.d("Main_Flag","flagMain value is $flagMain")
            mainViewModel.vmGetNews()
        }

    }

    private fun layoutStatus() {
        if (flagMain==0){
            mainBinding.progressBar.visibility = View.GONE
            mainBinding.loadFailed.visibility = View.VISIBLE
        }
        else{
            mainBinding.progressBar.visibility = View.GONE
            mainBinding.newsList.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(article: Article) {
        Toast.makeText(this,article.title, Toast.LENGTH_SHORT).show()

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(article.url))
    }

    override fun onDeleteClicked(article: Article) {
        Toast.makeText(this,"Delete for ${article.title} clicked", Toast.LENGTH_SHORT).show()
        mainViewModel.vmDeleteArticle(article)
    }



    /*
    private fun getNews() {
        val news = NewsService.newsInstance.getHeadlines("in",pageNum)

        news.enqueue(object :Callback<News>{

            override fun onResponse(call: Call<News>, response: Response<News>) {
                val newsIn = response.body()
                if (newsIn!=null){
                    Log.d("Passed",newsIn.toString())
                    //totalResults = newsIn.totalResults


                    articles.addAll(newsIn.articles)
                    mAdapter.notifyDataSetChanged()
                    mainBinding.progressBar.visibility = View.GONE
                    mainBinding.newsList.visibility = View.VISIBLE


                    //mAdapter.updateNews(ArrayList(newsIn.articles))
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("Failed","Error in Fetching News",t)
                mainBinding.progressBar.visibility = View.GONE
                mainBinding.loadFailed.visibility = View.VISIBLE
            }
        })
    }*/

}