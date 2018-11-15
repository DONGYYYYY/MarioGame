package com.project.my.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.CpuUsageInfo;

public class Character {

    /*
        캐릭터 판정에 관련된 클래스
     */
    private int speedJump = 1300;
    private int gravity = 2000;
    static public int jumpCnt = 0;
    static public boolean isJump;
    private int speed = 3;
    private int animNum = 0; // 캐릭터 모양

    private boolean charactermoving = false; // 케릭 이미지 변경 유무

    private int imgCnt = 2;
    private Bitmap[] imgs = new Bitmap[imgCnt];

    public static float x , y;
    public PointF dir = new PointF(0,0);

    public boolean gotoRight = false;
    public boolean gotoLeft = false;

    static public float ground;

    public Bitmap img;
    public float w,h;

    private float scrW , scrH; // 배경 너비와 높이

    public Character(Context context , float width , float height) // 생성자
    {
        //캐릭 위치와 변수값 설정
        this.scrW = width;
        this.scrH = height;
        makeBitmap(context);
        ground = height*0.9f;
        x = CommonResources.cw[0] * 4 ;
        y = ground - h;

    }


    public void update() // 지속적인 업데이트를 통한 캐릭 모션
    {
        if(GameView.isDemo == true) // 게임 오버일 경우
        {
            x = x;
            y = y;
            return;
        }

        if(y+CommonResources.ch[0] >= scrH) // 캐릭터가 바닥(떨어졌을)일 경우
        {
            GameView.checkOver();
        }

        if(!isBlock()) // 캐릭 아래 블록 유무 판정
        { // 없을 시
            isJump = true; //점프중으로 간주
            jumpCnt = 1;
        }
        // 충돌 유무 판정
        if(checkMonster())return;
        if(checkBlock()) return;
        if(checkCoin()) return;

        if(!isJump) { // 점프도중이 아닐 경우 캐릭 이동
            if(charactermoving) // 점프가 아닐 경우에는 이미지 변화
            {
                if(img == imgs[0])
                {
                    img = imgs[1];
                }
                else
                {
                    img = imgs[0];
                }
            }
            if(gotoRight) // 오른쪽 이동
            {
                if(x+w >= GameView.backW - w) // 오른쪽 끝에 도달했을 경우
                {
                    GameView.stageNum++; // 스테이지 ++
                    x = CommonResources.cw[0];
                    GameView.makeStage(); // 다음 스테이지 생성
                    setMove( false ,   false ); // 다음 맵 이동 시 자동 이동 x
                }
                else { // 그 외의 경우에는 오른쪽 이동
                        x += speed;
                }
            }
            else if(gotoLeft)  // 왼쪽으로 이동 중일때
            {
                if((x-w > 0 )) { // 왼쪽 끝이 아닐 경우에
                        x -= speed; // 이동
                }
            }
            else //이동이 없을 시
            {
                x = x; // 제자리
            }
        }
        else { //점프중일때
            if (gotoRight) {
                if (x + w >= GameView.backW - w) { // 오른쪽 끝에 도달했을 경우 (점프중이 아닐때와 동일)
                    GameView.stageNum++; //다음 스테이지로
                    x = CommonResources.cw[0]; // 위치 변경
                    GameView.makeStage(); // 스테이지 생성
                    setMove( false ,   false ); // 다음 맵 이동 시 자동 이동 x
                } else {
                    x += speed; // 우로 이동
                }
            }
            if (gotoLeft) {
                if ((x - w > 0)) { // 왼쪽 끝이 아닐 경우
                    x -= speed; // 좌로 이동
                }
            }
                //캐릭터 높이 위치 조정 (떨어지도록)
                dir.y += gravity * Time.deltaTime;
                y += dir.y * Time.deltaTime;

                if (y > ground - h) { // 중력 판단
                    y = ground - h;
                    jumpCnt = 0;
                    isJump = false;
                    animNum = 0;
                    x = x;
                }
        }

    }
    public void setMove(boolean btnLeft , boolean btnRight ) // 좌우 이동
    {
        if(btnLeft) // 왼쪽 이동 버튼 클릭시 캐릭 이동
        {
                x -= speed; // 캐릭 좌측 이동
                charactermoving = true; // 캐릭 이미지 변경 가능하도록
                gotoLeft = true;
                gotoRight = false;
        }
        else if(btnRight) // 오른쪽 이동 버튼 클릭시
        {

                x += speed; // 캐릭 우측 이동
                charactermoving = true; // 캐릭 이미지 변경
                gotoLeft = false;
                gotoRight = true;

        }

        else if(!btnLeft&&!btnRight) // 이동 버튼을 둘다 클릭하고 있지 않을 경우
        {
            dir.x = 0; // x위치 그대로
            charactermoving = false ;// 캐릭 변화 x

            gotoLeft = false;
            gotoRight = false;


            if(y > ground - h) // 중력 판정
            {
                y = ground - h ;
                jumpCnt = 0;
                isJump = false;
            }

        }
    }

    public void setJump(boolean btnJump) // 점프
    {
        if(btnJump && jumpCnt < 1 ) // 점프 도중일 경우
        {
            dir.y = -speedJump;
            animNum = 0;
            jumpCnt++;
            isJump = true;
            charactermoving = false;
        }
    }

