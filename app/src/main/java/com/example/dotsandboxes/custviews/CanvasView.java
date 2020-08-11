package com.example.dotsandboxes.custviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.*;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.dotsandboxes.R;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {

    float circleX, circleY, circleRadius,circleXIncrement,circleYIncrement;
    private Paint circlePaint, linePaint, boxPaint;
    int gridDimensionX =3, gridDimensionY=4;
    float startpointX, startpointY,endpointX,endpointY;
    boolean isDownOnDot;
    int redScore, blueScore;
    List <Integer> startPointList = new ArrayList();
    List <Integer> endPointList = new ArrayList();
    List <Integer> boxList = new ArrayList();
    List <Integer> turnList = new ArrayList();//////////////////////////////////////////////////////////////////////////////////////// turn list fix plz, use this for turns
    Rect box =new Rect();
    boolean gameContinue=true;


    public CanvasView(Context context) {
        super(context);
        init(null);
    }

    public CanvasView( Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init( attrs);
    }

    public CanvasView( Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( attrs);
    }

    public CanvasView( Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init( attrs);
    }

    private void init(@Nullable AttributeSet set) {
        circlePaint= new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#8a8a8a"));
        linePaint = new Paint();
        boxPaint = new Paint();
        linePaint.setStrokeWidth(35f);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        boxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        turnList.add(0);
        redScore=0;
        blueScore=0;
    }

    public void setGridDimension(boolean gridDimension){
        if(gridDimension){
            gridDimensionX = 4;
            gridDimensionY = 5;
            Log.i("gridDimension Start", "setGridDimension:");
            // set new values for
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#00F652"));
        circleRadius= (getWidth()/25f)+10f;
        circleX = circleRadius+ 100f;
        circleXIncrement=(getWidth()-200f-2*circleRadius)/(gridDimensionX-1);
        circleYIncrement=(getHeight()-200f-2*circleRadius)/(gridDimensionY-1);

        Log.i("Box array:", boxList.toString());

        for(int i=0;i<boxList.size();i++){  // info: this code draws the boxes in the correct color
            if((boxList.get(i)%10)==1)
                boxPaint.setColor(0xFFA80000);
            else
                boxPaint.setColor(0xFF2E1998);
            int topLeftCorner=boxList.get(i)/10;
            box.top= (int) (100+circleRadius+circleYIncrement*(topLeftCorner%10));
            box.left= (int) (100+circleRadius+circleXIncrement*(topLeftCorner/10));
            box.bottom= (int) (box.top+circleYIncrement);
            box.right= (int) (box.left+circleXIncrement);
            canvas.drawRect(box,boxPaint);
        }


        for(int i=0; i<startPointList.size();i++){  // info: this code draws the lines in the correct color
            if(turnList.get(i)==0)
                linePaint.setColor(0xFF4123D8);
            else
                linePaint.setColor(0xFFE10000);
            float sX, sY, eX, eY;
            sX = 100 + circleRadius + (startPointList.get(i) / 10) * circleXIncrement;
            eX = 100 + circleRadius + (endPointList.get(i) / 10) * circleXIncrement;
            sY = 100 + circleRadius + (startPointList.get(i) % 10) * circleYIncrement;
            eY = 100 + circleRadius + (endPointList.get(i) % 10) * circleYIncrement;
            canvas.drawLine(sX, sY, eX, eY, linePaint);
        }


        for (int i=0;i<gridDimensionX; i++){ // loop that draws circles/ dots
            circleY = circleRadius+100f;
            for(int j=0; j<gridDimensionY; j++){
                canvas.drawCircle(circleX,circleY,circleRadius,circlePaint);
                circleY+=circleYIncrement;
            }
            circleX+=circleXIncrement;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        isDownOnDot=false;

        if(!gameContinue) {
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :{
                circleX = circleRadius+ 100f;
                float x= event.getX();
                float y= event.getY();
                double dx;
                double dy;
                startpointX=0;
                startpointY=0;

                for (int i=0;i<gridDimensionX; i++){

                    dx = Math.pow(x - circleX, 2);
                    circleY = circleRadius+100f;
                    for (int j=0;j<gridDimensionY; j++) {

                        dy = Math.pow(y - circleY, 2);

                        if (dx + dy < Math.pow(circleRadius, 2)) {
                            isDownOnDot = true;
                            startpointX=i;
                            startpointY=j;
                            break;
                        }
                        circleY += circleYIncrement;
                    }

                    if (isDownOnDot) {
                        break;
                    }
                    circleX += circleXIncrement;

                }
                return true;

            }

            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:{
                circleX = circleRadius+ 100f;
                float x= event.getX();
                float y= event.getY();
                double dx;
                double dy;
                endpointY=0;
                endpointX=0;


                for (int i = 0; i < gridDimensionX; i++) {

                    dx = Math.pow(x - circleX, 2);
                    circleY = circleRadius + 100f;
                    for (int j = 0; j < gridDimensionY; j++) {

                        dy = Math.pow(y - circleY, 2);

                        if (dx + dy < Math.pow(circleRadius, 2)) {
                            endpointX=i;
                            endpointY=j;
                            lineCheck(startpointX,startpointY,endpointX,endpointY);
                            break;
                        }

                        circleY += circleYIncrement;

                    }
                    if (isDownOnDot) break;
                    circleX += circleXIncrement;

                }


                isDownOnDot=false;
                postInvalidate();
                return false;
            }
        }
        return value;
    }
    private void lineCheck(float sX, float sY, float eX,float eY){
        boolean lineCheckFlag=true;
        if((10*sX + sY) > (10*eX + eY)){
            float t;
            t=sX;
            sX=eX;
            eX=t;
            t=sY;
            sY=eY;
            eY=t;
        }
        for(int i=0;i<startPointList.size()-1;i++){
            if( (int)(10*sX+sY)== startPointList.get(i) && (int)(10 * eX + eY)==endPointList.get(i) ){
                lineCheckFlag=false;
                break;
            }
        }
        if(lineCheckFlag) {
            if ((sX == eX && ((sY - eY) * (sY - eY) == 1))  || (sY == eY && ((sX - eX) * (sX - eX) == 1))    ) {
                startPointList.add((int) (10 * sX + sY));
                endPointList.add((int) (10 * eX + eY));
                boxDetector((int) (10 * sX + sY),(int) (10 * eX + eY));
            }
            Log.i("start array list", startPointList.toString());
            Log.i("end array list", endPointList.toString());

        }
    }
    private void boxDetector(int sp, int ep){
        Log.i("box", "boxDetector started");
        int arrSize=startPointList.size();
        boolean isOneNewBox1=false;
        boolean isOneNewBox2=false;
        int Const=10;
        if(ep-sp == 10) Const=1;
        if(arrSize>3){

            for(int i=0;i<arrSize-1;i++){
                if(sp==startPointList.get(i)){
                    for(int j=0;j<arrSize-1;j++){
                        if(ep==startPointList.get(j) && endPointList.get(j)==(sp+11)){
                            for(int k=0;k<arrSize-1;k++) {
                                if ((sp + 11) == endPointList.get(k) && (sp + Const) == startPointList.get(k)) {
                                    // a box is found, check if tis new box and accordingly add box to arr
                                    isOneNewBox1 = isNewBox(sp);

                                }

                            }
                        }
                    }
                    break;
                }
            }



            for(int i=0;i<arrSize-1;i++){
                Log.d("rep2", "loop one :ep="+ ep+" endpoint:"+ endPointList.get(i));
                if(ep==endPointList.get(i)){
                    for(int j=0;j<arrSize-1;j++){
                        if(sp==endPointList.get(j) && startPointList.get(j)==(ep-11)){
                            for(int k=0;k<arrSize-1;k++) {
                                if ((ep - 11) == startPointList.get(k) && (ep - Const) == endPointList.get(k)) {
                                    // a box is found, check if tis new box and accordingly add box to arr
                                    isOneNewBox2 = isNewBox(ep - 11);
                                }
                            }
                        }
                    }
                    break;
                }
            }

        }
        if(isOneNewBox1||isOneNewBox2){
            turnList.add(turnList.get(turnList.size()-1));
        }else {
            turnList.add(((turnList.get(turnList.size()-1))+1)%2);
        }
        Log.i("turn no", "Turn arr "+turnList.toString());
    }
    private boolean isNewBox(int boxNo){

        boolean isNewBoxNo=true;
        for(int i=0; i< boxList.size()-1;i++){
            if(boxNo==(boxList.get(i)/10)){
                isNewBoxNo=false;
                break;
            }
        }
        if(isNewBoxNo){
            boxList.add(10*boxNo+turnList.get(turnList.size()-1));
            if(boxList.size()==(gridDimensionX-1)*(gridDimensionY-1))
                gameContinue=false;
            if(turnList.get(turnList.size()-1)==0)                     // score and 'gameContinue'  updated
                blueScore++;
            else
                redScore++;
        }
        Log.i("box no", "Box arr "+boxList.toString());
        return isNewBoxNo;
    }
    public int getScore(boolean isBlueScoreWanted){
        if(isBlueScoreWanted)
            return blueScore;
        else
            return redScore;
    }
    public boolean isRedTurn(){ //false for blue, true for red
        if(turnList.get(turnList.size()-1)==0)
            return false;
        else
            return true;
    }
    public boolean isGameContinue(){
        return gameContinue;
    }
    public void resetCanvas(){
        gridDimensionX =3;
        gridDimensionY=4;
        redScore=0;
        blueScore=0;
        startPointList.clear();
        endPointList.clear();
        boxList.clear();
        turnList.clear();
        turnList.add(0);
        gameContinue=true;

    }
}
