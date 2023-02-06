package com.example.pdapp2022919.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pdapp2022919.R;

import java.util.Random;

public class GameView extends View {

    private static Random random = new Random();
    private int level_difficulty;
    private int viewWidth, viewHeight;
    private int ground, speed = 10, xSpeed = 10;
    private int size = 50, x = -size, y = -size, distance = 0, level = -1, gap = 0, failcount = 0;
    private double stander;
    private Paint p = new Paint();
    private int blockIndex = 0;
    private Block[] blocks = new Block[2];
    private boolean isGameOver = false;
    private Bitmap heart = createBitmap(R.drawable.heart, 55, 60);


    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void update(double volume) {
        if (isGameOver) {
            return;
        }
        volume = 20 * (Math.log10(Math.abs(volume)));
        Block nowBlock = blocks[blockIndex];
        // move ball
        if (volume > stander) {
            if (y < getLimit(volume)) {
                y += speed;
            } else y -= speed * 5;
        } else y += speed;
        if (collision()) {
            if (y < nowBlock.y) y = nowBlock.y - size;
            else {
                x = nowBlock.x - size;
                y += speed;
            }
        }
        if (viewWidth <= 0) return;
        // generate block
        if (nowBlock.x + nowBlock.length < viewWidth / 10) {
            if (blockIndex == 0) blockIndex = 1;
            else blockIndex = 0;
            blocks[blockIndex] = new Block(false);
            gap++;
        }
        // move block
        for (Block block : blocks) {
            if (block == null) continue;
            block.x -= xSpeed;
        }
        if (x < 0 || y > viewHeight) {
            isGameOver = true;
            failcount++;
        }
    }

    public void startGame() {
        if (!isGameOver) this.level++;
        gap = 0;
        isGameOver = false;
        x = viewWidth / 10;
        y = (int) getLimit(stander + 3 * level) - size - 10;
        blocks[blockIndex] = new Block(true);
    }
    public void setLevelDifficulty(int level_difficulty) {
        this.level_difficulty =  level_difficulty;
    }

    public void setStander(double avg) {
        stander = avg;
    }

    public int getLevel() {
        return level + 1;
    }

    public int getGap() {
        return gap;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int failCount() {
        return failcount;
    }

    private double getLimit(double volume) {
        return ground - (volume - stander) / (90 - stander) * ground;
    }

    private boolean collision() {
        Block nowBlock = blocks[blockIndex];
        if (nowBlock == null) return false;
        if (x + size < nowBlock.x) return false;
        if (x - size > nowBlock.x + nowBlock.length) return false;
        return y + size >= nowBlock.y;
    }

    public Bitmap createBitmap(int drawableId, int newHeight, int newWidth) {
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        myBitmap = Bitmap.createScaledBitmap(myBitmap, newWidth, newHeight, false);
        return myBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setStyle(Paint.Style.FILL);
        // draw block
        p.setShader(new LinearGradient(0, ground, 0, getHeight(), Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));
        for (Block block : blocks) {
            if (block == null) continue;
            canvas.drawRoundRect(block.x, block.y, block.x + block.length, viewHeight, size, size, p);
        }
        // draw ball
        p.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));
        canvas.drawCircle(x, y, size, p);

        float center = viewWidth * 0.5f - 30;
        for (int i = 0; i < 3 - failcount; i++) {
            canvas.drawBitmap(heart, center + 60 * (i - 1) + i * 10, viewHeight * 0.08f, null);
        }
        invalidate();
}

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed) return;
        viewWidth = right - left;
        viewHeight = bottom - top;
//        x = viewWidth / 10;
        ground = viewHeight / 3 * 2;
//        y = ground - size + 10;
//        blocks[blockIndex] = new Block(true);
    }

    private class Block {

        int x, y, length;

        public Block(boolean init) {
            if (init) {
                x = 0;
                y = (int) getLimit(stander + level_difficulty * level);
                length = viewWidth;
            }
            else {
                x = xSpeed * 60 * 3 + random.nextInt(xSpeed * 60 );
                y = (int) getLimit(stander + level_difficulty * level);
                length = xSpeed * 60 *2;
            }
        }

    }
}
