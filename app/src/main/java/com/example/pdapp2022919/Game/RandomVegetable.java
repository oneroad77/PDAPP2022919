package com.example.pdapp2022919.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.pdapp2022919.R;

import java.util.Random;

public class RandomVegetable {

    private static volatile RandomVegetable instance;

    private final Random random = new Random();
    private final Vegetable[] vegetables;
    private final int[] weights;

    private int weightSum = 0;

    private RandomVegetable() {
        vegetables = new Vegetable[]{
//                new Vegetable(1, R.drawable.tomato),
                new Vegetable(1, R.drawable.cabbage)
        };
        weightSum = 0;
        weights = new int[vegetables.length];
        for (int i = 0; i < vegetables.length; i++) {
            weightSum += vegetables[i].weight;
            weights[i] = weightSum;
        }
    }

    // 多執行緒時，當物件需要被建立時才使用synchronized保證Singleton一定是單一的 ，增加程式校能
    public static RandomVegetable getInstance() {
        if (instance == null) {
            synchronized (RandomVegetable.class) {
                if (instance == null) {
                    instance = new RandomVegetable();
                }
            }
        }
        return instance;
    }

    public int getRandomVegetable() {
        int r = random.nextInt(weightSum);
        int index = 0;
        while (r > 0) r -= weights[index++];
        return vegetables[index].id;
    }

    private static class Vegetable {

        private final int weight;
        private final int id;

        private Vegetable(int weight, int id) {
            this.weight = weight;
            this.id = id;
        }
    }

}
