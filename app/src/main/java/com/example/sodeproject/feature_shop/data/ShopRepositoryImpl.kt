package com.example.sodeproject.feature_shop.data

import android.util.Log
import com.example.sodeproject.feature_login.data.UserSession
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
            var do1: Boolean = false
            var do2: Boolean = false
            var do3: Boolean = false
            var g1: Int = 0
            var g2: Int = 0
            var g3: Int = 0
            var p1: Int = 0
            var p2: Int = 0
            var p3: Int = 0
            var d1 : String = ""
            var d2 : String = ""
            var d3 : String = ""
            var r1 : Int = 0
            var r2 : Int = 0
            var r3 : Int = 0




            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("shops")
            var finished1: Boolean = false
            var finished2: Boolean = false
            var finished3: Boolean = false
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
                    finished1 = true
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error downloading shops: ${error.message}")
                }
            }
            )

            val referenceChal = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("challanges")
            referenceChal.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    d1 = snapshot.child("c1/description").value as String
                    d2 = snapshot.child("c2/description").value as String
                    d3 = snapshot.child("c3/description").value as String
                    r1 = snapshot.child("c1/reward").getValue(Int::class.java) ?: 0
                    r2 = snapshot.child("c2/reward").getValue(Int::class.java) ?: 0
                    r3 = snapshot.child("c3/dreward").getValue(Int::class.java) ?: 0

                    finished2 = true
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error downloading shops: ${error.message}")
                }
            }
            )

            val referenceUserChal = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/${UserSession.uid}/challenges")
            referenceUserChal.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    do1 = snapshot.child("c1/done").getValue(Boolean::class.java) ?: false
                    do2 = snapshot.child("c2/done").getValue(Boolean::class.java) ?: false
                    do3 = snapshot.child("c3/done").getValue(Boolean::class.java) ?: false
                    g1 = snapshot.child("c1/goal").getValue(Int::class.java) ?: 0
                    g2 = snapshot.child("c2/goal").getValue(Int::class.java) ?: 0
                    g3 = snapshot.child("c3/goal").getValue(Int::class.java) ?: 0
                    p1 = snapshot.child("c1/progress").getValue(Int::class.java) ?: 0
                    p2 = snapshot.child("c2/progress").getValue(Int::class.java) ?: 0
                    p3 = snapshot.child("c3/progress").getValue(Int::class.java) ?: 0

                    finished3 = true
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error downloading shops: ${error.message}")
                }
            }
            )
            while (finished1 == false && finished2 == false && finished3 == false) {
                kotlinx.coroutines.delay(10)
            }

            ShopSession.challangelist = listOf(Challenges(d1,r1,do1,g1,p1),Challenges(d2,r2,do2,g2,p2),Challenges(d3,r3,do3,g3,p3))

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

    override fun getShopInst(shopId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())

            val reference2 =
                FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("shops/$shopId")
            var finished2: Boolean = false
            reference2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val id = dataSnapshot.child("id").getValue(String::class.java) ?: ""
                    val logo = dataSnapshot.child("logo").getValue(String::class.java) ?: ""
                    val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                    val offer = dataSnapshot.child("offer/offerDescription").getValue(String::class.java) ?: ""
                    val offerId = dataSnapshot.child("offer/offerId").getValue(String::class.java) ?: ""
                    val offerCost = dataSnapshot.child("offer/offerCost").getValue(Int::class.java) ?: 0
                    val shopDescription = dataSnapshot.child("shopDescription").getValue(String::class.java) ?: ""

                    val shop = Shop(id, logo, name, offer, offerId, offerCost, shopDescription)

                    ActiveInfoShop.shop = shop
                    finished2 = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Behandlung von Fehlern
                    println("Database Error: ${databaseError.message}")
                }
            }
            )


            ShopArticleSession.articleList = emptyList()
            val reference =
                FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("shops/$shopId/articles")
            var finished1: Boolean = false
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
                    finished1 = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Behandlung von Fehlern
                    println("Database Error: ${databaseError.message}")
                }
            })

            while (finished1 == false || finished2 == false) {
                kotlinx.coroutines.delay(10)
            }

            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}