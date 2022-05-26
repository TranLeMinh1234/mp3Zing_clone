package com.example.mp3zing.model.view;

import static android.content.ContentValues.TAG;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3zing.MainActivity;
import com.example.mp3zing.R;
import com.example.mp3zing.model.modelAppOrApi.SpotifyApiHelper;
import com.example.mp3zing.model.modelAppOrApi.Track;
import com.example.mp3zing.model.modelAppOrApi.TrackListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TrackListFragment extends Fragment implements TrackListAdapter.EventTrackListAdapter {

    private List<Track> listTrack;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private TrackListAdapter trackListAdapter;
    private int flagLoadDone = 0;
    private boolean loadFirst = true;
    private PopupWindow popupMoreFeature;
    private LinearLayoutCompat add_track_playlist;
    private ImageView close_more_feature_popup;
    public Track trackWillAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tracklist_fragment,container,false);
        recyclerView = view.findViewById(R.id.list_tracklist_fragment);
        searchView = view.findViewById(R.id.search_track_tracklist_fragment);

        listTrack = new ArrayList<Track>();
        trackListAdapter = new TrackListAdapter(getContext(),listTrack, this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(trackListAdapter);

        initPopupListPlaying();
        loadTrackFromSpotify("a");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadTrackFromSpotify(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0)
                {
                    loadTrackFromSpotify("a");
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void loadTrackFromSpotify(String searchValue) {
        SpotifyApiHelper.getCallGetTrackList(searchValue,new Callback() {
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
                        List<Track> listTrackConvert = new ArrayList<Track>();
                        for (int i = 0 ; i < lengthListTrack;i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("data");
                            String nameArtist = jsonObject1.getJSONObject("artists").getJSONArray("items").getJSONObject(0).getJSONObject("profile").getString("name");
                            Track track = new Track(jsonObject1.getString("id"),jsonObject1.getString("name"),jsonObject1.getJSONObject("artists"),jsonObject1,nameArtist);
                            track.setImgUrl(jsonObject1.getJSONObject("albumOfTrack").getJSONObject("coverArt").getJSONArray("sources").getJSONObject(0).getString("url"));
                            listTrackConvert.add(track);
                        }

                        listTrack = listTrackConvert;
                        if(loadFirst)
                        {
                            Context context = getContext();
                            ((MainActivity) context).setAllInfoPlayBoxBeforePlay(listTrack.get(0), false);
                            ((MainActivity) context).playTrackManager.setListTrack(listTrack);
                            ((MainActivity) context).trackPLayingAdapter.updateList(listTrack,1);
                        }
                        loadFirst = false;
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
        while(flagLoadDone != 1)
        {
            //wait until load data end
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        trackListAdapter.updateList(listTrack,1);
        flagLoadDone = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initPopupListPlaying()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_more_feature, null);
        close_more_feature_popup = popupView.findViewById(R.id.close_more_feature_popup);
        add_track_playlist= popupView.findViewById(R.id.add_track_playlist);

        // create the popup window
        int width = 1080;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupMoreFeature = new PopupWindow(popupView, width, height, focusable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            popupMoreFeature.setBackgroundDrawable(new ColorDrawable(Color.argb(0.5F,0,0,0)));
        }
        close_more_feature_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMoreFeature.dismiss();
            }
        });

        add_track_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMoreFeature.dismiss();

            }
        });

    }

    @Override
    public void moreFeatureCLicked(Track track,View view) {
        trackWillAdd = track;
        popupMoreFeature.showAtLocation(view, Gravity.BOTTOM,0,0);
    }
}
