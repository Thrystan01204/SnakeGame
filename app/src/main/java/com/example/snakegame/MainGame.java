package com.example.snakegame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 * Project Name
 */
public class MainGame extends AppCompatActivity {
    public static ImageView img_swipe;
    public static Dialog dialogScore;
    private GameView gv;
    public static TextView txt_score, txt_best_score, txt_dialog_score, txt_dialog_best_score;
    public static ArrayList<String> score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        score = new ArrayList<String>();
        try {
            chargerDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        setContentView(R.layout.activity_game);
        img_swipe = findViewById(R.id.img_swipe);
        gv = findViewById(R.id.gv);
        txt_score = findViewById(R.id.txt_score);
        txt_best_score = findViewById(R.id.txt_best_score);
        dialogScore();
    }

    // Charge l'historique des parties dans le stockage réservé à l'application
    public void chargerDonnees() throws IOException {
        File file = new File(getFilesDir(), "stats.txt");
        // Si il n'y a pas de sauvegarde on ne charge rien
        if(!file.exists()) return;

        // Lectures du flux de byte
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        // Conversion des bytes en un seul string
        String contents = new String(bytes);

        // Les stats sont stockées ligne par ligne, on découpe et on ajoute en mémoire
        String[] data = contents.split("\n");
        for (int i=0; i < data.length; i++){
            score.add(data[i]);
        }
    }

    private void dialogScore() {
        int bestScore = 0;
        SharedPreferences sp = this.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null){
            bestScore = sp.getInt("bestscore",0);
        }
        MainGame.txt_best_score.setText(bestScore+"");
        dialogScore = new Dialog(this);
        dialogScore.setContentView(R.layout.dialog_start);
        txt_dialog_score = dialogScore.findViewById(R.id.txt_dialog_score);
        txt_dialog_best_score = dialogScore.findViewById(R.id.txt_dialog_best_score);
        txt_dialog_best_score.setText(bestScore + "");
        dialogScore.setCanceledOnTouchOutside(false);
        RelativeLayout rl_start = dialogScore.findViewById(R.id.rl_start);
        rl_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_swipe.setVisibility(View.VISIBLE);
                gv.reset();
                dialogScore.dismiss();
            }
        });
        RelativeLayout rl_scores = dialogScore.findViewById(R.id.rl_score);
        rl_scores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMenu();
            }
        });
        dialogScore.show();
    }
    public void openMenu(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}