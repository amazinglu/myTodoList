package com.example.guojing.mytodolist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.guojing.mytodolist.animation.ItemTouchHelperAdapter;
import com.example.guojing.mytodolist.model.Todo;
import com.example.guojing.mytodolist.util.UIutils;

import java.util.Collections;
import java.util.List;

/**
 * Created by AmazingLu on 9/9/17.
 */

/**
 * use recycle view
 * step 4: create the recycler view adapter
 * implement: constructor, onCreateViewHolder, onBindViewHolder and getItemCount(do not forget this)
 * */

public class RecyclerViewAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter{
    private MainActivity activity;
    private List<Todo> data;

    public RecyclerViewAdapter(MainActivity activity, List<Todo> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.main_list_item, parent, false);
        View view = activity.getLayoutInflater().inflate(R.layout.main_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    /**
     * if the recycler view show only one element, layout of main_list_item
     * must have a height of match_parent
     * */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // insert the data
        if (position > 0) {
            System.out.println("here");
        }
        final int i = position;
        RecyclerViewHolder vh = (RecyclerViewHolder)holder;
        final Todo todo = data.get(position);
        vh.todoText.setText(todo.todoText);
        /**
         * using onCheckedChanged in recycler view adapter must have the under line
         * reason is here:
         * https://stackoverflow.com/questions/27070220/android-recyclerview-notifydatasetchanged-illegalstateexception
         *
         * */
        vh.doneCheckedBox.setOnCheckedChangeListener(null);
        vh.doneCheckedBox.setChecked(todo.completed);
        UIutils.setTextViewStrikeThrough(vh.todoText, todo.completed);

        /**
         * click each item in the list view will go to the edit activity
         * */
        // for recycler view, the main view of the view holder is  RecyclerView.ViewHolder holder.itemView
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TodoEditActivity.class);
                intent.putExtra(TodoEditActivity.KEY_TODO, todo);
                activity.startActivityForResult(intent, MainActivity.REQ_CODE_TODO_EDIT);
            }
        });

        /**
         * click the check box will update the status of the check box in main activity
         * */
        vh.doneCheckedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                activity.todoListFragment.updateTodo(i, !todo.completed);
                activity.updateTodo(i, !todo.completed);
                /**
                 * important:
                 * here update may call notifyDataSetChanged() again, which may lead to an infinite loop
                 * therefore we have to add line 62 before line 63
                 * As long as we set the listener to "null" before we set Checked,
                 * then it will update the UI without triggering the listener,
                 * avoiding the infinite loop. Then we can set the listener after.
                 * */
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    /**
     * ItemTouchHelperAdapter part
     * use ItemTouchHepler
     * step 3
     * implements the ItemTouchHelperAdapter interface => how to change the data
     * */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // change to element position in data
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; ++i) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; --i) {
                Collections.swap(data, i, i - 1);
            }
        }
        // ask the adapter to change the layout
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }
}
