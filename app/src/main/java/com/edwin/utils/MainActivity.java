package com.edwin.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.edwin.utils.shapeimageview.MultiShapeView;

public class MainActivity extends AppCompatActivity {

    private MultiShapeView photo1;
    private MultiShapeView photo2;
    private MultiShapeView photo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        photo1 = (MultiShapeView) findViewById(R.id.iv_circle_one);
        photo2 = (MultiShapeView) findViewById(R.id.iv_circle_two);
        photo3 = (MultiShapeView) findViewById(R.id.iv_round);

        photo1.setImageResource(R.drawable.photo_one);
        photo2.setImageResource(R.drawable.photo_two);
        photo3.setImageDrawable(getResources().getDrawable(R.drawable.photo_three));

    }


}
