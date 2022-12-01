package com.example.pdapp2022919.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pdapp2022919.R;

import java.util.LinkedList;
import java.util.Random;

public class GameView extends View {

    private static Random random = new Random();

    private int viewWidth, viewHeight;
    private int size = 100, x = 0, y = 0, distance = 0;
    private Paint p = new Paint();
    private LinkedList<Gap> gaps = new LinkedList<>();

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void update() {
        distance++;
        y++;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();
        p.setStyle(Paint.Style.FILL);
        // draw background
        p.setColor(Color.WHITE);
//        canvas.drawRect(0, 0, 1000, 1000, p);
        p.setColor(Color.RED);
        canvas.drawRect(x, y, x + size, y + size, p);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed) return;
        viewWidth = right - left;
        viewHeight = bottom - top;
        x = viewWidth / 10;
        y = viewHeight / 2;
    }

    private class Gap {

        int x, length;

        public Gap() {
            x = distance + random.nextInt(viewWidth);
            length = random.nextInt(viewWidth / 2);
        }

    }
}
