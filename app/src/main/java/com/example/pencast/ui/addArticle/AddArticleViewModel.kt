package com.example.pencast.ui.addArticle

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.pencast.login.User
import com.example.pencast.ui.article.Article
import com.example.pencast.ui.article.ArticleInfo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddArticleViewModel(application: Application) : AndroidViewModel(application) {

    private var articleDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("/Articles")

    private var userDatabase: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("/Users")

    private var user: User

    private var _navigateToArticle = MutableLiveData<Article>()
    val navigateToArticle: LiveData<Article>
        get() = _navigateToArticle

    init {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application)
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

    fun submitArticle(title: String, subTitle: String, details: String, selectedPhotoUri: Uri?) {
        val timeStamp = System.currentTimeMillis()
        val articleId = "${user.uid}@$timeStamp"
        val storage = FirebaseStorage.getInstance().getReference("/articles/${articleId}")
        if (selectedPhotoUri != null) {
            storage.putFile(selectedPhotoUri)
                .addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener {
                        val article = Article(
                            articleId,
                            title,
                            subTitle,
                            user,
                            details,
                            it.toString(),
                            timeStamp,
                            0
                        )
                        articleDatabase.child(articleId).setValue(article)
                        userDatabase.child(user.uid).child("articles").child(articleId).setValue(
                            ArticleInfo(
                                article.articleId,
                                article.title,
                                article.imageUrl
                            )
                        )
                        Toast.makeText(
                            getApplication(),
                            "Your article has been successfully submitted.",
                            Toast.LENGTH_SHORT
                        ).show()
                        _navigateToArticle.value = article
                    }.addOnFailureListener {
                        Log.e("AddArticleViewModel", it.message)
                        Toast.makeText(
                            getApplication(),
                            "Your selected image has not uploaded successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}