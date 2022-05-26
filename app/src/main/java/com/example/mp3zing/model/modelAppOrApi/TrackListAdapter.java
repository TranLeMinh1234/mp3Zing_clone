package com.example.mp3zing.model.modelAppOrApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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

import java.io.InputStream;
import java.util.List;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>{

    private List<Track> listTrack;
    private Context context;
    private EventTrackListAdapter eventTrackListAdapter;

    public TrackListAdapter(Context context, List<Track> reList,EventTrackListAdapter eventTrackListAdapter)
    {
        this.listTrack = reList;
        this.context = context;
        this.eventTrackListAdapter = eventTrackListAdapter;
    }

    @NonNull
    @Override
    public TrackListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracklist_viewholder,parent,false);
        return new TrackListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackListViewHolder holder, int position) {
        Track track = listTrack.get(position);
        holder.name_track.setText(track.getNameTrack());
        new DownloadImageTask(holder.image_track)
                .execute(track.getImgUrl());
        holder.name_artist.setText(track.getNameArtist());

        holder.item_track_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).setAllInfoPlayBoxBeforePlay(track,true);
                ((MainActivity) context).playTrackManager.setListTrack(listTrack);
                ((MainActivity) context).trackPLayingAdapter.updateList(listTrack,1);
                ((MainActivity) context).playTrackManager.playMusic(position);
            }
        });

        holder.more_feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventTrackListAdapter.moreFeatureCLicked(track,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTrack.size();
    }

    public class TrackListViewHolder extends RecyclerView.ViewHolder{
        TextView name_track,name_artist;
        ImageView image_track,more_feature;
        ConstraintLayout item_track_list;


        public TrackListViewHolder(@NonNull View itemView) {
            super(itemView);
            name_artist = itemView.findViewById(R.id.name_artist);
            name_track = itemView.findViewById(R.id.name_track);
            image_track = itemView.findViewById(R.id.image_track);
            more_feature = itemView.findViewById(R.id.more_feature);
            item_track_list = itemView.findViewById(R.id.item_track_list);
        }
    }

    public void updateList(List<Track> listNew,int modeUpDate)
    {
        this.listTrack = listNew;
        notifyDataSetChanged();
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        public Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    public interface EventTrackListAdapter{
        public void moreFeatureCLicked(Track track, View view);
    }
}

