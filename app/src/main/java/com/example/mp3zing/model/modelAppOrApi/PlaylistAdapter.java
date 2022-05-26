package com.example.mp3zing.model.modelAppOrApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3zing.MainActivity;
import com.example.mp3zing.R;
import com.example.mp3zing.model.modelDatabase.Playlist;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>{
    private List<Playlist> listPlaylist;
    private Context context;

    public PlaylistAdapter(Context context, List<Playlist> reList)
    {
        this.listPlaylist = reList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistAdapter.PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_viewholder,parent,false);
        return new PlaylistAdapter.PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.PlaylistViewHolder holder, int position) {
        Playlist playList = listPlaylist.get(position);
        holder.name_playlist.setText(playList.getName_playlist());
        holder.name_creator.setText(playList.getName_creator());
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout item_playlist;
        TextView name_playlist, name_creator;
        ImageView playlist_more_feature,image_playlist;


        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            item_playlist = itemView.findViewById(R.id.item_playlist);
            name_playlist = itemView.findViewById(R.id.name_playlist);
            playlist_more_feature = itemView.findViewById(R.id.playlist_more_feature);
            name_creator = itemView.findViewById(R.id.name_creator);
            image_playlist = itemView.findViewById(R.id.image_playlist);
        }
    }

    public void updateList(List<Playlist> listNew,int modeUpDate)
    {
        this.listPlaylist = listNew;
        notifyDataSetChanged();
    }
}
