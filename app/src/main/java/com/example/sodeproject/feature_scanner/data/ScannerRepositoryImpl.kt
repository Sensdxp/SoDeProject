package com.example.sodeproject.feature_scanner.data

import com.example.sodeproject.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

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

    override fun updateScore(addScore: Int, userId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())

            var score = -1
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId/userScore")

            reference.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    score = dataSnapshot.getValue(Int::class.java)!!

                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error reading scores: ${error.message}")
                }
            }
            )

            while (score == -1) {
                kotlinx.coroutines.delay(10)
            }
            val updatedScore = score + addScore
            /*
            if(updatedScore < 0){
                emit(Resource.Error("User dose not have enough points"))
                return@flow
            }

             */
            reference.setValue(updatedScore).await()

            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
    /*
    override fun getOffer(userId: String, offerId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())
            ShopArticleSession.offer = ""

            var finished: Boolean = false
            var shopOfferId: String = ""
            var offer: String = ""
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("shops/$userId")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    offer = dataSnapshot.child("offer").value.toString()
                    shopOfferId = dataSnapshot.child("offerId").value.toString()
                    finished = true
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            })
            while (finished == false) {
                kotlinx.coroutines.delay(10)
            }

            if(offerId == shopOfferId){
                ShopArticleSession.offer = offer
                emit(Resource.Success(1))
            }else{
                emit(Resource.Error("Customer has used an invalid code"))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

     */

    override fun checkOfferScore(customerId: String, addScore: Int, shopId: String, offerId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())
            ShopArticleSession.offer = ""
            ShopArticleSession.offerCost = 0
            var finishedOffer: Boolean = false
            var finishedScore: Boolean = false
            var shopOfferId: String = ""
            var offer: String = ""
            var offerCost: Int = 0
            var score: Int = 0



            val referenceOffer = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("shops/$shopId/offer")
            referenceOffer.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    offer = dataSnapshot.child("offerDescription").getValue(String::class.java) ?: ""
                    shopOfferId = dataSnapshot.child("offerId").getValue(String::class.java) ?: ""
                    offerCost = dataSnapshot.child("offerCost").getValue(Int::class.java) ?: 0
                    finishedOffer = true
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            })

            val referenceScore = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$customerId/userScore")
            referenceScore.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    score = dataSnapshot.getValue(Int::class.java)!!
                    finishedScore = true
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            }
            )

            while (finishedOffer == false || finishedScore == false) {
                kotlinx.coroutines.delay(10)
            }

            var newScore = score + addScore - offerCost
            if(offerId == shopOfferId && newScore >= 0){
                ShopArticleSession.offer = offer
                ShopArticleSession.offerCost = offerCost
                emit(Resource.Success(1))
            }else if(newScore < 0){
                emit(Resource.Error("Customer has not enough points for the used code!"))
            }else{
                emit(Resource.Error("Customer has used an invalid code"))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}