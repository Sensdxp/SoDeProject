package com.example.sodeproject.feature_scanner.data

import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    override fun updateScore(addScore: Int, userId: String, shopId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())

            var score = -1
            var trans = ""
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId")

            reference.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    trans = dataSnapshot.child("trans").getValue(String::class.java)!!
                    score = dataSnapshot.child("userScore").getValue(Int::class.java)!!
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
            reference.child("userScore").setValue(updatedScore).await()

            var totalScore = 0
            var mCustomer = 0f
            var mScore = 0f
            var mOffer = 0f
            var finished = false
            val referenceShop = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$shopId")

            referenceShop.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    totalScore = dataSnapshot.child("stats/totalScore").getValue(Int::class.java) ?: 0
                    mCustomer = dataSnapshot.child("stats/mCustomer/m1").getValue(Float::class.java) ?: 0f
                    mScore = dataSnapshot.child("stats/mScore/m1").getValue(Float::class.java) ?: 0f
                    mOffer = dataSnapshot.child("stats/mOffer/m1").getValue(Float::class.java) ?: 0f
                    finished = true
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error reading scores: ${error.message}")
                }
            }
            )
            while (finished == false) {
                kotlinx.coroutines.delay(10)
            }

            var newtrans: String = appendToInputString(trans, Trans(getCurrentDate(),addScore,UserSession.userName))

            val newtotalScore = totalScore + addScore
            referenceShop.child("stats/totalScore").setValue(newtotalScore).await()
            referenceShop.child("stats/mCustomer/m1").setValue(mCustomer + 1).await()
            referenceShop.child("stats/mScore/m1").setValue(mScore + 1).await()
            referenceShop.child("stats/mOffer/m1").setValue(mOffer +1).await()
            reference.child("trans").setValue(newtrans).await()

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

    override fun checkChallenges(
        userId: String,
        addScore: Int,
        shopId: String
    ): Flow<Resource<Int>> {
        return flow {
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

            var finishedChallenges = false
            val referenceChal = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("challanges")
            referenceChal.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    d1 = snapshot.child("c1/description").value as String
                    d2 = snapshot.child("c2/description").value as String
                    d3 = snapshot.child("c3/description").value as String
                    r1 = snapshot.child("c1/reward").getValue(Int::class.java) ?: 0
                    r2 = snapshot.child("c2/reward").getValue(Int::class.java) ?: 0
                    r3 = snapshot.child("c3/dreward").getValue(Int::class.java) ?: 0

                    finishedChallenges = true
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error downloading shops: ${error.message}")
                }
            }
            )

            var finishedUserChallenges = false
            val referenceUserChal = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/${userId}/challenges")
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

                    finishedUserChallenges = true
                }
                override fun onCancelled(error: DatabaseError) {
                    println("Error downloading shops: ${error.message}")
                }
            }
            )

            while (finishedChallenges == false || finishedUserChallenges == false) {
                kotlinx.coroutines.delay(10)
            }

            val referenceBonus = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/${userId}")
            if(do1 == false && shopId == "gNKg4v5wwxaHrF9N4VDLECqCrLq2") {
                referenceUserChal.child("c1/progress").setValue(p1 + addScore).await()
                if (p1 + addScore > g1) {
                    referenceUserChal.child("c1/done").setValue(true).await()
                    referenceBonus.child("userScore").setValue(r1).await()
                }
            }else if(do2 == false && shopId == "DeO67GMvINcH0WyPJGivTKvkI6H3"){
                referenceUserChal.child("c2/progress").setValue(p2 + addScore ).await()
                if(p2 + addScore  > g2){
                    referenceUserChal.child("c2/done").setValue(true).await()
                    referenceBonus.child("userScore").setValue(r2).await()
                }
            }
            if(do3 == false){
                referenceUserChal.child("c3/progress").setValue(p3 + addScore).await()
                if(p3 + addScore  > g3){
                    referenceUserChal.child("c3/done").setValue(true).await()
                    referenceBonus.child("userScore").setValue(r3).await()
                }
            }



            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}

data class Trans(val datum: String, val punkte: Int, val shop: String)

fun parseInputString(input: String): List<Trans> {
    val transList = mutableListOf<Trans>()

    val transStrings = input.split("*")
    for (transString in transStrings) {
        val transValues = transString.split("|")
        if (transValues.size == 3) {
            val datum = transValues[0]
            val punkte = transValues[1].toIntOrNull()
            val shop = transValues[2]

            if (punkte != null) {
                val trans = Trans(datum, punkte, shop)
                transList.add(trans)
            }
        }
    }

    return transList
}

fun appendToInputString(input: String, newTrans: Trans): String {
    val transString = "${newTrans.datum}|${newTrans.punkte}|${newTrans.shop}"
    val appendedString = if (input.isNotEmpty()) "$transString*$input" else transString
    return appendedString
}

fun getCurrentDate(): String {
    val currentDate = Date()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return dateFormat.format(currentDate)
}

