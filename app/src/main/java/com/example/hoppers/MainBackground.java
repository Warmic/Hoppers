package com.example.hoppers;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Peter on 19.04.2017.
 */

public class MainBackground extends View {
    Bitmap backgroundformain;
    boolean rescaled;
    public MainBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        backgroundformain = BitmapFactory.decodeResource(getResources(),R.drawable.startbackground);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint =  new Paint();
        if (!rescaled)backgroundformain = Bitmap.createScaledBitmap(backgroundformain,canvas.getWidth(),canvas.getHeight(),false);
        canvas.drawBitmap(backgroundformain,0,0,paint);
        rescaled=true;
        invalidate();
    }
}
