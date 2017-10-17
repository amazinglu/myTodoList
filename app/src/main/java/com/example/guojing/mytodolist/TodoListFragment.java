package com.example.guojing.mytodolist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guojing.mytodolist.MainActivity;
import com.example.guojing.mytodolist.R;
import com.example.guojing.mytodolist.RecyclerViewAdapter;
import com.example.guojing.mytodolist.model.Todo;
import com.example.guojing.mytodolist.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AmazingLu on 9/10/17.
 */

public class TodoListFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    public RecyclerViewAdapter adapter;
    private MainActivity activity;
    public List<Todo> todoList;

    public TodoListFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mockData();
        adapter = new RecyclerViewAdapter(activity, todoList);
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private void mockData() {
        todoList = ModelUtils.read(getContext(), MainActivity.TODO, new TypeToken<List<Todo>>(){});
        if (todoList == null) {
            todoList = new ArrayList<>();
        }
    }

    void updateTodo(Todo todo) {
        boolean found = false;
        for (int i = 0; i < todoList.size(); ++i) {
            if (TextUtils.equals(todo.id, todoList.get(i).id)) {
                todoList.set(i, todo);
                found = true;
            }
        }
        if (!found) {
            todoList.add(todo);
        }
        /**
         * any time we update a data, we tell the adapter that the data is update and the adapter
         * will do the rest, which is update the data and the status of the view based on what we have
         * set in the adapter
         * */
        adapter.notifyDataSetChanged();
        ModelUtils.save(activity, MainActivity.TODO, todoList);
    }

    void updateTodo(int position, boolean isChecked) {
        todoList.get(position).completed = isChecked;
        ModelUtils.save(activity, MainActivity.TODO, todoList);
        adapter.notifyDataSetChanged();
    }

    void deleteTodo(String id) {
        for (int i = 0; i < todoList.size(); ++i) {
            if (TextUtils.equals(id, todoList.get(i).id)) {
                todoList.remove(i);
                break;
            }
        }
        ModelUtils.save(activity, MainActivity.TODO, todoList);
        adapter.notifyDataSetChanged();
    }
}
