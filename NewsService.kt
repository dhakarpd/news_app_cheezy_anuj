package com.example.news_app

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//Your API key is: a90f70f467b94d1e9e374055383ed45c
//https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=a90f70f467b94d1e9e374055383ed45c
//https://newsapi.org/v2/top-headlines?country=in&apiKey=a90f70f467b94d1e9e374055383ed45c

const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "a90f70f467b94d1e9e374055383ed45c"

/*Whatever endpoints we want to call from our app
  methods for those endpoints are declared in this interface*/
interface NewsInterface {

    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getHeadlines(@Query("country")country:String,@Query("page")page:Int):Call<News>
}


/*Singleton object for retrofit; single object around whole wherever required
  Implementing above interface in this using retrofit object*/
object NewsService{
    val newsInstance:NewsInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance = retrofit.create(NewsInterface::class.java)
    }
}