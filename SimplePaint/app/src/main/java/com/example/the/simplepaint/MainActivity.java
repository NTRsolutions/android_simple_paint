package com.example.the.simplepaint;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends Activity implements View.OnClickListener{

    // custom drawing view
    private CustomView drawView;

    // button
    private ImageButton btnNew;
    private ImageButton btnBrush;
    private ImageButton btnEraser;
    private ImageButton btnSizeChooser;
    private ImageButton btnColorPicker;
    private ImageButton btnOpenGallery;
    private ImageButton btnSave;

    // number id
    private static int GALLERY_IMAGE = 123;

    private ContentResolver con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        con = getApplicationContext().getContentResolver();

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

        btnSizeChooser = (ImageButton)findViewById(R.id.btnSizeChooser);
        btnSizeChooser.setOnClickListener(this);

        btnColorPicker = (ImageButton)findViewById(R.id.btnColorPicker);
        btnColorPicker.setOnClickListener(this);

        btnSave = (ImageButton)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnOpenGallery = (ImageButton)findViewById(R.id.btnOpenGallery);
        btnOpenGallery.setOnClickListener(this);
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
        } else if (view.getId() == btnSizeChooser.getId()){
            openSizeChooserDialog();
        } else if (view == btnOpenGallery){
            openGallery();
        } else if (view == btnSave){
            saveImage();
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

    private void openSizeChooserDialog() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Size: ");
        dialog.setContentView(R.layout.size_chooser);

        // Get View
        SeekBar sbBrushSize = (SeekBar)dialog.findViewById(R.id.seekBarBrushSize);
        final SeekBar sbEraserSize = (SeekBar)dialog.findViewById(R.id.seekBarEraserSize);
        Button btnOK = (Button)dialog.findViewById(R.id.btnOKSizeChooser);

        sbBrushSize.setMax(180);
        sbBrushSize.setProgress((int)drawView.getBrushSize());
        sbBrushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                drawView.setBrushSize(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbEraserSize.setMax(180);
        sbEraserSize.setProgress((int)drawView.getEraserSize());
        sbEraserSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                drawView.setEraserSize(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // Show size dialog
        dialog.show();
        dialog.getWindow().setLayout((5 * width)/7, (3 * height)/5); // set dimension for custom dialog
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File"), GALLERY_IMAGE);
    }

    private void saveImage(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Confirm:");
        dialog.setContentView(R.layout.dialog_yes_no);

        // Get view
        Button btnYes = (Button)dialog.findViewById(R.id.btnYes2);
        Button btnNo = (Button)dialog.findViewById(R.id.btnNo2);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setDrawingCacheEnabled(true);
                String name = UUID.randomUUID().toString();

                String imgSaved = MediaStore.Images.Media.insertImage(
                        con, drawView.getDrawingCache(),
                        name + ".png", "drawing");

                drawView.destroyDrawingCache();
                Toast.makeText(getApplicationContext(), "Image name:" + imgSaved, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // Show dialog
        dialog.show();
        dialog.getWindow().setLayout((5 * width)/7, (3 * height)/5); // set dimension for custom dialog
    }
    /*
    public void saveBitmap(Bitmap bmp) {
        String _time = "";
        Calendar cal = Calendar.getInstance();
        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour_of_day = cal.get(Calendar.HOUR_OF_DAY);
        _time = "image_" + hour_of_day + "" + minute + "" + second + ""
                + millisecond + ".png";
        String file_path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ "/Pictures";
        Log.e("error in saving image", file_path.toString());
        try {
            File dir = new File(file_path);
            File file = new File(dir, _time);
            FileOutputStream fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(getApplicationContext(),
                    "Image has been saved in KidsPainting folder",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("error in saving image", e.getMessage());
        }
    }
    */
    // Get result from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_IMAGE)
            {
                setImageFromGallery(data);
            }
        }
    }

    private void setImageFromGallery(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        drawView.setCanvasBitmap(bm);
    }

}
