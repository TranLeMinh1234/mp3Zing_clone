package com.example.mp3zing.model.modelAppOrApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DataSpotifyProcess {
    /*public static void loadTrackFromSpotify(ArrayList<Track> listResult) {
        SpotifyApiHelper.getCallGetTrackList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONObject("tracks").getJSONArray("items");
                        int lengthListTrack = jsonArray.length();
                        List<Track> listTrack = new ArrayList<Track>();
                        for (int i = 0 ; i < lengthListTrack;i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("data");
                            *//*Track track = new Track(jsonObject1.getString("id"),jsonObject1.getString("name"),jsonObject1.getJSONObject("artists"),jsonObject1);
                            listTrack.add(track);*//*
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Do what you want to do with the response.
                } else {
                    // Request not successful
                }
            }
        });
    }*/
}
