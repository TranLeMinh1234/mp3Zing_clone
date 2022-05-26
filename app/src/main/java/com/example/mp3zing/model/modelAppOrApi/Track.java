package com.example.mp3zing.model.modelAppOrApi;

import com.example.mp3zing.model.modelDatabase.BaseClass;

import org.json.JSONArray;
import org.json.JSONObject;

public class Track extends BaseClass {
    private String trackId;
    private String nameTrack;
    private String urlStream;
    private String imgUrl;
    private JSONObject artist;
    private String nameArtist;
    private JSONObject jsonObject;

    public Track(){}

    public Track(String trackId, String urlStream, String imgUrl,JSONObject artist,String nameTrack,JSONObject jsonObject,String nameArtist) {
        this.trackId = trackId;
        this.urlStream = urlStream;
        this.imgUrl = imgUrl;
        this.artist = artist;
        this.nameTrack = nameTrack;
        this.jsonObject = jsonObject;
        this.nameArtist = nameArtist;
    }

    public Track(String trackId, String nameTrack, JSONObject artist, JSONObject jsonObject,String nameArtist)
    {
        this.trackId = trackId;
        this.artist = artist;
        this.nameArtist = nameArtist;
        this.nameTrack = nameTrack;
        this.jsonObject = jsonObject;
    }

    public String getNameTrack() {
        return nameTrack;
    }

    public void setNameTrack(String nameTrack) {
        this.nameTrack = nameTrack;
    }

    public JSONObject getArtist() {
        return artist;
    }

    public void setArtist(JSONObject artist) {
        this.artist = artist;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUrlStream() {
        return urlStream;
    }

    public void setUrlStream(String urlStream) {
        this.urlStream = urlStream;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
