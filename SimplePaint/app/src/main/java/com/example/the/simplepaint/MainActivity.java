package com.example.the.simplepaint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    // custom drawing view
    private CustomView customView;

    // button
    private ImageButton btnNew;
    private ImageButton btnBrush;
    private ImageButton btnEraser;
    private ImageButton btnColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getView
    }
}
