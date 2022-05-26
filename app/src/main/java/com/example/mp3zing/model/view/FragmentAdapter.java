package com.example.mp3zing.model.view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mp3zing.model.view.TrackListFragment;
import com.example.mp3zing.MainActivity;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private int pageNum;

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior, int pageNum) {
        super(fm, behavior);
        this.pageNum = pageNum;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new TrackListFragment();
            case 1:
                return new PlaylistFragment();
            case 2:
                return new TrackListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.pageNum;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Danh sach";
            case 1:
                return "Tien ich&Thong tin";
            case 2:
                return "Tim kiem";
        }
        return "";
    }
}
