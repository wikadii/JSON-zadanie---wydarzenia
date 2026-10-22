package com.example.zadaniejson;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter(this);
        recyclerView.setAdapter(eventAdapter);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Calendar.class, (JsonDeserializer<Calendar>) (json,type, ctx) -> {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                .parse(json.getAsString()));
                        return calendar;
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }).create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/wikadii/JSON-zadanie/refs/heads/main/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiConnection apiConnection = retrofit.create(ApiConnection.class);
        Call<List<Event>> call = apiConnection.getEvents();
        call.enqueue(
                new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<Event> events = response.body();
                        Log.d("Aplikacja", "Pobrano " + events.size() + " wydarzeń");
                        eventAdapter.setEvents(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Log.e("Aplikacja", "Nie udało pobrać się JSON'a", t);
                    }
                }
        );

    }
}