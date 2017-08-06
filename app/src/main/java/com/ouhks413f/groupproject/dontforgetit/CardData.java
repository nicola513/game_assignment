package com.ouhks413f.groupproject.dontforgetit;

/**
 * Created by ngnicola on 15/11/2016.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;

class CardData {
    static enum State { Open, Close, Clear ,Disappear};

    Drawable drawable;
    ImageData imageData;

    int imageNo;

    Context context;

    State state = State.Disappear;

    public CardData( Context context,int imageNo) {
        this.context = context;
        this.imageNo = imageNo;
        drawable = context.getResources().getDrawable(imageData.imageList.get(imageNo));
    }

    public CardData(CardData other) {
        this.imageNo = other.imageNo;
        drawable = other.context.getResources().getDrawable(imageData.imageList.get(imageNo));

    }

    @Override
    public boolean equals(Object o) {
        CardData other = (CardData) o;
        return imageNo==other.imageNo && state == other.state;
    }

}
