package com.example.sodeproject.feature_shop.data

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
                        val offer = shopSnapshot.child("offer").getValue(String::class.java) ?: ""
                        val offerId = shopSnapshot.child("offerId").getValue(String::class.java) ?: ""
                        val shopDescription = shopSnapshot.child("shopDescription").getValue(String::class.java) ?: ""

                        val shop = Shop(id, logo, name, offer, offerId, shopDescription)
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
}