package com.example.sodeproject.feature_scanner.data

import com.example.sodeproject.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor(

): ScannerRepository {
    override fun getArticles(userId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())
            ShopArticleSession.articleList = emptyList()
            val reference =
                FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("shops/$userId/articles")
            var finished: Boolean = false
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val articleList = mutableListOf<ShopArticle>()

                    for (articleSnapshot in dataSnapshot.children) {
                        val articleId = articleSnapshot.key as String
                        val description = articleSnapshot.child("description").value as String
                        val scorePoints = articleSnapshot.child("scorePoints").value as Long

                        val article = ShopArticle(articleId, description, scorePoints.toInt())
                        articleList.add(article)
                    }
                    ShopArticleSession.articleList = articleList
                    finished = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Behandlung von Fehlern
                    println("Database Error: ${databaseError.message}")
                }
            })

            while (finished == false) {
                kotlinx.coroutines.delay(10)
            }

            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}