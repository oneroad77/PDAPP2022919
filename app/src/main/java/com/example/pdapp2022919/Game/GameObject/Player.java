package com.example.pdapp2022919.Game.GameObject;

import static com.example.pdapp2022919.Game.GameUtils.createBitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.pdapp2022919.Game.GameView;
import com.example.pdapp2022919.Game.RandomVegetable;

public class Player extends GameObject {

    private static final int BALL_RADIUS = 80;
    private static final int SPEED = 10;

    private final double stander;
    private final int originX;
    private final Bitmap vegetable;
    private final Rect vegSrc;
    private final Matrix matrix = new Matrix();
    private final RectF dest = new RectF(0, 0, BALL_RADIUS * 2, BALL_RADIUS * 2);

    public Player(GameView view, int px, int py, double stander) {
        super(view);
        this.x = px;
        this.y = py;
        this.stander = stander;
        this.originX = px;

        RandomVegetable randomVegetable = RandomVegetable.getInstance();
        vegetable = createBitmap(view, randomVegetable.getRandomVegetable(), 500, 500);
        vegSrc = new Rect(0, 0, vegetable.getWidth() - 1, vegetable.getHeight() - 1);
    }

    public void move(double volume, double limit, Block nearBlock) {
        if (volume > stander) {
            if (y < limit) {
                y += SPEED;
            } else y -= SPEED * 5;
        } else y += SPEED;
        if (collision(nearBlock)) {
            if (y < nearBlock.y) y = nearBlock.y - BALL_RADIUS;
            else {
                x = nearBlock.x - BALL_RADIUS;
                y += SPEED;
            }
        } else if (x < originX) {
            x += SPEED;
        }
    }

    private boolean collision(Block block) {
        if (block == null) return false;
        if (x + BALL_RADIUS < block.x) return false;
        if (x - BALL_RADIUS > block.x + block.length) return false;
        return y + BALL_RADIUS >= block.y;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        matrix.setTranslate(x - BALL_RADIUS, y - BALL_RADIUS);
        canvas.setMatrix(matrix);
        if (vegetable != null) canvas.drawBitmap(vegetable, vegSrc, dest, null);
        canvas.restore();
    }

}
