package com.example.mp3zing.model.modelAppOrApi;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.mp3zing.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PlayTrackManager {
    private List<Track> listTrack;
    private int playTrackIndex = 0;
    private int modePlaying;
    private MediaPlayer mediaPlayer;
    private int flagLoadDone = 0;
    private int flagModeStop = 0;
    private Context context;

    public PlayTrackManager(Context context)
    {
        initMediaPlayerAndEvent();
        this.context = context;
    }

    public PlayTrackManager(List<Track> listTrack, int playTrackIndex, int modePlaying) {
        this.listTrack = listTrack;
        this.playTrackIndex = playTrackIndex;
        this.modePlaying = modePlaying;
        initMediaPlayerAndEvent();
    }

    public void initMediaPlayerAndEvent()
    {
        mediaPlayer = new MediaPlayer();
    }


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setListTrack(List<Track> listTrack) {
        this.listTrack = listTrack;
    }

    public int getPlayTrackIndex() {
        return playTrackIndex;
    }

    public void setPlayTrackIndex(int playTrackIndex) {
        this.playTrackIndex = playTrackIndex;
    }

    public int getModePlaying() {
        return modePlaying;
    }

    public void setModePlaying(int modePlaying) {
        this.modePlaying = modePlaying;
    }

    public void addTrackToPlayList(Track track)
    {
        listTrack.add(track);
    }

    public List<Track> getListTrack() {
        return this.listTrack;
    }

    public int getFlagLoadDone() {
        return flagLoadDone;
    }

    public void setFlagLoadDone(int flagLoadDone) {
        this.flagLoadDone = flagLoadDone;
    }

    public int getFlagModeStop() {
        return flagModeStop;
    }

    public void setFlagModeStop(int flagModeStop) {
        this.flagModeStop = flagModeStop;
    }

    public void playMusic(int index)
    {
        this.playTrackIndex = index;
       if(mediaPlayer != null)
       {
           if(flagModeStop == 1)
           {
               mediaPlayer.start();
               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                   @Override
                   public void onCompletion(MediaPlayer mediaPlayer) {
                       if(flagModeStop == 0)
                       {
                           playNextTrack();
                           flagModeStop = 0;
                       }
                   }
               });
               flagModeStop = 0;
               return;
           }
           mediaPlayer.stop();
           mediaPlayer.reset();
           mediaPlayer.release();

           String urlStreamOfTrack = listTrack.get(playTrackIndex).getUrlStream();
           if(urlStreamOfTrack != null && !urlStreamOfTrack.isEmpty())
           {
                flagLoadDone++;
           }
           else
           {
               SpotifyApiHelper.getTrackById(listTrack.get(index).getTrackId(), new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {

                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       if (response.isSuccessful()) {
                           try {
                               JSONObject jsonObject = new JSONObject(response.body().string());
                               JSONArray jsonArray = jsonObject.getJSONArray("tracks");
                               if(jsonArray.length() > 0)
                               {
                                   listTrack.get(index).setUrlStream(jsonArray.getJSONObject(0).getString("preview_url"));
                               }

                               flagLoadDone++;
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                           // Do what you want to do with the response.
                       } else {
                           // Request not successful
                       }
                   }
               });
           }

           while(flagLoadDone != 1)
           {
               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
           flagLoadDone = 0;
           playAudio(listTrack.get(this.playTrackIndex).getUrlStream());

       }
    }

    private void playAudio(String url) {
        String audioUrl = url;
        // initializing media player
        mediaPlayer = new MediaPlayer();
        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(audioUrl);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(flagModeStop == 0)
                    {
                        playNextTrack();
                        flagModeStop = 0;
                    }
                }
            });
            flagModeStop = 0;

        } catch (IllegalArgumentException e) {
        } catch (SecurityException e) {
        } catch (IllegalStateException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextTrackIndex()
    {
        if(playTrackIndex >= listTrack.size()-1)
            playTrackIndex = 0;
        else
            playTrackIndex++;

        return playTrackIndex;
    }

    public void playNextTrack()
    {
        getNextTrackIndex();
        ((MainActivity) context).setAllInfoPlayBoxBeforePlay(listTrack.get(playTrackIndex),true);
        this.playMusic(playTrackIndex);
    }
}
