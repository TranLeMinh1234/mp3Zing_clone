package com.example.mp3zing.model.modelAppOrApi;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyApiHelper {
    public static Call getCallGetTrackList(String searchValue,Callback callback)
    {
        String type = "tracks";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://spotify23.p.rapidapi.com/search/?q="+searchValue+"&type=tracks&offset=0&limit=20&numberOfTopResults=5")
                .get()
                .addHeader("X-RapidAPI-Host", "spotify23.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "55892f6900mshf0d1ceb6555332ep107a5cjsn6dcf92222b63")
                .build();

        Call call  = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call getTrackById(String Id, Callback callback)
    {
        String type = "tracks";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://spotify23.p.rapidapi.com/tracks/?ids="+Id)
                .get()
                .addHeader("X-RapidAPI-Host", "spotify23.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "55892f6900mshf0d1ceb6555332ep107a5cjsn6dcf92222b63")
                .build();

        Call call  = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
