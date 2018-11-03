package com.example.eggii.rps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Class for first splash screen
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.img_main);
        imageView.setOnClickListener(MainActivity.this);
    }

    /**
     * Method for starting activity UserInfo
     * */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,UserInfo.class);
        startActivity(intent);
    }
}
