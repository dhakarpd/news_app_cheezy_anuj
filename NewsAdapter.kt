package com.example.news_app

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//Anuj Bhaiya (Recycler View)
/*Interface used for click events; Click karne ke baad jo bhi kaam karna hai that's the job of Activity
  so to amke the activity know that spmething is clicked we require a callback and simple way to do
  these callbacks is interface; and then implement methods of interface in activity*/
class NewsAdapter(val context: Context, val articles: List<Article>,private val listener:NewsItemClicked) :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val art=ArrayList<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false)
        val viewHolder = ArticleViewHolder(view)

        viewHolder.newsImage.setOnClickListener {
            listener.onItemClicked(articles[viewHolder.adapterPosition])
        }

        viewHolder.deleteButton.setOnClickListener {
            listener.onDeleteClicked(articles[viewHolder.adapterPosition])
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.newsTitle.text = article.title
        holder.newsDescription.text = article.description
        Glide.with(context).load(article.urlToImage).into(holder.newsImage)

        /*holder.itemView.setOnClickListener {
            Toast.makeText(context,article.title,Toast.LENGTH_SHORT).show()

            val builder = CustomTabsIntent.Builder();
            val customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(article.url));
        }*/



        /*val article = art[position]
        holder.newsTitle.text = article.title
        holder.newsDescription.text = article.description
        Glide.with(context).load(article.urlToImage).into(holder.newsImage)*/

        /*holder.itemView.setOnClickListener {
            Toast.makeText(context,article.title,Toast.LENGTH_SHORT).show()
        }*/

    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun updateNews(updatedNews:ArrayList<Article>){
        art.clear()
        art.addAll(updatedNews)

        notifyDataSetChanged()
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var newsImage = itemView.findViewById<ImageView>(R.id.newsImage)
        var newsTitle = itemView.findViewById<TextView>(R.id.newsTitle)
        var newsDescription = itemView.findViewById<TextView>(R.id.newsDescription)
        var deleteButton = itemView.findViewById<ImageView>(R.id.deleteBtn)

    }
}

interface NewsItemClicked{
    fun onItemClicked(article: Article)

    fun onDeleteClicked(article: Article)
}