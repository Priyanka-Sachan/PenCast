package com.example.pencast.ui.me.aboutMe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.pencast.login.User
import com.example.pencast.ui.article.Article
import com.example.pencast.ui.article.ArticleInfo
import com.google.firebase.database.*

class AboutMeViewModel(application: Application) : AndroidViewModel(application) {

    private var articleDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("/Articles")
    private var userDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("/Users")

    private var articleWorkChildEventListener: ChildEventListener? = null
    private var articleInterestedChildEventListener: ChildEventListener? = null

    val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(getApplication())

    lateinit var user: User

    private var _article = MutableLiveData<Article>()
    val article: LiveData<Article>
        get() = _article


    private var _articleWorkList = MutableLiveData<List<ArticleInfo>>()
    val articleWorkList: LiveData<List<ArticleInfo>>
        get() = _articleWorkList
    private var articleWorkArrayList = ArrayList<ArticleInfo>()

    private var _articleInterestedList = MutableLiveData<List<ArticleInfo>>()
    val articleInterestedList: LiveData<List<ArticleInfo>>
        get() = _articleInterestedList
    private var articleInterestedArrayList = ArrayList<ArticleInfo>()

    init {
        getUser()
        attachArticleWorkDatabaseListener()
        attachArticleInterestedDatabaseListener()
    }

    private fun getUser() {
        user =
            User(
                sharedPreferences.getString("UID", "No-Uid")!!,
                sharedPreferences.getString("USERNAME", "Guest User")!!,
                sharedPreferences.getString(
                    "PROFILE_IMAGE_URL",
                    "https://firebasestorage.googleapis.com/v0/b/pencast-1163e.appspot.com/o/profileImages%2FdeaultProfile.png?alt=media&token=d088380e-1465-4b3e-883b-69362271c84a"
                )!!,
                sharedPreferences.getString("STATUS", "Come join us at PenCast!!")!!
            )
    }

    private fun attachArticleWorkDatabaseListener() {
        if (articleWorkChildEventListener == null) {
            articleWorkChildEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val article: ArticleInfo? =
                        dataSnapshot.getValue(ArticleInfo::class.java)
                    if (article != null) {
                        articleWorkArrayList.add(article)
                        _articleWorkList.value = articleWorkArrayList
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            userDatabase.child(user.uid).child("articles")
                .addChildEventListener(articleWorkChildEventListener as ChildEventListener)
        }
    }

    private fun attachArticleInterestedDatabaseListener() {
        if (articleInterestedChildEventListener == null) {
            articleInterestedChildEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val article: ArticleInfo? =
                        dataSnapshot.getValue(ArticleInfo::class.java)
                    if (article != null) {
                        articleInterestedArrayList.add(article)
                        _articleInterestedList.value = articleInterestedArrayList
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            userDatabase.child(user.uid).child("favourites")
                .addChildEventListener(articleInterestedChildEventListener as ChildEventListener)
        }
    }

    fun getArticle(articleId: String) {
        articleDatabase.child(articleId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _article.value = dataSnapshot.getValue(Article::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ProfileFragment", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

}