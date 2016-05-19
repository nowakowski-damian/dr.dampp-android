package com.thirteendollars.drdampp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Damian Nowakowski on 17.05.16.
 */
public class TwoJoysticksView extends View implements Runnable {



    public final static long DEFAULT_LOOP_INTERVAL = 200; // 100 ms


    private OnJoystickMoveListener onJoystickMoveListener;
    private Thread thread = new Thread(this);
    private long loopInterval = DEFAULT_LOOP_INTERVAL;

    // Speed joystick
    private float ySpeedPosition = 0;
    private float speedCenterX = 0;
    private float speedCenterY = 0;
    private Paint speedMainCircle;
    private Paint speedMainButton;
    private Paint speedVerticalLine;

    // Turn joystick
    private float xTurnPosition = 0;
    private float turnCenterX = 0;
    private float turnCenterY = 0;
    private Paint turnMainCircle;
    private Paint turnMainButton;
    private Paint turnHorizontalLine;

    // Common settings
    private int joysticksRadius;
    private int buttonsRadius;


    public TwoJoysticksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystickView();
    }

    public TwoJoysticksView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        initJoystickView();
    }

    protected void initJoystickView() {
        // speed joystick
        speedMainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        speedMainCircle.setColor(Color.WHITE);
        speedMainCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        speedVerticalLine = new Paint();
        speedVerticalLine.setStrokeWidth(15);
        speedVerticalLine.setColor(Color.BLACK);

        speedMainButton = new Paint(Paint.ANTI_ALIAS_FLAG);
        speedMainButton.setColor(Color.GRAY);
        speedMainButton.setStyle(Paint.Style.FILL_AND_STROKE);

        // turn joystick
        turnMainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        turnMainCircle.setColor(Color.WHITE);
        turnMainCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        turnHorizontalLine = new Paint();
        turnHorizontalLine.setStrokeWidth(15);
        turnHorizontalLine.setColor(Color.BLACK);

        turnMainButton = new Paint(Paint.ANTI_ALIAS_FLAG);
        turnMainButton.setColor(Color.GRAY);
        turnMainButton.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        // before measure, get the center of views
        ySpeedPosition = getHeight() / 2;
        xTurnPosition = 3*((getWidth()) / 4 );

        int d = Math.min(xNew, yNew);
        buttonsRadius = (int) (d / 2 * 0.20);
        joysticksRadius = (int) (d / 2 * 0.70);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // set size
        speedCenterX = (getWidth()) / 4;
        speedCenterY = (getHeight()) / 2;
        turnCenterX = 3*((getWidth()) / 4 );
        turnCenterY = (getHeight()) / 2;

        // paint main circles
        canvas.drawCircle(speedCenterX, speedCenterY, joysticksRadius, speedMainCircle);
        canvas.drawCircle(turnCenterX, turnCenterY, joysticksRadius, turnMainCircle);
        // paint lines
        canvas.drawLine(speedCenterX, (speedCenterY - joysticksRadius), speedCenterX,(speedCenterY + joysticksRadius), speedVerticalLine);
        canvas.drawLine((turnCenterX - joysticksRadius), turnCenterY, (turnCenterX + joysticksRadius), turnCenterY, turnHorizontalLine);
        // paint move buttons
        canvas.drawCircle(speedCenterX, ySpeedPosition, buttonsRadius, speedMainButton);
        canvas.drawCircle(xTurnPosition, turnCenterY, buttonsRadius, turnMainButton);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        invalidate();


        // if stop touching
        if (event.getAction() == MotionEvent.ACTION_UP) {

            ySpeedPosition = speedCenterY;
            xTurnPosition = turnCenterX;
            thread.interrupt();
            if (onJoystickMoveListener != null) {
                onJoystickMoveListener.onValueChanged(0, 0);
            }
            return true;
        }

        // if touched first time
        if (onJoystickMoveListener != null && event.getAction() == MotionEvent.ACTION_DOWN) {

            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
            //if touched in joystick range
            if( ( Math.abs( turnCenterX - event.getX() ) <= joysticksRadius &&
                    Math.abs( turnCenterY - event.getY() ) <= joysticksRadius ) ||
                    (Math.abs( speedCenterX - event.getX() ) <= joysticksRadius &&
                            Math.abs( speedCenterY - event.getY() ) <= joysticksRadius ) ) {
                thread = new Thread(this);
                thread.start();
            }

            if (onJoystickMoveListener != null ){
                sendCurrentControllerPosition();
            }
            return true;
        }


        // if only one finger
        if( event.getPointerCount()<=1 ){

            // if finger is on speed joystick
            if( Math.abs( speedCenterX - event.getX() ) <= joysticksRadius &&
                    Math.abs( speedCenterY - event.getY() ) <= joysticksRadius ) {

                ySpeedPosition= event.getY();
                xTurnPosition=turnCenterX;
            }

            // if finger is on turn joystick
            else if( Math.abs( turnCenterX - event.getX() ) <= joysticksRadius &&
                    Math.abs( turnCenterY - event.getY() ) <= joysticksRadius ) {

                xTurnPosition= event.getX();
                ySpeedPosition= speedCenterY;
            }
            else{
                xTurnPosition= turnCenterX;
                ySpeedPosition= speedCenterY;
            }

        }
        // if more than one finger
        else{

            for(int index=0; index<event.getPointerCount(); index++ ){

                // if finger is on speed joystick, set speed value
                if( Math.abs( speedCenterX - event.getX(index) ) <= joysticksRadius &&
                        Math.abs( speedCenterY - event.getY(index) ) <= joysticksRadius ) {

                    ySpeedPosition= event.getY(index);
                }
                // else if finger is on turn joystick, set turn value;
                else if( Math.abs( turnCenterX - event.getX(index) ) <= joysticksRadius &&
                        Math.abs( turnCenterY - event.getY(index) ) <= joysticksRadius ) {

                    xTurnPosition= event.getX(index);
                }
            }
        }



        return true;
    }


    private void sendCurrentControllerPosition() {
        int speedValue = (int) ( ( (ySpeedPosition-speedCenterY)/joysticksRadius ) *100);
        int turnValue = (int) ( ( (xTurnPosition-turnCenterX)/joysticksRadius ) *100);
        onJoystickMoveListener.onValueChanged( -speedValue,turnValue );
    }


    public void setOnJoystickMoveListener(OnJoystickMoveListener listener,long repeatInterval) {
        this.onJoystickMoveListener = listener;
        this.loopInterval = repeatInterval;
    }

    public interface OnJoystickMoveListener {
        void onValueChanged(int speed, int turn);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            post(new Runnable() {
                public void run() {

                    if (onJoystickMoveListener != null){
                        sendCurrentControllerPosition();
                    }
                }
            });
            try {
                Thread.sleep(loopInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
