package com.project.my.finalproject;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;

public class Button {
    /*
        버튼에 관련된 클래스
     */
    public Bitmap img; // 버튼
    public int x,y;

    private int ptrId = -1; // 터치 id
    private RectF rect; // 판정 범위

    public boolean isTouch; // 터치 판정

    public Button(Bitmap bitmap , Point pos) //버튼 터치 범위 설정
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        img = bitmap;

        rect = new RectF(pos.x , pos.y , pos.x + w , pos.y + h);
        x = pos.x;
        y = pos.y;
    }

    public void action(int id , boolean isDown, float x , float y) // 해당 버튼을 클릭했는지 확인
    {
        if(isDown && rect.contains(x,y))
        {
            isTouch = true;
            ptrId = id;
        }

        if(!isDown && id == ptrId)
        {
            isTouch = false;
        }
    }
}
