package com.example.guojing.mytodolist.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by AmazingLu on 8/30/17.
 */

public class Todo implements Parcelable{
    public String id;
    public String todoText;
    public boolean completed;
    public Date remainDate;

    protected Todo(Parcel in) {
        id = in.readString();
        todoText = in.readString();
        completed = in.readByte() != 0;
        long date = in.readLong();
        // new Date()
        // Allocates a Date object and initializes it to represent
        // the specified number of milliseconds
        // since the standard base time known as "the epoch", namely January 1, 1970, 00:00:00 GMT
        remainDate = date == 0 ? null : new Date(date);
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(todoText);
        parcel.writeByte((byte) (completed ? 1 : 0));
        // getTime()
        // Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
        parcel.writeLong(remainDate != null ? remainDate.getTime() : 0);
    }

    public Todo() {
        this.id = UUID.randomUUID().toString();
        this.completed = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
