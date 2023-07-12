package com.example.pdapp2022919.Store;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Carousel;

import com.example.pdapp2022919.R;
import com.example.pdapp2022919.SystemManager.ScreenSetting;

public class Basket extends ScreenSetting {

    private ImageView[] images = new ImageView[3];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
//        images[0] = findViewById(R.id.imageView_1);
//        images[1] = findViewById(R.id.imageView_2);
//        images[2] = findViewById(R.id.imageView_3);
        Carousel carousel = findViewById(R.id.carousel);
        carousel.setAdapter(new Carousel.Adapter() {
            @Override
            public int count() {
                // need to return the number of items we have in the carousel
                return images.length;
            }

            @Override
            public void populate(View view, int index) {
                // need to implement this to populate the view at the given index
                images[index] = (ImageView) view;
            }

            @Override
            public void onNewItem(int index) {
                // called when an item is set
            }
        });
    }
}
