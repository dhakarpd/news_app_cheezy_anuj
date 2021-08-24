package com.example.news_app.view_model

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.news_app.Article
import com.example.news_app.News
import com.example.news_app.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) :AndroidViewModel(application) {

    //LiveData to observe data changes and constantly letting activity know

    val flag = MutableLiveData<Int>(-1)
    val vmArticles = MutableLiveData<List<Article>>()



    fun vmGetNews() {

        /*if condition to survive the configuration changes*/
        if(flag.value==-1){
            val news = NewsService.newsInstance.getHeadlines("in",1)

            /*as we have single instance for NewsInstance so inorder to handle multiple requests
              being done to that interface we will put all requests in a queue*/

            /*This retrofit callback happens on main thread thus postValue for livedata not required*/
            news.enqueue(object : Callback<News> {

                override fun onResponse(call: Call<News>, response: Response<News>) {
                    val newsIn = response.body()
                    if (newsIn!=null){
                        Log.d("Passed",newsIn.toString())
                        //totalResults = newsIn.totalResults

                        flag.value=1
                        vmArticles.value=newsIn.articles


                        //mAdapter.updateNews(ArrayList(newsIn.articles))
                    }
                    else{
                        flag.value=0
                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {

                    Log.d("Failed","Error in Fetching News",t)
                    flag.value=0

                }
            })
        }




    }



    fun vmDeleteArticle(article: Article){
        var temp = mutableListOf<Article>()

        temp.addAll(vmArticles.value as MutableList<Article>)
        Log.d("Before_Removal","Temp is $temp")

        temp.remove(article)

        Log.d("After_Removal","Temp is $temp")
        vmArticles.value = temp
        Log.d("vm_articles_afterwards","vmArticles is ${vmArticles.value}")
    }

}