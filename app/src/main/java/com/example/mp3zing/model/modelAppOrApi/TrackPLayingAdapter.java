package com.example.mp3zing.model.modelAppOrApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3zing.MainActivity;
import com.example.mp3zing.R;

import java.util.List;

public class TrackPLayingAdapter extends RecyclerView.Adapter<TrackPLayingAdapter.TrackPLayingViewHolder>{
    private List<Track> listTrack;
    private Context context;
    private int loadFirst = 0;

    public TrackPLayingAdapter(Context context, List<Track> reList)
    {
        this.listTrack = reList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrackPLayingAdapter.TrackPLayingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracklist_viewholder,parent,false);
        return new TrackPLayingAdapter.TrackPLayingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackPLayingAdapter.TrackPLayingViewHolder holder, int position) {
        Track track = listTrack.get(position);
        holder.name_track.setText(track.getNameTrack());
        holder.more_feature.setVisibility(View.GONE);
        if(loadFirst == 0)
            holder.select_line.setVisibility(View.VISIBLE);
        new TrackListAdapter.DownloadImageTask(holder.image_track)
                .execute(track.getImgUrl());
        holder.name_artist.setText(track.getNameArtist());
        loadFirst++;
        holder.item_track_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).setAllInfoPlayBoxBeforePlay(track,true);
                ((MainActivity) context).playTrackManager.playMusic(position);
                RecyclerView recyclerViewList = ((MainActivity) context).listPlayingPopup;

                for (int i = 0; i < listTrack.size(); ++i) {
                    TrackPLayingViewHolder holderTemp = (TrackPLayingViewHolder) recyclerViewList.findViewHolderForAdapterPosition(i);
                    if(holderTemp != null && holderTemp.select_line != null)
                    {
                        holderTemp.select_line.setVisibility(View.GONE);
                    }
                }
                holder.select_line.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTrack.size();
    }

    public class TrackPLayingViewHolder extends RecyclerView.ViewHolder{
        TextView name_track,name_artist;
        ImageView image_track,more_feature;
        ConstraintLayout item_track_list;
        CardView select_line;


        public TrackPLayingViewHolder(@NonNull View itemView) {
            super(itemView);
            name_artist = itemView.findViewById(R.id.name_artist);
            name_track = itemView.findViewById(R.id.name_track);
            image_track = itemView.findViewById(R.id.image_track);
            more_feature = itemView.findViewById(R.id.more_feature);
            item_track_list = itemView.findViewById(R.id.item_track_list);
            select_line = itemView.findViewById(R.id.select_line);
        }
    }

    public void updateList(List<Track> listNew,int modeUpDate)
    {
        this.listTrack = listNew;
        notifyDataSetChanged();
    }
}
