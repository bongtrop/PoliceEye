package xyz.pongsakorn.policeeye.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Porpeeranut on 29/2/2559.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public GridSpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position % 2 == 0) {
            //  item left
            outRect.left = spacing;
            outRect.right = spacing / 4;
        } else {
            //  item right
            outRect.left = spacing / 4;
            outRect.right = spacing;
        }
        if (position == 0 || position == 1) {
            outRect.top = spacing;
        } else {
            outRect.top = spacing / 2;
        }
        outRect.bottom = spacing / 2;
    }
}