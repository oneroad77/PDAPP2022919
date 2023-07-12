package com.example.pdapp2022919.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.pdapp2022919.Game.GameObject.Block;
import com.example.pdapp2022919.Game.GameObject.GameGui;
import com.example.pdapp2022919.Game.GameObject.Player;
import com.example.pdapp2022919.R;

import java.util.Random;

public class GameView extends View {

    private static Random random = new Random();
    private int level_difficulty;
    private int viewWidth, viewHeight;
    private int ground, speed = 10, xSpeed = 10;
    private int size = 80, distance = 0, blockCount = 0, failCount = 0;
    private int level = 0;
    private int xInit;
    private double stander;
    private Paint p = new Paint();
    private boolean isGameOver = false;

    private final Block[] blocks = new Block[4];
    private Player player;
    private GameGui gui;

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
        Block nowBlock = blocks[blockCount];
        // move ball
        player.move(volume, getLimit(volume), nowBlock);
        if (viewWidth <= 0) return;
        // jump over the gap
        if (nowBlock.x + nowBlock.length < 0) {
            blockCount++;
        }
        else if (blockCount == blocks.length - 1 && nowBlock.x < 0) {
            blockCount++;
        }
        // move block
        for (Block block : blocks) {
            block.x -= xSpeed;
        }
        if (player.x + size < 0 || player.y > viewHeight) {
            isGameOver = true;
            failCount++;
            gui.setHeartCount(3 - failCount);
        }
        // switch light
        gui.switchLight(player.x < nowBlock.x);
        distance += speed;
    }

    public void startGame() {
        player = new Player(this, xInit, (int) getLimit(stander + 3) - size - 10, stander);
        blockCount = 0;
        isGameOver = false;
        int x = 0;
        int y = (int) getLimit(stander + level_difficulty);
        // generate block
        for (int i = 0; i < blocks.length; i++) {
            int blockLength = i == 0 ? xSpeed * 60 * 3 : xSpeed * 60 * 6;
            int gapLength = xSpeed * 60 * 3 + random.nextInt(xSpeed * 60);
            blocks[i] = new Block(this, x, y, blockLength);
            x += gapLength + blockLength;
        }
    }

    public void setLevelDifficulty(int level_difficulty) {
        this.level_difficulty = level_difficulty;
    }

    public void setStander(double avg) {
        stander = avg;
    }

    public int getLevel() {
        return level + 1;
    }

    public int getGap() {
        return blockCount - 1;
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

    public Bitmap createBitmap(int drawableId, int newWidth, int newHeight) {
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        myBitmap = Bitmap.createScaledBitmap(myBitmap, newWidth, newHeight, false);
        return myBitmap;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setStyle(Paint.Style.FILL);
        // draw ball
        if (player != null) player.onDraw(canvas);
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
            block.onDraw(canvas);
        }
        // draw hintLight and heart
        gui.onDraw(canvas);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed) return;
        viewWidth = right - left;
        viewHeight = bottom - top;
        xInit = viewWidth / 10;
        ground = viewHeight / 3 * 2;
        gui = new GameGui(this);
    }

}
