package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedScroller extends Scroller {

    private int mDuration = 2000;

    public FixedScroller(Context context) {
        super(context);
    }

    public FixedScroller(Context context, Interpolator interpolator, int duration) {
        super(context, interpolator);
        this.mDuration = duration;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FixedScroller(Context context, Interpolator interpolator,
                         boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

}
