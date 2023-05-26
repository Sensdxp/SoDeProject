package com.example.sodeproject.feature_shop.data

import android.util.Log
import com.example.sodeproject.feature_scanner.data.ShopArticle
import com.example.sodeproject.feature_scanner.data.ShopArticleSession
import com.example.sodeproject.feature_shop.presentation.ActiveInfoShop
import com.example.sodeproject.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class ShopRepositoryImpl @Inject constructor(

): ShopRepository {
    override fun getShop(): Flow<Resource<Int>> {
        return flow {
            ShopSession.shoplist = emptyList()
            emit(Resource.Loading())
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("shops")
            var finished: Boolean = false
            reference.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val shopList = mutableListOf<Shop>()
                    for (shopSnapshot in snapshot.children) {
                        val id = shopSnapshot.child("id").getValue(String::class.java) ?: ""
                        val logo = shopSnapshot.child("logo").getValue(String::class.java) ?: ""
                        val name = shopSnapshot.child("name").getValue(String::class.java) ?: ""
                        val offer = shopSnapshot.child("offer/offerDescription").getValue(String::class.java) ?: ""
                        val offerId = shopSnapshot.child("offer/offerId").getValue(String::class.java) ?: ""
                        val offerCost = shopSnapshot.child("offer/offerCost").getValue(Int::class.java) ?: 0
                        val shopDescription = shopSnapshot.child("shopDescription").getValue(String::class.java) ?: ""

                        val shop = Shop(id, logo, name, offer, offerId, offerCost, shopDescription)
                        shopList.add(shop)
                    }
                    ShopSession.shoplist = shopList
                    finished = true
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error downloading shops: ${error.message}")
                }
            }
            )
            while (finished == false) {
                kotlinx.coroutines.delay(10)
            }

            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun getArticle(shopId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())
            ShopArticleSession.articleList = emptyList()
            val reference =
                FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("shops/$shopId/articles")
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
                    ActiveInfoShop.articleList = articleList
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