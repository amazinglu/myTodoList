package com.example.guojing.mytodolist.animation;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by AmazingLu on 9/10/17.
 */

/**
 * use ItemTouchHelper
 * step 1:
 * you’ll create an ItemTouchHelper.Callback. T
 * his is the interface that allows you to listen for “move” and “swipe” events.
 * It’s also where you are able to control the state of the view selected,
 * and override the default animations.
 * */

public class RecyclerViewTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter adapter;

    public RecyclerViewTouchHelperCallback(ItemTouchHelperAdapter adapter) {
       this.adapter = adapter;
    }
    /**
     * ItemTouchHelper allows you to easily determine the direction of an event.
     * You must override getMovementFlags() to specify which directions of drags and swipes are supported.
     * Use the helper ItemTouchHelper.makeMovementFlags(int, int) to build the returned flags.
     * We’re enabling dragging and swiping in both directions here.
     * */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dropFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // start means left and end means right
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dropFlags, swipeFlags);
    }
    /**
     * mplementations should return true from isLongPressDragEnabled()
     * in order to support starting drag events from a long press on a RecyclerView item.
     * */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
    /**
     * To enable swiping from touch events that start anywhere within the view,
     * simply return true from isItemViewSwipeEnabled()
     * */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
    /**
     * onMove() and onSwiped() are needed to notify anything in charge of updating the underlying data.
     * So first we’ll create an interface that allows us to pass these event callbacks back up the chain.
     * past the position of the element we taoching to adapter
     * */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
