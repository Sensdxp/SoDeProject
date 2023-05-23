package com.example.sodeproject.feature_score.data

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
import javax.inject.Inject


class ScoreRepositoryImpl @Inject constructor(

): ScoreRepository {

    override fun getScore(userId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())

            var score = -1
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId/userScore")

            reference.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    score = dataSnapshot.getValue(Int::class.java)!!

                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            }
            )

            while (score == -1) {
                kotlinx.coroutines.delay(10)
            }

            UserSession.score = score

            emit(Resource.Success(score))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun saveScore(userId: String, score: Int): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())

            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId/score")
            reference.setValue(score).await()

            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun getChartData(userId: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading())
            var finished: Boolean = false

            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId/stats")

            reference.addValueEventListener( object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val totalScore = dataSnapshot.child("totalScore").getValue(Int::class.java) ?: 0
                    val mCustomerData = dataSnapshot.child("mCustomer").children.mapNotNull { it.getValue(Float::class.java) }
                    val mScoreData = dataSnapshot.child("mScore").children.mapNotNull { it.getValue(Float::class.java) }
                    val mOfferData = dataSnapshot.child("mOffer").children.mapNotNull { it.getValue(Float::class.java) }

                    ChartSession.mCustomer.clear()
                    ChartSession.mCustomer.addAll(mCustomerData.takeLast(5))

                    ChartSession.mScore.clear()
                    ChartSession.mScore.addAll(mScoreData.takeLast(5))

                    ChartSession.mOffer.clear()
                    ChartSession.mOffer.addAll(mOfferData.takeLast(5))

                    ChartSession.totalScore = totalScore

                    finished = true

                }

                override fun onCancelled(error: DatabaseError) {
                    println("Fehler beim Lesen des Scores: ${error.message}")
                }
            }
            )

            while (!finished) {
                kotlinx.coroutines.delay(10)
            }

            emit(Resource.Success(1))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }
}