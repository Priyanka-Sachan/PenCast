package com.example.pencast.ui.addArticle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.example.pencast.login.User
import com.example.pencast.ui.article.Article
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddArticleViewModel(application: Application) : AndroidViewModel(application) {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("/Articles")

    private var user: User

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

    fun submitArticle(title: String, subTitle: String, details: String, imageUrl: String) {
        val timeStamp = System.currentTimeMillis()
        val articleId = "${user.uid}@$timeStamp"
        val article = Article(articleId, title, subTitle, user, details, imageUrl, timeStamp, 0)
        database.child(articleId).setValue(article)
    }
}