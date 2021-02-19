package com.daws.projects.codamation.utils.components;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecorationUtil extends RecyclerView.ItemDecoration {
    private int mItemOffset;
    private String type;

    public GridSpacingItemDecorationUtil(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    public GridSpacingItemDecorationUtil(float itemOffsetFloat, String type){
        this.mItemOffset = (int) itemOffsetFloat;
        this.type = type;
    }

    public GridSpacingItemDecorationUtil(int mItemOffset) {
        this.mItemOffset = mItemOffset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        if (type.equalsIgnoreCase("vertical"))
            outRect.set(0, 0, 0, mItemOffset);
        else if (type.equalsIgnoreCase("horizontal"))
            outRect.set(0,0,mItemOffset,0);
        else
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);

    }
}
