package com.example.mp3zing.model.modelDatabase;

import android.database.Cursor;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONStringer;

import java.lang.reflect.Type;

public class Playlist extends BaseClass{
    private String name_playlist;
    private String name_creator;
    private String dataString;

    public Playlist()
    {
        dataString = "[]";
    }

    public Playlist(String name_playlist, String name_creator, String dataString) {
        this.name_playlist = name_playlist;
        this.name_creator = name_creator;
        this.dataString = dataString == null ? "[]": dataString;
    }

    public String getName_playlist() {
        return name_playlist;
    }

    public void setName_playlist(String name_playlist) {
        this.name_playlist = name_playlist;
    }

    public String getName_creator() {
        return name_creator;
    }

    public void setName_creator(String name_creator) {
        this.name_creator = name_creator;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    @Override
    public void initWithCursor(Cursor cursor)
    {
        this.id = cursor.getInt(0);
        this.name_playlist = cursor.getString(3);
        this.name_creator = cursor.getString(2);
        this.dataString = cursor.getString(1);
    }
}
