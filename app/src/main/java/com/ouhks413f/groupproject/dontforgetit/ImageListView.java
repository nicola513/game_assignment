package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;

public class ImageListView extends View {
    ImageData data;

    public ImageListView(Context context,ImageData data) {
        super(context);
        this.data = data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Resources res = getResources();
        int bgColor = res.getColor(R.color.list_color, null);
        canvas.drawColor(bgColor);

        float marginRatio = res.getFraction(R.dimen.open_margin, 1, 1);
        int marginX = (int) (this.getWidth() * marginRatio);
        int marginY = (int) (this.getHeight() * marginRatio);
        data.drawable.setBounds(
                0 + marginX, 0 + marginY,
                this.getWidth() - marginX, this.getHeight() - marginY);
        data.drawable.draw(canvas);
    }
}
