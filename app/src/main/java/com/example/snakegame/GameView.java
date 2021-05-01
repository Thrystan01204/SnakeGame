
package com.example.snakegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {
    private Bitmap bmGrass1, bmGrass2, bmSnake1, bmApple, bmStop;
    private ArrayList<Grass> arrGrass = new ArrayList<>();
    private int w = 12, h=21;
    public static int sizeElementMap = 75*Constants.SCREEN_WIDTH/1080;
    private Snake snake;
    private Object apple;
    private Object stop;
    private Handler handler;
    private Runnable r;
    private boolean move = false;
    private float mx, my;
    public static boolean isPlaying = false;
    public static int score = 0, bestScore = 0;
    private Context context;
    private int soundEat, soundDie;
    private float volume;
    private boolean loadedsound;
    private SoundPool soundPool;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null){
            bestScore = sp.getInt("bestscore",0);
        }
        bmGrass1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.texture_grass_1);
        bmGrass1 = Bitmap.createScaledBitmap(bmGrass1, sizeElementMap, sizeElementMap, true);
        bmGrass2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.texture_grass_2);
        bmGrass2 = Bitmap.createScaledBitmap(bmGrass2, sizeElementMap, sizeElementMap, true);
        bmSnake1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake1);
        bmSnake1 = Bitmap.createScaledBitmap(bmSnake1, 14*sizeElementMap, sizeElementMap, true);
        bmApple = BitmapFactory.decodeResource(this.getResources(), R.drawable.ze_pom);
        bmApple = Bitmap.createScaledBitmap(bmApple, sizeElementMap, sizeElementMap, true);
        bmStop = BitmapFactory.decodeResource(this.getResources(), R.drawable.stop);
        bmStop = Bitmap.createScaledBitmap(bmStop, sizeElementMap, sizeElementMap, true);

        for(int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                if((j+i)%2==0){
                    arrGrass.add(new Grass(bmGrass1, j*bmGrass1.getWidth() + Constants.SCREEN_WIDTH/2 - (w/2)*bmGrass1.getWidth(), i*bmGrass1.getHeight()+50*Constants.SCREEN_HEIGHT/1920, bmGrass1.getWidth(), bmGrass1.getHeight()));
                }else{
                    arrGrass.add(new Grass(bmGrass2, j*bmGrass2.getWidth() + Constants.SCREEN_WIDTH/2 - (w/2)*bmGrass2.getWidth(), i*bmGrass2.getHeight()+50*Constants.SCREEN_HEIGHT/1920, bmGrass2.getWidth(), bmGrass2.getHeight()));
                }
            }
        }
        snake = new Snake(bmSnake1,arrGrass.get(126).getX(),arrGrass.get(126).getY(), 4);
        apple = new Object(bmApple, arrGrass.get(objectPlacementRandom()[0]).getX(), arrGrass.get(objectPlacementRandom()[1]).getY());
        stop = new Object(bmStop, arrGrass.get(objectPlacementRandom()[0]).getX(), arrGrass.get(objectPlacementRandom()[1]).getY());
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        if(Build.VERSION.SDK_INT>=21){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        }else{
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedsound = true;
            }
        });
        soundEat = this.soundPool.load(context, R.raw.eating_voice, 1);
        soundDie = this.soundPool.load(context, R.raw.dying_voice, 1);
    }

    private int[] objectPlacementRandom(){
        int []xy = new int[2];
        Random r = new Random();
        xy[0] = r.nextInt(arrGrass.size()-1);
        xy[1] = r.nextInt(arrGrass.size()-1);
        Rect rect = new Rect(arrGrass.get(xy[0]).getX(), arrGrass.get(xy[1]).getY(), arrGrass.get(xy[0]).getX()+sizeElementMap, arrGrass.get(xy[1]).getY()+sizeElementMap);
        boolean check = true;
        while (check){
            check = false;
            for (int i = 0; i < snake.getArrPartSnake().size(); i++){
                if(rect.intersect(snake.getArrPartSnake().get(i).getrBody())){
                    check = true;
                    xy[0] = r.nextInt(arrGrass.size()-1);
                    xy[1] = r.nextInt(arrGrass.size()-1);
                    rect = new Rect(arrGrass.get(xy[0]).getX(), arrGrass.get(xy[1]).getY(), arrGrass.get(xy[0]).getX()+sizeElementMap, arrGrass.get(xy[1]).getY()+sizeElementMap);
                }
            }
        }
        return xy;
    }
    //We detectation the moment when 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int a = event.getActionMasked();
        switch (a){
            case  MotionEvent.ACTION_MOVE:{
                if(!move){
                    mx = event.getX();
                    my = event.getY();
                    move = true;
                }else{
                    if(mx - event.getX() > 100 && !snake.isMove_right()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_left(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }else if(event.getX() - mx > 100 &&!snake.isMove_left()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_right(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }else if(event.getY() - my > 100 && !snake.isMove_up()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_down(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }else if(my - event.getY() > 100 && !snake.isMove_down()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_up(true);
                        isPlaying = true;
                        MainActivity.img_swipe.setVisibility(INVISIBLE);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                mx = 0;
                my = 0;
                move = false;
                break;
            }
        }
        return true;
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(0xFF065700);
        for(int i = 0; i < arrGrass.size(); i++){
            canvas.drawBitmap(arrGrass.get(i).getBm(), arrGrass.get(i).getX(), arrGrass.get(i).getY(), null);
        }
        // Si on joue :
        // On update le snake.
        // On vérifie que la tête n'est pas sortie de la zone de jeu.
        // Boucle for : On vérifie que la tête n'entre pas en contact avec un autre élément du snake.
        if(isPlaying){
            snake.update();
            if(snake.getArrPartSnake().get(0).getX() < this.arrGrass.get(0).getX()
                    ||snake.getArrPartSnake().get(0).getY() < this.arrGrass.get(0).getY()
                    ||snake.getArrPartSnake().get(0).getY()+sizeElementMap>this.arrGrass.get(this.arrGrass.size()-1).getY() + sizeElementMap
                    ||snake.getArrPartSnake().get(0).getX()+sizeElementMap>this.arrGrass.get(this.arrGrass.size()-1).getX() + sizeElementMap){
                gameOver();
            }
            for (int i = 1; i < snake.getArrPartSnake().size(); i++){
                if (snake.getArrPartSnake().get(0).getrBody().intersect(snake.getArrPartSnake().get(i).getrBody())){
                    gameOver();
                }
            }

        }
        snake.drawSnake(canvas);
        apple.draw(canvas);
        stop.draw(canvas);
        //When snake reach apple, load sound and add part + score
        if(snake.getArrPartSnake().get(0).getrBody().intersect(apple.getR())){
            if(loadedsound){
                int streamId = this.soundPool.play(this.soundEat, (float)0.5, (float)0.5, 1, 0, 1f);
            }
            apple.reset(arrGrass.get(objectPlacementRandom()[0]).getX(), arrGrass.get(objectPlacementRandom()[1]).getY());
            snake.addPart();
            score++;
            MainActivity.txt_score.setText(score+"");
            //Update du bestScore
            if(score > bestScore){
                bestScore = score;
                SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("bestscore", bestScore);
                editor.apply();
                MainActivity.txt_best_score.setText(bestScore+"");
            }
        }


        //Lorsque le serpent passe sur un stop, gameover
        if(snake.getArrPartSnake().get(0).getrBody().intersect(stop.getR())){
            //son de mort a ajouter
            if(loadedsound){
                int streamId = this.soundPool.play(this.soundDie, (float)0.5, (float)0.5, 1, 0, 1f);
            }
            gameOver();
        }

        handler.postDelayed(r, 100);
    }


    // On arrête le jeu when gameover reached and we load sound
    private void gameOver() {
        isPlaying = false;
        MainActivity.dialogScore.show();
        MainActivity.txt_dialog_best_score.setText(bestScore+"");
        MainActivity.txt_dialog_score.setText(score+"");
        if(loadedsound){
            int streamId = this.soundPool.play(this.soundDie, (float)0.5, (float)0.5, 1, 0, 1f);
        }
    }

    public void reset(){
        for(int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                if((j+i)%2==0){
                    arrGrass.add(new Grass(bmGrass1, j*bmGrass1.getWidth() + Constants.SCREEN_WIDTH/2 - (w/2)*bmGrass1.getWidth(), i*bmGrass1.getHeight()+50*Constants.SCREEN_HEIGHT/1920, bmGrass1.getWidth(), bmGrass1.getHeight()));
                }else{
                    arrGrass.add(new Grass(bmGrass2, j*bmGrass2.getWidth() + Constants.SCREEN_WIDTH/2 - (w/2)*bmGrass2.getWidth(), i*bmGrass2.getHeight()+50*Constants.SCREEN_HEIGHT/1920, bmGrass2.getWidth(), bmGrass2.getHeight()));
                }
            }
        }
        snake = new Snake(bmSnake1,arrGrass.get(126).getX(),arrGrass.get(126).getY(), 4);
        apple = new Object(bmApple, arrGrass.get(objectPlacementRandom()[0]).getX(), arrGrass.get(objectPlacementRandom()[1]).getY());
        score = 0;
    }


}
