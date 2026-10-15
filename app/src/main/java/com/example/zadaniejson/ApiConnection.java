package com.example.zadaniejson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiConnection {
    @GET("events.json")
    public Call<List<Event>> getEvents();
}
