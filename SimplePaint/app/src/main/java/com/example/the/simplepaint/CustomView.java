package com.example.the.simplepaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by THE on 2/26/2017.
 */

public class CustomView extends View {

    private Path drawPath;

    //
    private Paint drawPaint;

    // Paint object holds the style and color information
    private Paint canvasPaint;

    // initial color
    private int brushColor = 0XFF660000;

    private Canvas drawCanvas;

    private Bitmap canvasBitmap;

    // brush size
    private float brushSize = 40;
    private float eraseSize = 40;

    // Is eraser mode ?
    private boolean isEraserMode = false;

    private void init(){

        drawPath = new Path();

        drawPaint = new Paint();
        drawPaint.setColor(brushColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(drawPaint.DITHER_FLAG);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // each time the user draws using touch interaction,
        // invalidate the View, causing the onDraw method to execute.
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    // this method is called during layout
    // when the size of this view has changed
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // view given size
        super.onSizeChanged(w, h, oldw, oldh);

        // create Bitmap of certain w,h
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        // apply bitmap to graphic to start drawing
        drawCanvas = new Canvas(canvasBitmap);
    }

    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();

        // respond to down, move and up events
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.moveTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        // redraw
        invalidate();
        return true;
    }

    // Start new
    public void startNewDrawingView(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    // Enable eraser mode
    public void setEraserMode(boolean b){
        if (b == true){ // eraser
            // set paint to erase
            drawPaint.setStrokeWidth(eraseSize);
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint.setStrokeWidth(brushSize);
            drawPaint.setXfermode(null);
        }
    }

    // Get brush's color
    public int getBrushColor(){
        return drawPaint.getColor();
    }

    // Set brush's color
    public void setBrushColor(int newColor){
        brushColor = newColor;
        drawPaint.setColor(brushColor);
    }
}
