package com.example.pdapp2022919.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private int size = 80, x = -size, y = -size, distance = 0, gap = 0, failCount = 0;
    private int level = 0;
    private float center;
    private float lightHeight;
    private float lightSize = 125;
    private int xInit;
    private double stander;
    private Paint p = new Paint();
    private int blockIndex = 0;
    private Block[] blocks = new Block[2];
    private boolean isGameOver = false, onNextGap = true, inGap = false;
    private Bitmap heart = createBitmap(R.drawable.heart, 60, 55);
    private RandomVegetable randomVegetable = RandomVegetable.getInstance();
    private Bitmap vegetable;
    private Bitmap redlight = createBitmap(R.drawable.group_107,400,400);
    private Bitmap greenlight = createBitmap(R.drawable.group_118,400,400);
    private Rect vegSrc, lightSrc,  lightDest;
    private BitmapShader grassShader = new BitmapShader(
            createBitmap(R.drawable.grass, 498, 100),
            Shader.TileMode.MIRROR, Shader.TileMode.MIRROR
    );
    private BitmapShader groundShader = new BitmapShader(
            createBitmap(R.drawable.ground, 992, 452),
            Shader.TileMode.MIRROR, Shader.TileMode.MIRROR
    );
    private BitmapShader spikeShader = new BitmapShader(
            createBitmap(R.drawable.spike, 1082, 113),
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT
    );
    private Matrix matrix = new Matrix();

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
        if (isGameOver) return;
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
        else if (x < xInit){
            x += speed;
        }
        if (viewWidth <= 0) return;
        // jump over the gap
        if (onNextGap && nowBlock.x < 0) {
            gap++;
            onNextGap = false;
        }
        // generate block
        if (nowBlock.x + nowBlock.length < xInit) {
            if (blockIndex == 0) blockIndex = 1;
            else blockIndex = 0;
            blocks[blockIndex] = new Block(false);
            onNextGap = true;
        }
        // move block
        for (Block block : blocks) {
            if (block == null) continue;
            block.x -= xSpeed;
        }
        if (x + size < 0 || y > viewHeight) {
            isGameOver = true;
            failCount++;
        }
        inGap = x < nowBlock.x;
        distance += speed;
    }

    public void startGame() {
        if (!isGameOver) {
//            this.level++;
            failCount = 0;
        }
        vegetable = createBitmap(randomVegetable.getRandomVegetable(), 500, 500);
        vegSrc = new Rect(0, 0, vegetable.getWidth() - 1, vegetable.getHeight() - 1);
        lightSrc = new Rect(0, 0, redlight.getWidth() - 1, redlight.getHeight() - 1);
        gap = 0;
        isGameOver = false;
        x = xInit;
        y = (int) getLimit(stander + 3) - size - 10;
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
        return failCount;
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

    public Bitmap createBitmap(int drawableId, int newWidth, int newHeight) {
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        myBitmap = Bitmap.createScaledBitmap(myBitmap, newWidth, newHeight, false);
        return myBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setStyle(Paint.Style.FILL);
        //draw hintLight
        if (inGap) canvas.drawBitmap(greenlight, lightSrc, lightDest,null);
        else canvas.drawBitmap(redlight, lightSrc, lightDest,null);

        // draw ball
        RectF dest = new RectF(x - size, y - size, x + size, y + size);
        if (vegetable != null) canvas.drawBitmap(vegetable, vegSrc, dest, null);
        // draw spike
        float spikeX = 0;
        float spikeY = viewHeight * 0.95f;
        p.setShader(spikeShader);
        matrix.setTranslate(spikeX - distance, spikeY - 1);
        spikeShader.setLocalMatrix(matrix);
        canvas.drawRect(spikeX, spikeY, viewWidth, viewHeight, p);
        // draw block
        for (Block block : blocks) {
            if (block == null) continue;
            matrix.setTranslate(block.x, block.y + 100);
            groundShader.setLocalMatrix(matrix);
            p.setShader(groundShader);
            canvas.drawRect(block.x, block.y + 50, block.x + block.length, viewHeight + 100, p);
            matrix.setTranslate(block.x, block.y - 1);
            grassShader.setLocalMatrix(matrix);
            p.setShader(grassShader);
            canvas.drawRoundRect(block.x, block.y, block.x + block.length, block.y + 100, size, size, p);

        }
        for (int i = 0; i < 3 - failCount; i++) {
            canvas.drawBitmap(heart, center - 30 + 60 * (i - 1) + i * 10, viewHeight * 0.08f, null);
        }
        invalidate();
}

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed) return;
        viewWidth = right - left;
        viewHeight = bottom - top;
        xInit = viewWidth / 10;
        center = viewWidth * 0.5f;
        lightHeight = viewHeight * 0.13f;
        ground = viewHeight / 3 * 2;
        lightDest = new Rect(
                (int) (center - lightSize),
                (int) (lightHeight),
                (int) (center + lightSize),
                (int) (lightHeight + lightSize * 2)
        );
    }

    private class Block {

        int x, y, length;

        public Block(boolean init) {
            if (init) {
                x = 0;
                y = (int) getLimit(stander + level_difficulty);
                length = xSpeed * 60 * 3;
            }
            else {
                x = xSpeed * 60 * 3 + random.nextInt(xSpeed * 60);
                y = (int) getLimit(stander + level_difficulty);
                length = xSpeed * 60 * 6;
            }
        }

    }
}
