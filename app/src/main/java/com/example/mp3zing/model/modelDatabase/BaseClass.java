package com.example.mp3zing.model.modelDatabase;

import android.database.Cursor;

public class BaseClass {
    protected int id;

    public void initWithCursor(Cursor cursor)
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

