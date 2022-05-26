package com.example.mp3zing.model.view;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3zing.DBHelper.DatabaseGenericHelper;
import com.example.mp3zing.R;
import com.example.mp3zing.model.modelAppOrApi.PlaylistAdapter;
import com.example.mp3zing.model.modelDatabase.Playlist;

import java.util.List;

public class PlaylistFragment extends Fragment {

    private List<Playlist> listPLayList;
    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private ConstraintLayout add_playlist_box;
    private DatabaseGenericHelper databaseGenericHelper;
    private PopupWindow popupAddPlaylist;
    private ImageView close_add_playlist_popup;
    private EditText edit_name_playlist,edit_name_creator;
    private Button add_playlist_btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.playlist_fragment,container,false);

        recyclerView = view.findViewById(R.id.list_playlist);
        add_playlist_box = view.findViewById(R.id.add_playlist_box);

        databaseGenericHelper = new DatabaseGenericHelper(getContext(),new Playlist());
        listPLayList = (List<Playlist>) databaseGenericHelper.getList(null);
        playlistAdapter = new PlaylistAdapter(getContext(),listPLayList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(playlistAdapter);

        initPopupAddPlayList();

        add_playlist_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddPlaylist.showAtLocation(view, Gravity.BOTTOM,0,0);
            }
        });

        return view;
    }

    private void initPopupAddPlayList()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_add_playlist, null);
        close_add_playlist_popup = popupView.findViewById(R.id.close_add_playlist_popup);
        edit_name_creator = popupView.findViewById(R.id.edit_name_creator);
        edit_name_playlist = popupView.findViewById(R.id.edit_name_playlist);
        add_playlist_btn = popupView.findViewById(R.id.add_playlist_btn);

        // create the popup window
        int width = 1080;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupAddPlaylist = new PopupWindow(popupView, width, height, focusable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            popupAddPlaylist.setBackgroundDrawable(new ColorDrawable(Color.argb(0.5F,0,0,0)));
        }
        close_add_playlist_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddPlaylist.dismiss();
            }
        });

        add_playlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Playlist playlist = new Playlist(edit_name_playlist.getText().toString(),edit_name_creator.getText().toString(),"[]");
                playlist.setId(0);
                databaseGenericHelper.insertRecord(playlist);
                listPLayList = (List<Playlist>) databaseGenericHelper.getList(null);
                playlistAdapter.updateList(listPLayList,1);
                popupAddPlaylist.dismiss();
            }
        });

        /*popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupListPlaying.dismiss();
                return true;
            }
        });*/

    }

}
