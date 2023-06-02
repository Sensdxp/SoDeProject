package com.example.sodeproject.feature_score.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_scanner.data.Trans
import com.example.sodeproject.feature_score.presentation.Stats
import com.example.sodeproject.feature_shop.data.Shop
import com.example.sodeproject.feature_shop.data.ShopSession
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


class ScoreRepositoryImpl @Inject constructor(

): ScoreRepository {

    override fun getScore(userId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())

            var score = -1
            var name = ""
            var trans = ""
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId")

            reference.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    trans = dataSnapshot.child("trans").getValue(String::class.java) ?: ""
                    name = dataSnapshot.child("userName").getValue(String::class.java) ?: ""
                    score = dataSnapshot.child("userScore").getValue(Int::class.java) ?: 0

                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            }
            )

            while (score == -1) {
                kotlinx.coroutines.delay(10)
            }
            Log.d("HEY",trans)
            UserSession.trans = parseInputString(trans)
            UserSession.userName = name
            UserSession.score = score


            emit(Resource.Success(score))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun saveScore(userId: String, score: Int): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())
            ChartSession.mCustomer = listOf(80f, 65f, 90f, 75f, 30f)
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId/score")
            reference.setValue(score).await()

            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun getChartData(userId: String): Flow<Resource<Stats>> {
        return flow {
            emit(Resource.Loading())

            var finished: Boolean = false
            var finished1: Boolean = false

            val listmCustomer = mutableStateListOf<Float>()
            val listmScore = mutableStateListOf<Float>()
            val listmOffer = mutableStateListOf<Float>()

            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("shops/$userId/stats")

            reference.addValueEventListener( object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val totalScore = dataSnapshot.child("totalScore").getValue(Int::class.java) ?: 0

                    val mCustomerSnapshot = dataSnapshot.child("mCustomer")
                    val mScoreSnapshot = dataSnapshot.child("mScore")
                    val mOfferSnapshot = dataSnapshot.child("mOffer")

                    listmCustomer.clear()
                    listmScore.clear()
                    listmOffer.clear()

                    mCustomerSnapshot.children.forEach { childSnapshot ->
                        val value = childSnapshot.getValue(Float::class.java)
                        value?.let {
                            listmCustomer.add(it)
                        }
                    }

                    mScoreSnapshot.children.forEach { childSnapshot ->
                        val value = childSnapshot.getValue(Float::class.java)
                        value?.let {
                            listmScore.add(it)
                        }
                    }

                    mOfferSnapshot.children.forEach { childSnapshot ->
                        val value = childSnapshot.getValue(Float::class.java)
                        value?.let {
                            listmOffer.add(it)
                        }
                    }

                    ChartSession.mCustomer = emptyList()
                    //ChartSession.mCustomer.clear()
                    //ChartSession.mCustomer.addAll(listmCustomer)
                    ChartSession.mCustomer = listmCustomer

                    //ChartSession.mScore.clear()
                    //ChartSession.mScore.addAll(listmScore)
                    ChartSession.mScore = emptyList()
                    ChartSession.mScore = listmScore

                    //ChartSession.mOffer.clear()
                    //ChartSession.mOffer.addAll(listmOffer)
                    ChartSession.mOffer = emptyList()
                    ChartSession.mOffer = listmOffer

                    ChartSession.totalScore = totalScore

                    finished = true

                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            }
            )

            val reference1 = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("shops/$userId")

            reference1.addValueEventListener( object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val shopName = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                    UserSession.userName = shopName

                    finished1 = true

                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            }
            )

            while (finished == false && finished1 == false) {
                kotlinx.coroutines.delay(10)
            }

            emit(Resource.Success(Stats(
                mCustomer = listOf(80f, 65f, 90f, 75f, 30f),
                mScore = listOf(0f, 50f, 100f, 150f, 200f),
                mOffer = listOf(0f, 2500f, 5000f, 7500f, 10000f),
                totalScore = 10000)))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}

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