    private void makeBitmap(Context context) // 캐릭터 크기 값에 해당 부분 저장
    {
        //캐릭터에 필요한 변수 값들 설정
         for(int i = 0 ; i < 2 ; i++) {
            imgs[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit1+i);
            imgs[i] = Bitmap.createScaledBitmap(imgs[i],imgs[i].getWidth()/2,imgs[i].getHeight()/2,true);
         }
         w = imgs[0].getWidth() / 2;
         h = imgs[0].getHeight() / 2;
         for(int i = 0 ; i < 2 ; i++)
         {
             CommonResources.cw[i] = w;
             CommonResources.ch[i] = h;
             CommonResources.cr[i] = w;
         }
         img = imgs[0];
    }

    public void SizeupBitmap(Context context ) // 캐릭터 사이즈 업
    {
        if(GameView.life == 1)
        {
            GameView.life++;
        }
        //캐릭터에 필요한 변수 값들 설정
        for(int i = 0 ; i < 2 ; i++) {
            imgs[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit1+i);
            imgs[i] = Bitmap.createScaledBitmap(imgs[i],imgs[i].getWidth()/3*2,imgs[i].getHeight()/3*2,true);
        }
        w = imgs[0].getWidth() / 2;
        h = imgs[0].getHeight() / 2;
        for(int i = 0 ; i < 2 ; i++)
        {
            CommonResources.cw[i] = w;
            CommonResources.ch[i] = h;
            CommonResources.cr[i] = w;
        }
        img = imgs[0];
    }

    public void SizedownBitmap(Context context) // 캐릭터 사이즈 다운
    {
        //캐릭터에 필요한 변수 값들 설정
        for(int i = 0 ; i < 2 ; i++) {
            imgs[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit1+i);
            imgs[i] = Bitmap.createScaledBitmap(imgs[i],imgs[i].getWidth()/2,imgs[i].getHeight()/2,true);
        }
        w = imgs[0].getWidth() / 2;
        h = imgs[0].getHeight() / 2;
        for(int i = 0 ; i < 2 ; i++)
        {
            CommonResources.cw[i] = w;
            CommonResources.ch[i] = h;
            CommonResources.cr[i] = w;
        }
        img = imgs[0];
    }
    private boolean checkBlock() // 캐릭터와 블록과의 충돌 유무 확인
    {

        int n = 0 ;

        for(Block tmp : GameView.mBlock) // 모든 블록과의 충돌 판정 확인
        {
            n = tmp.getHit(x , y, CommonResources.cr[0]); // 블록 클래스에 있는 getHit메소드 호출
            switch (n) // 스위치문
            {

                case 1 : // 캐릭터의 바닥과 블록의 윗면이 충돌했을 경우
                    y = Block.newY; // 캐릭 위치
                    isJump = false; // 점프 가능
                    jumpCnt = 0; // 점프 가능
                    if(y > ground - h) // 중력판정
                    {
                        y = ground - h ;
                        jumpCnt = 0;
                        isJump = false;
                    }
                    break;
                case 2 : // 캐릭의 윗면과 블록의 아랫면이 충돌
                    if(tmp.kind == 1) { // 일반 블록일 경우에는 블록 파괴 후 동전 생성
                        GameView.mCoin.add(new Coin(1,tmp.x , tmp.y - CommonResources.coinh));
                    }
                    if(tmp.kind == 3)
                    {
                        int number = (int)(Math.random()*2)+1;
                        tmp.kind--;
                        if(number == 1) {
                            GameView.mCoin.add(new Coin(2, tmp.x, tmp.y - 3 * CommonResources.coinh));
                        }
                        if(number == 2)
                        {
                            GameView.mMonster.add(new Monster(1,tmp.x,tmp.y-3 * CommonResources.monsterh));
                        }
                    }
                    y += tmp.h;
                    dir.y = tmp.newY; // 그자리에서 바로 떨어지도록
                    break;
                case 3 : // 그 외의 경우
                    if(gotoRight) {
                        x -= speed;
                    }
                    break;
                case 4:
                    if(gotoLeft) {
                        x += speed;
                    }
                    break;

            }
            if(n > 0) break;

        }
        if(n>0)
            return true;
        else
            return false;
    }

    private boolean isBlock() // 캐릭 아래에 블록이 있는지 판단
    {
        boolean isblock = false;
        for(Block tmp : GameView.mBlock)
        {
            if(tmp.y > y) { // 캐릭보다 아래에 있는 블록만 비교 (그렇지 않을 경우에는 위에 블록이 있으면 중력 판정 제대로 불가)
                isblock = tmp.isblock(x, y, CommonResources.cw[0], CommonResources.ch[0]); // 아래에 블록이 있는지를 확인
            }
            if(isblock == true) // 있을 경우
            {
                return true;
            }

        }
        return false; // 없을 경우
    }

    private boolean checkCoin() // 캐릭터와 코인과의 충돌 유무 확인
    {

        int n = 0 ;

        for(Coin tmp : GameView.mCoin) // 모든 코인과의 충돌 판정 확인
        {
            n = tmp.getHit(x , y, CommonResources.cr[0]); // 코인 클래스에 있는 getHit메소드 호출
            if(n > 0) break;
        }
        if(n>0) {
            return true;
        }
        else
            return false;
    }

    private boolean checkMonster() // 몬스터와 캐릭터의 충돌  유무 확인
    {
        int n = 0 ;
        for(Monster tmp : GameView.mMonster) // 모든 몬스터와의 충돌 판정 확인
        {
            n = tmp.getHit(x , y, CommonResources.cr[0]); // 몬스터 클래스에 있는 getHit메소드 호출
            if(n > 0) break;
        }
        if(n>0)
            return true;
        else
            return false;
    }

}


