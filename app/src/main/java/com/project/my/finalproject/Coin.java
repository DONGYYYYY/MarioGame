package com.project.my.finalproject;

import android.graphics.Bitmap;

public class Coin {

    /*
        코인에 해당하는 클래스
     */
    private int kind;

    public Bitmap img;
    public float x , y;
    public int w, h;

    public boolean isDead;

    public Coin(int kind ,float cx , float cy) // 생성자
    {
        this.kind = kind;
        x = cx; // 중앙x값
        y = cy; // 중앙y값
        if(kind == 1) { // 동전 이미지
            img = CommonResources.coinimg;
        }
        if(kind == 2) // 버섯 이미지
        {
            img = CommonResources.itemimg;
        }
        w = CommonResources.coinw; // 이미지 너비 / 2
        h = CommonResources.coinh; // 이미지 높이 / 2
        isDead = false;
    }

    public int getHit(float cx , float cy , float r) // 코인 충돌 판정
    {
        //충돌 판정이 없을 경우
        if(!MathF.checkCollision(cx,cy,r,x,y,w,h))
        {
            return 0;
        }
        //판정이 있을 경우 점수 추가
        if(kind == 1) {
            GameView.score += 1000;
        }
        if(kind == 2)
        {
            GameView.score += 2000;
            GameView.character.SizeupBitmap(GameView.context);
        }
        GameView.setScore();
        isDead = true;
        return 1;
    }
}
