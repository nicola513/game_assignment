package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;

class CardView extends View {

    CardData data;
    static Drawable drawable;

    CardView(Context context, int style){
        //todo
        super(context);
        drawable = context.getResources().getDrawable(ImageData.coveStyle[style]);
    }

    CardView(Context context, CardData data) {
        super(context);
        this.data = data;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        Resources res = getResources();
        int bgColor = res.getColor(R.color.background_color, null);
        canvas.drawColor(bgColor);

        switch (data.state) {
            case Open:
                float marginRatio = res.getFraction(R.dimen.open_margin, 1, 1);
                int marginX = (int) (this.getWidth() * marginRatio);
                int marginY = (int) (this.getHeight() * marginRatio);
                data.drawable.setBounds(
                        0 + marginX, 0 + marginY,
                        this.getWidth() - marginX, this.getHeight() - marginY);
                data.drawable.draw(canvas);

                break;
            case Close:
                float ratio = res.getFraction(R.dimen.open_margin, 1, 1);
                int x = (int) (this.getWidth() * ratio);
                int y = (int) (this.getHeight() * ratio);
                drawable.setBounds(0 + x, 0 + y,
                        this.getWidth() - x, this.getHeight() - y);
                drawable.draw(canvas);

                break;
            case Clear:
                setVisibility(INVISIBLE);
                break;
        }
    }
}
