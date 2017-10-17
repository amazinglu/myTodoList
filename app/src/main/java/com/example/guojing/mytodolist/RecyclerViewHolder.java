package com.example.guojing.mytodolist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by AmazingLu on 9/9/17.
 */

/**
 * use recycler view
 * step 1: recycler view xml in main activity
 * step 2: complie recycle view widget in build.gradle
 * step 3: create a view holder
 * */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    View mainView;
    TextView todoText;
    CheckBox doneCheckedBox;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mainView = itemView;
        todoText = itemView.findViewById(R.id.main_list_text);
        doneCheckedBox = itemView.findViewById(R.id.main_list_completed);
    }
}
