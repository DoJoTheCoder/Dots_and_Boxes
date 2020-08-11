package com.example.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotsandboxes.custviews.CanvasView;

public class MainActivity extends AppCompatActivity {

    Button gridbutton3x4;
    Button gridbutton4x5;
    RelativeLayout gridSizeChoice;
    TextView topTitleTextView;
    CanvasView canvasView;
    TextView redScoreView, blueScoreView;
    boolean isGridSize4x5;
    boolean isGameComplete;

    /*
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridbutton3x4 = findViewById(R.id.gridButton3x4);
        gridbutton4x5 = findViewById(R.id.gridButton4x5);
        gridSizeChoice = findViewById(R.id.gridSizeChoice);
        topTitleTextView = findViewById(R.id.topTitleTextView);
        canvasView = (CanvasView) findViewById(R.id.gameCanvas);
        redScoreView= findViewById(R.id.redScoreTextView);
        blueScoreView= findViewById(R.id.blueScoreTextView);
        isGameComplete=false;
    }

    public void setGridSize1(View view){
        canvasView.setGridDimension(false);
        isGridSize4x5=false;
        gridSizeChoice.setVisibility(View.INVISIBLE);
        topTitleTextView.setText("Blue's Turn");
        topTitleTextView.setBackgroundResource(R.color.teamTextBg);
        topTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.blueTeam));
    }

    public void setGridSize2(View view){
        canvasView.setGridDimension(true);
        isGridSize4x5=true;
        gridSizeChoice.setVisibility(View.INVISIBLE);
        topTitleTextView.setText("Blue's Turn");
        topTitleTextView.setBackgroundResource(R.color.teamTextBg);
        topTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.blueTeam));
    }

    public void resetGame(){
        canvasView.resetCanvas();

        topTitleTextView.setBackgroundResource(R.color.resetTitleBg);
        topTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.resetTitleText));
        topTitleTextView.setText("Dots And Boxes");

        gridSizeChoice.setVisibility(View.VISIBLE);
        redScoreView.setText(Integer.toString(0));
        blueScoreView.setText(Integer.toString(0));
        isGameComplete=false;
    }


    public void updateGameState(boolean isRedTurn, int redScore, int blueScore, boolean isGameContinue ){
        redScoreView.setText(Integer.toString(redScore));
        blueScoreView.setText(Integer.toString(blueScore));
        if(isRedTurn&& isGameContinue){
            topTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.redTeam));
            topTitleTextView.setText("Red's Turn");
        }else if(isGameContinue){
            topTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.blueTeam));
            topTitleTextView.setText("Blue's Turn");
        }else{
            if(redScore>blueScore){
                topTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.redTeam));
                topTitleTextView.setText("Red WINS!!!!");
            }else if(redScore<blueScore){
                topTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.blueTeam));
                topTitleTextView.setText("Blue WINS!!!!");
            }else if(redScore==blueScore){
                topTitleTextView.setTextColor(Color.BLACK);
                topTitleTextView.setText("Draw Match!");
            }
            Toast.makeText(MainActivity.this,"Tap the screen to play again!", Toast.LENGTH_LONG).show();
            isGameComplete=true;

        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        switch (eventAction) {
                case MotionEvent.ACTION_UP:
                //Toast.makeText(this, "ACTION_UP "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
                Log.i("main actvity motion", "onTouchEvent: started ");
                if(!isGameComplete)
                updateGameState(canvasView.isRedTurn(),canvasView.getScore(false),canvasView.getScore(true),canvasView.isGameContinue());
                else
                    resetGame();

        }
        return true;
    }
}

