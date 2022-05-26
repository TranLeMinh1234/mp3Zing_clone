package com.example.mp3zing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3zing.model.modelAppOrApi.PlayTrackManager;
import com.example.mp3zing.model.modelAppOrApi.SpotifyApiHelper;
import com.example.mp3zing.model.modelAppOrApi.Track;
import com.example.mp3zing.model.modelAppOrApi.TrackListAdapter;
import com.example.mp3zing.model.modelAppOrApi.TrackPLayingAdapter;
import com.example.mp3zing.model.view.FragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public PlayTrackManager playTrackManager;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private ImageView play_play_box,pause_play_box,next_play_box,image_track_play_box,close_playing_popup;
    private TextView name_track_play_box,artist_track_play_box;
    private LinearLayoutCompat play_box;
    private PopupWindow popupListPlaying;
    public RecyclerView listPlayingPopup;
    public TrackPLayingAdapter trackPLayingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playTrackManager = new PlayTrackManager(this);
        play_play_box = findViewById(R.id.play_play_box);
        pause_play_box = findViewById(R.id.pause_play_box);
        next_play_box = findViewById(R.id.next_play_box);
        image_track_play_box = findViewById(R.id.image_track_play_box);
        name_track_play_box = findViewById(R.id.name_track_play_box);
        artist_track_play_box = findViewById(R.id.artist_track_play_box);
        play_box = findViewById(R.id.play_box);

        initPopupListPlaying();

        play_play_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_play_box.setVisibility(View.GONE);
                pause_play_box.setVisibility(View.VISIBLE);
                playTrackManager.playMusic(playTrackManager.getPlayTrackIndex());
            }
        });

        pause_play_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause_play_box.setVisibility(View.GONE);
                play_play_box.setVisibility(View.VISIBLE);
                playTrackManager.setFlagModeStop(1);
                playTrackManager.getMediaPlayer().pause();
            }
        });

        next_play_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTrackManager.setFlagModeStop(0);
                playTrackManager.playNextTrack();
            }
        });


        viewPager = findViewById(R.id.view_pager_main);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,3);
        viewPager.setAdapter(fragmentAdapter);
        bottomNavigationView = findViewById(R.id.bottom_navi_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search_list:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.personnal:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.three:
                        viewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.search_list).setCheckable(true);
                        bottomNavigationView.setSelectedItemId(R.id.search_list);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.personnal).setCheckable(true);
                        bottomNavigationView.setSelectedItemId(R.id.personnal);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.three).setCheckable(true);
                        bottomNavigationView.setSelectedItemId(R.id.three);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        play_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupListPlaying.showAtLocation(view, Gravity.BOTTOM,0,0);
            }
        });
    }

    private void initPopupListPlaying()
    {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_list_playing, null);
        close_playing_popup = popupView.findViewById(R.id.close_playing_popup);
        listPlayingPopup = popupView.findViewById(R.id.list_playing_in_popup);

        // create the popup window
        int width = 1080;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupListPlaying = new PopupWindow(popupView, width, height, focusable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            popupListPlaying.setBackgroundDrawable(new ColorDrawable(Color.argb(0.5F,0,0,0)));
        }
        close_playing_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupListPlaying.dismiss();
            }
        });

        /*popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupListPlaying.dismiss();
                return true;
            }
        });*/

        initListPlayingPopup(popupView);
    }

    private void initListPlayingPopup(View view)
    {
        trackPLayingAdapter = new TrackPLayingAdapter(this,new ArrayList<Track>());
        listPlayingPopup = view.findViewById(R.id.list_playing_in_popup);
        listPlayingPopup.setLayoutManager(new GridLayoutManager(this,1));
        listPlayingPopup.setAdapter(trackPLayingAdapter);
    }

    public void setAllInfoPlayBoxBeforePlay(Track track, Boolean playingDisplay)
    {
        if(playingDisplay)
        {
            play_play_box.setVisibility(View.GONE);
            pause_play_box.setVisibility(View.VISIBLE);
        }
        new TrackListAdapter.DownloadImageTask(image_track_play_box)
                .execute(track.getImgUrl());
        name_track_play_box.setText(track.getNameTrack());
        artist_track_play_box.setText(track.getNameArtist());
    }

}