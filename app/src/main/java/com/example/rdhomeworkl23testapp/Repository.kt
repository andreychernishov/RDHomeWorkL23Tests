package com.example.rdhomeworkl23testapp

import retrofit2.Response
import retrofit2.Retrofit

class Repository(private val apiInterface: ApiInterface) {
    suspend fun getCurrencyByName(name: String): Response<BitcoinResponce> {
        return apiInterface.getCryptoByName(name)
    }
}