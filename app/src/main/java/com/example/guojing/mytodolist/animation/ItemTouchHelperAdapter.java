package com.example.guojing.mytodolist.animation;

/**
 * Created by AmazingLu on 9/10/17.
 */


/**
 * use ItemTouchHelper
 * step 2:
 * we’ll create an interface that allows us to pass these event callbacks back up the chain.
 * adapter => change the data
 * thia adapter changes the data base on the ItemTouchHelper Callback
 * */
public interface ItemTouchHelperAdapter {
    /**
     * the action when item in recycler view is moved
     * */
    void onItemMove(int fromPosition, int toPosition);
    /**
     * the action when item in recycler view is deleted
     * */
    void onItemDismiss(int position);
}
