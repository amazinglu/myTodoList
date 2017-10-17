package com.example.guojing.mytodolist;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;

import com.example.guojing.mytodolist.animation.RecyclerViewTouchHelperCallback;
import com.example.guojing.mytodolist.model.Todo;
import com.example.guojing.mytodolist.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_CODE_TODO_EDIT = 100;
    public static final String TODO = "todo_list";
    public TodoListFragment todoListFragment;
    private List<Todo> todoList;
    private RecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * use to add one new task
         * */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TodoEditActivity.class);
                startActivityForResult(intent, REQ_CODE_TODO_EDIT);
            }
        });
        loadData();
        setUI();
//        setupFragment();
    }

    /**
     * when use a fragment
     * adapter set is in the fragment
     * the data edit should be all in the fragment because fragment contains the data
     * */
//    private void setupFragment() {
//        todoListFragment = new TodoListFragment(MainActivity.this);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.fragment_container, todoListFragment)
//                .commit();
//        adapter = todoListFragment.adapter;
//        todoList = todoListFragment.todoList;
//    }

    /**
     * use recycler view
     * step 5: set the layout manager and adapter of recycler view
     * */
    private void setUI() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        /**
         * in recycler view, we can seasily change the layout format by using different layout manager
         * */
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        adapter = new RecyclerViewAdapter(MainActivity.this, todoList);
        recyclerView.setAdapter(adapter);
        // ItemTouchHelper
        /**
         * use ItemTouchHelper
         * step 4
         * initialize the touch helper in main activity
         * */
        ItemTouchHelper.Callback callback = new RecyclerViewTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_TODO_EDIT && resultCode == Activity.RESULT_OK) {
            String todoID = data.getStringExtra(TodoEditActivity.KEY_TODO_ID);
            if (todoID != null) {
                deleteTodo(todoID);
//                todoListFragment.deleteTodo(todoID);
            } else {
                Todo todo = data.getParcelableExtra(TodoEditActivity.KEY_TODO);
                updateTodo(todo);
//                todoListFragment.updateTodo(todo);
            }
        }
    }

    private void loadData() {
        todoList = ModelUtils.read(MainActivity.this, TODO, new TypeToken<List<Todo>>(){});
        if (todoList == null) {
            todoList = new ArrayList<Todo>();
            fakeData();
        }
    }

    private void updateTodo(Todo todo) {
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
        ModelUtils.save(MainActivity.this, TODO, todoList);
    }

    public void updateTodo(int position, boolean isChecked) {
        todoList.get(position).completed = isChecked;
        ModelUtils.save(MainActivity.this, TODO, todoList);
        adapter.notifyDataSetChanged();
    }

    private void deleteTodo(String id) {
        for (int i = 0; i < todoList.size(); ++i) {
            if (TextUtils.equals(id, todoList.get(i).id)) {
                todoList.remove(i);
                break;
            }
        }
        ModelUtils.save(MainActivity.this, TODO, todoList);
        adapter.notifyDataSetChanged();
    }

    private void fakeData() {
        Todo todo = new Todo();
        todo.todoText = "new task";
        todoList.add(todo);
        ModelUtils.save(MainActivity.this, TODO, todoList);
    }
 }
