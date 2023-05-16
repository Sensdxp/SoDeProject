package com.example.sodeproject.feature_score.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_score.data.ScoreRepository
import com.example.sodeproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel


import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val repository: ScoreRepository
    ): ViewModel(){

    val _scoreState = Channel<ScoreState>()
    val scoreState = _scoreState.receiveAsFlow()

    init {
        UserSession.uid?.let { getScore(it) }
    }

    fun getScore(userId: String) = viewModelScope.launch {
        repository.getScore(userId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _scoreState.send(ScoreState(isSuccess = true))
                }
                is Resource.Loading -> {
                    _scoreState.send(ScoreState(isLoading = true))
                }
                is Resource.Error -> {
                    _scoreState.send(ScoreState(isError = true))

                }
            }
        }
    }
}
/*
val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email,password).collect { result ->
            when (result){
                is Resource.Success ->{
                    _signInState.send(SignInState(isSuccess = "Sign In Success "))
                }
                is Resource.Loading ->{
                    _signInState.send(SignInState(isLoading = true))
                }
                is Resource.Error ->{
                    _signInState.send(SignInState(isError = result.message))
                }
            }
        }
    }
}
 */

/*
class ScoreViewModel() : ViewModel(){
    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)

    init {
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        var userId: String? = UserSession.uid
        var tempUser: User? = null
        response.value = DataState.Loading
        val reference = FirebaseDatabase.getInstance().getReference("user/${userId}")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        response.value = DataState.Success(user)
                    } else {
                        response.value = DataState.Failure("Failed to retrieve user data")
                    }
                } else {
                    response.value = DataState.Failure("User data does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = DataState.Failure(error.message)
            }
        })
    }
}

 */

/*
fun saveScoreToFirebase(userId: String, score: Int) {
    val scoreData = Score(userId, score)
    val database = Firebase.database.reference
    val userScoresRef = database.child("users").child(userId).child("score")
    userScoresRef.setValue(scoreData)
}

fun saveScore(userId: String, score: Int) {
    saveScoreToFirebase(userId, score)
}

fun loadScoreFromFirebase(userId: String, onSuccess: (Int) -> Unit, onFailure: () -> Unit) {
    val database = Firebase.database.reference
    val userScoresRef = database.child("users").child(userId).child("score")
    userScoresRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val scoreData = snapshot.getValue(Score::class.java)
            if (scoreData != null) {
                onSuccess(scoreData.score)
            } else {
                onFailure()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onFailure()
        }
    })
}

fun loadScore(userId: String, onSuccess: (Int) -> Unit, onFailure: () -> Unit) {
    loadScoreFromFirebase(userId, onSuccess, onFailure)
}

 */