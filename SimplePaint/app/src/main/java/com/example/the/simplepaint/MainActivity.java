package com.example.the.simplepaint;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends Activity implements View.OnClickListener{

    // custom drawing view
    private CustomView drawView;

    // button
    private ImageButton btnNew;
    private ImageButton btnBrush;
    private ImageButton btnEraser;
    private ImageButton btnColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // make screen rotate
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // getView
        getView();
    }

    private void getView(){

        drawView = (CustomView)findViewById(R.id.customView);

        btnNew = (ImageButton)findViewById(R.id.btnNew);
        btnNew.setOnClickListener(this);

        btnBrush = (ImageButton)findViewById(R.id.btnBrush);
        btnBrush.setOnClickListener(this);

        btnEraser = (ImageButton)findViewById(R.id.btnEraser);
        btnEraser.setOnClickListener(this);

        btnColorPicker = (ImageButton)findViewById(R.id.btnColorPicker);
        btnColorPicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnNew.getId()){
            newDrawingView();
        } else if (view.getId() == btnBrush.getId()){
            drawView.setEraserMode(false);
        } else if (view.getId() == btnEraser.getId()){
            drawView.setEraserMode(true);
        } else if (view.getId() == btnColorPicker.getId()){
            openColorPickerDialog(false);
        }
    }

    private void newDrawingView(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Confirm: ");
        dialog.setContentView(R.layout.dialog_yes_no_cancel);

        // Get View
        Button btnYes = (Button)dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button)dialog.findViewById(R.id.btnNo);
        Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Save is not completed", Toast.LENGTH_LONG).show();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                drawView.startNewDrawingView();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // Show dialog
        dialog.show();
        dialog.getWindow().setLayout((5*width)/7, (3*height)/5);
    }

    private void openColorPickerDialog(boolean supportsAlpha){
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, drawView.getBrushColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                drawView.setBrushColor(color);
            }
        });
        dialog.show();
    }
}
