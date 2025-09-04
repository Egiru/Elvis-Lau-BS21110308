package com.example.bottomnavigationbar.network

import com.example.bottomnavigationbar.sampledata.MeetingDetail
import com.example.bottomnavigationbar.sampledata.MeetingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("meeting-record/get")
    suspend fun getMeetings(
        @Query("pageSize") pageSize: Int,
        @Query("pageNo") pageNo: Int,
        @Header("Authorization") token: String = "Bearer 58|KKedhO6m7QR3QAPfCObNR0MMRuVTaqA8yVPrnktJ7638eb79",
        @Header("Accept") accept: String = "application/json"
    ): Response<MeetingResponse>

    @GET("meeting-record/get/{id}")
    suspend fun getMeetingById(
        @Path("id") id: Int,
        @Header("Authorization") token: String = "Bearer 58|KKedhO6m7QR3QAPfCObNR0MMRuVTaqA8yVPrnktJ7638eb79",
        @Header("Accept") accept: String = "application/json"
    ): Response<MeetingDetail>
}