package com.example.pencast.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pencast.ui.article.Article
import com.example.pencast.ui.article.ArticleInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FeedViewModel : ViewModel() {

    private var childEventListener: ChildEventListener? = null
    private var articleDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Articles")
    private var usersDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users")

    private var _feeds = MutableLiveData<List<Article>>()
    val feeds: LiveData<List<Article>>
        get() = _feeds

    private var feedList = ArrayList<Article>()

    init {
        attachDatabaseReadListener()
    }

    private fun attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val feed: Article? =
                        dataSnapshot.getValue(Article::class.java)
                    if (feed != null) {
                        feedList.add(feed)
                        _feeds.value = feedList
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            articleDatabase.addChildEventListener(childEventListener as ChildEventListener)
        }
    }

    fun isFavourite(article: Article) {
        articleDatabase.child(article.articleId).child("favouriteOf")
            .setValue(article.favouriteOf + 1)
        usersDatabase.child(FirebaseAuth.getInstance().uid.toString()).child("favourites")
            .child(article.articleId)
            .setValue(ArticleInfo(article.articleId, article.title, article.imageUrl))
    }
}