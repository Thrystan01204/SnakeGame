package com.example.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;

public class ListeBestScore extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_best_score);
        ListView listView = (ListView)findViewById(R.id.listeScore);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MainGame.score);

        listView.setAdapter(arrayAdapter);

        RelativeLayout rl_BackGame = findViewById(R.id.rl_backGame);
        rl_BackGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}