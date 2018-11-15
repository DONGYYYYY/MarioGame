package com.project.my.finalproject;

import android.graphics.Bitmap;

public class Block {

    /*
        블록들에 해당하는 클래스
     */
    public int kind; // 블록 종류

    public Bitmap img; // 현재 블록 이미지
    public float x , y; // 블록의 위치
    public int w, h; // 블록의 너비와 높이의 반값들

    public boolean isDead;
    static public float newY;

    public Block(int kind , float bx , float by) // 생성자
    {
        this.kind  = kind;

        x = bx; // 중앙x값
        y = by; // 중앙 y값
        if(kind == 1) {
            img = CommonResources.blockimg; // 일반블록
        }
        if(kind == 2)
        {
            img = CommonResources.useblockimg; // 사용블록
        }
        if(kind == 3)
        {
            img = CommonResources.itemblockimg; // 아이템 블록
        }
        w = CommonResources.bw; // 이미지 너비 / 2
        h = CommonResources.bh; // 이미지 높이 / 2


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


        if ((((x - w) <= cx + CommonResources.cw[0]) && ((x + w) >= cx - CommonResources.cw[0])) && (cy + CommonResources.ch[0] < y)) { //위에 충돌
            result = 1;
            newY = y - h;
            Character.ground = newY;
        } else if ((((x - w) <= cx + CommonResources.cw[0]) && ((x + w) >= cx - CommonResources.cw[0])) && (cy - CommonResources.ch[0] > y)) { // 아래 충돌
            result = 2;
            newY = y + h;

            if (kind == 1) { // 일반 블록일 경우에는 파괴
                isDead = true;
            }
            if (kind == 3) { // 아이템 블록일 경우에는 사용블록으로 변경
                img = CommonResources.useblockimg;

            }
        }
        else if ((x - w) <= cx + CommonResources.cw[0] && (cx - CommonResources.cw[0] < (x - w))) // 좌측 충돌
        {
            result = 3;
        } else if ((x + w) <= cx - CommonResources.cw[0] && (x + w) < cx + CommonResources.cw[0]) // 우측 충돌
        {
            result = 4;
        }
        else {
                result = 5;
            }

            return result;
    }


    public boolean isblock(float cx , float  cy ,float cw , float ch) // 블록 존재 판정 (아래에)
    {
        boolean isblock = false;
        if((cy+ch) >= (y - h) &&(((x - w) <= cx+cw) && ((x + w) >= cx-cw))) // 자신의 아래에 있는 블록만 확인 (이 if문이 없으면 위에 블록이 있으면 있는 것으로 판정)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
