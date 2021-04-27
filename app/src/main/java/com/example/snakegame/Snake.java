package com.example.snakegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 * Project Name
 */
public class Snake {
    private Bitmap bm, bm_head_up, bm_head_down,_bm_head_left,bm_head_right,bm_body_vertical,bm_body_horizontal,bm_body_top_right,bm_body_top_left, bm_body_bottom_right,bm_body_bottom_left,
    bm_tail_left, bm_tail_up,bm_tail_down,bm_tail_right;

    private int x,y, length;
    private ArrayList<PartSnake> arrPartSnake = new ArrayList<>();

    public Snake(Bitmap bm, int x, int y, int length) {
        this.bm = bm;
        this.x = x;
        this.y = y;
        this.length = length;
        bm_body_bottom_left = Bitmap.createBitmap(bm,0,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_body_bottom_right = Bitmap.createBitmap(bm,GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_body_horizontal = Bitmap.createBitmap(bm,2*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_body_top_left = Bitmap.createBitmap(bm,3*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_body_top_right = Bitmap.createBitmap(bm,4*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_body_vertical = Bitmap.createBitmap(bm,5*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_head_down = Bitmap.createBitmap(bm,6*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        _bm_head_left = Bitmap.createBitmap(bm,7*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_head_right = Bitmap.createBitmap(bm,8*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_head_up = Bitmap.createBitmap(bm,9*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_tail_up = Bitmap.createBitmap(bm,10*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_tail_right = Bitmap.createBitmap(bm,11*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_tail_left = Bitmap.createBitmap(bm,12*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);
        bm_tail_down = Bitmap.createBitmap(bm,13*GameView.sizeOfMap,0,GameView.sizeOfMap,GameView.sizeOfMap);

        arrPartSnake.add(new PartSnake(bm_head_right,x, y));
        for (int i = 1; i < length-1; i++) {
            arrPartSnake.add(new PartSnake(bm_body_horizontal,arrPartSnake.get(i-1).getX()-GameView.sizeOfMap, y));
        }
        arrPartSnake.add(new PartSnake(bm_tail_right,arrPartSnake.get(length-2).getX()-GameView.sizeOfMap, y ));
    }

    public void draw(Canvas canvas){
        for (int i = 0; i < length ; i++) {
            canvas.drawBitmap(arrPartSnake.get(i).getBm(),arrPartSnake.get(i).getX(),arrPartSnake.get(i).getY(),null);
        }
    }
    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getBm_head_up() {
        return bm_head_up;
    }

    public void setBm_head_up(Bitmap bm_head_up) {
        this.bm_head_up = bm_head_up;
    }

    public Bitmap getBm_head_down() {
        return bm_head_down;
    }

    public void setBm_head_down(Bitmap bm_head_down) {
        this.bm_head_down = bm_head_down;
    }

    public Bitmap get_bm_head_left() {
        return _bm_head_left;
    }

    public void set_bm_head_left(Bitmap _bm_head_left) {
        this._bm_head_left = _bm_head_left;
    }

    public Bitmap getBm_head_right() {
        return bm_head_right;
    }

    public void setBm_head_right(Bitmap bm_head_right) {
        this.bm_head_right = bm_head_right;
    }

    public Bitmap getBm_body_vertical() {
        return bm_body_vertical;
    }

    public void setBm_body_vertical(Bitmap bm_body_vertical) {
        this.bm_body_vertical = bm_body_vertical;
    }

    public Bitmap getBm_body_horizontal() {
        return bm_body_horizontal;
    }

    public void setBm_body_horizontal(Bitmap bm_body_horizontal) {
        this.bm_body_horizontal = bm_body_horizontal;
    }

    public Bitmap getBm_body_top_right() {
        return bm_body_top_right;
    }

    public void setBm_body_top_right(Bitmap bm_body_top_right) {
        this.bm_body_top_right = bm_body_top_right;
    }

    public Bitmap getBm_body_top_left() {
        return bm_body_top_left;
    }

    public void setBm_body_top_left(Bitmap bm_body_top_left) {
        this.bm_body_top_left = bm_body_top_left;
    }

    public Bitmap getBm_body_bottom_right() {
        return bm_body_bottom_right;
    }

    public void setBm_body_bottom_right(Bitmap bm_body_bottom_right) {
        this.bm_body_bottom_right = bm_body_bottom_right;
    }

    public Bitmap getBm_body_bottom_left() {
        return bm_body_bottom_left;
    }

    public void setBm_body_bottom_left(Bitmap bm_body_bottom_left) {
        this.bm_body_bottom_left = bm_body_bottom_left;
    }

    public Bitmap getBm_tail_left() {
        return bm_tail_left;
    }

    public void setBm_tail_left(Bitmap bm_tail_left) {
        this.bm_tail_left = bm_tail_left;
    }

    public Bitmap getBm_tail_right() {
        return bm_tail_right;
    }

    public void setBm_tail_right(Bitmap bm_tail_right) {
        this.bm_tail_right = bm_tail_right;
    }

    public Bitmap getBm_tail_up() {
        return bm_tail_up;
    }

    public void setBm_tail_up(Bitmap bm_tail_up) {
        this.bm_tail_up = bm_tail_up;
    }

    public Bitmap getBm_tail_down() {
        return bm_tail_down;
    }

    public void setBm_tail_down(Bitmap bm_tail_down) {
        this.bm_tail_down = bm_tail_down;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<PartSnake> getArrPartSnake() {
        return arrPartSnake;
    }

    public void setArrPartSnake(ArrayList<PartSnake> arrPartSnake) {
        this.arrPartSnake = arrPartSnake;
    }
}
