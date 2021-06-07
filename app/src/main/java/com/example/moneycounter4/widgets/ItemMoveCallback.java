package com.example.moneycounter4.widgets;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemMoveCallback {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onItemMoveFinish(RecyclerView rv);
}
