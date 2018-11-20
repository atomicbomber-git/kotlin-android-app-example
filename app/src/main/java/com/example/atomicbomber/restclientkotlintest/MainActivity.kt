package com.example.atomicbomber.restclientkotlintest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.post_item_layout.view.*

data class Post (val userId: Int, val id: Int, val body: String, val title: String)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidNetworking.initialize(this)

        val postList = mutableListOf<Post>()
        val viewManager = LinearLayoutManager(this)
        val postItemAdapter = PostItemAdapter(postList)

        recyclerViewPostList.layoutManager = viewManager
        recyclerViewPostList.adapter = postItemAdapter


        AndroidNetworking.get("https://jsonplaceholder.typicode.com/posts")
            .build()
            .getAsObjectList(Post::class.java, object: ParsedRequestListener<List<Post>> {
                override fun onResponse(responsePostList: List<Post>) {
                    postList.addAll(responsePostList)
                    postItemAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                }
            })
    }
}

class PostItemAdapter(private val postDataset: MutableList<Post>): RecyclerView.Adapter<PostItemAdapter.ViewHolder>() {
    class ViewHolder(val postItemView: View): RecyclerView.ViewHolder(postItemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = postDataset.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.postItemView.textViewTitle.text = postDataset[position].title
    }
}
