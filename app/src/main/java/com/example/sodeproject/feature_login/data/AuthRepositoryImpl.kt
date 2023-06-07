package com.example.sodeproject.feature_login.data

import com.example.sodeproject.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
)  : AuthRepository{

    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            // login
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            // saving userId
            val uid = result.user?.uid
            if(uid != null){
                UserSession.uid = uid
            }
            // check seller status
            UserSession.seller = getSeller(uid.toString())
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun registerUser(userName: String,email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            //Create User Account
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Failed to create user")

            val user = User(uid, 0, false)
            val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$uid")
            reference.setValue(user).await()
            val referenceUsername = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$uid/userName")
            referenceUsername.setValue(userName).await()
            val referenceMail = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$uid/userMail")
            referenceMail.setValue(email).await()

            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }



    suspend fun getSeller(userId:String): Boolean? {
        var seller: Boolean? = null
        val reference = FirebaseDatabase.getInstance("https://sodeproject-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/$userId/seller")

        reference.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                seller = dataSnapshot.getValue(Boolean::class.java)!!

            }

            override fun onCancelled(error: DatabaseError) {
                println("Fehler beim Lesen des Scores: ${error.message}")
            }
        }
        )

        while (seller == null) {
            kotlinx.coroutines.delay(10)
        }

        return seller
    }
}