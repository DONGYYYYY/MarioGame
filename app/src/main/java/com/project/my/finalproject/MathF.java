package com.project.my.finalproject;

public class MathF {
    /*
        충돌 판정에 관련된 메소드를 모은 클래스
     */
    //사각형과 사각형의 충돌 판정
    static public boolean checkCollision
    (float x , float y , float w , float h , float tx,float ty , int tw , int th)
    {
        return (w + tw) < Math.abs(x - tx) && (h + th) <= Math.abs(y - ty);
    }
    //사각형과 원의 충돌
    static public boolean checkCollision(float x , float y , float r , float tx , float ty , float tw , float th)
    {
        return Math.abs(x - tx) <= (tw + r) && Math.abs(y - ty) <= (th + r);
    }

    //원과 원의 충돌
    static public boolean checkCollision(float x , float y , float r , float tx , float ty , float tr)
    {
        return (x - tx) * (x - tx) + ( y - ty) * (y - ty) <= (r + tr) * ( r + tr);
    }
}
