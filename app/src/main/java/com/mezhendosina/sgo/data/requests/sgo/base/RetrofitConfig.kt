package com.mezhendosina.sgo.data.requests.sgo.base

import com.google.gson.Gson
import retrofit2.Retrofit

class RetrofitConfig(
    val retrofit: Retrofit,
    val gson: Gson
)