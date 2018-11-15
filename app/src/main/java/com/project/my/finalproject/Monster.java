package com.project.my.finalproject;

import android.graphics.Bitmap;

public class Monster {

    /*
        몬스터에 해당하는 클래스
    */
    public int kind; // 블록 종류

    public Bitmap img; // 현재 블록 이미지
    public float x , y; // 블록의 위치
    public int w, h; // 블록의 너비와 높이의 반값들

    public boolean isDead;

    public Monster(int kind , float bx , float by) // 생성자
    {
        this.kind  = kind;

        x = bx; // 중앙x값
        y = by; // 중앙 y값
        if(kind == 1) {
            img = CommonResources.monsterimg; // 몬스터
        }

        w = CommonResources.monsterw; // 이미지 너비 / 2
        h = CommonResources.monsterh; // 이미지 높이 / 2


        isDead = false;
    }


    public int getHit(float cx , float cy , float r) {
        int result = 0;
        //충돌 판정이 없을 경우
        if (!MathF.checkCollision(cx, cy, r, x, y, w, h)) {
            Character.ground = GameView.backH;
            return 0;

        }
        float d = r * 0.7f;


        if ((((x - w) <= cx + CommonResources.cw[0]) && ((x + w) >= cx - CommonResources.cw[0])) && (cy + CommonResources.ch[0] < y)) { // 몬스터 위에 충돌
            result = 1;
            isDead = true;
        } else  // 그 외의 경우
        {
            isDead = true;
            if(GameView.life == 2) // 목숨이 2일 경우에는 캐릭터 크기를 줄인다.
            {
                GameView.character.SizedownBitmap(GameView.context);
            }
            GameView.checkOver(); // 게임 오버인지 확인
            result = 2;
        }

        return result;
    }

}
