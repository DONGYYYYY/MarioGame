package com.project.my.finalproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {


    static public Context context;
    public GameThread mThread;
    static public boolean doThis = false;

    static public boolean isDemo; // 게임 정지 유무
    //너비 높이 딜레이 스테이지
    private int w, h;
    static private Bitmap imgBack;
    static public int backW , backH;
    static public boolean over = false;
    static public int stageNum ;
    static private float delaySpan;
    // 점수 목숨
    static public int score;
    public static int life = 0;
    static private DecimalFormat decFormat = new DecimalFormat("#,##0");
    // 화면에 글 표시
    private Paint paint = new Paint();
    private Paint stroke = new Paint();

    // /버튼
    private Button btnLeft;
    private Button btnRight;
    private Button btnJump;

    //터치 판정
    private boolean leftTouch = false;
    private boolean rightTouch = false;
    private boolean jumpTouch = false;
    //캐릭터
    static public Character character;

    //점수
    public static String sScore, msg;

    private boolean changeMap = false;
    //블록 리스트
    static public List<Block> mBlock = Collections.synchronizedList(new ArrayList<Block>());
    static public List<Coin> mCoin = Collections.synchronizedList(new ArrayList<Coin>());
    static public List<Monster> mMonster = Collections.synchronizedList(new ArrayList<Monster>());

    //점프키와 좌우이동의 판정을 다르게 설정
    boolean isTouch = false;
    boolean isJump = false;


    public GameView(Context context, AttributeSet attrs) { // 생성자
        super(context , attrs);
        this.context = context;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        this.backW = w;
        this.backH = h;
        this.w = w;
        this.h = h;
        initGame(); // 게임초기화
        character = new Character(context ,w, h);
        makeStage(); // 스테이지 생성
        makeButton(); // 버튼 만들기
        if(mThread == null) {
            mThread = new GameThread();
            mThread.start();
        };
    }


    protected void onDetachedFromWindow() { //종료시 완전 종료
        mThread.canRun = false;
        super.onDetachedFromWindow();
    }


    private void initGame() { // 게임 초기화
        stageNum = 0;
        life = 1; // 목숨 초기화
        CommonResources.set(context,w,h);
        imgBack = CommonResources.backimg;


        //캐릭터 초기 위치
        character.x = CommonResources.cw[0] * 4;
        character.y = backH - CommonResources.ch[0] * 3;

        isDemo = false;

        score = 0;
        msg = "계속하시겠습니까?\n[Touch] 다시 시작\n[Back Key] 종료";

        paint.setTextSize(60);
        paint.setColor(0xff000080);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        stroke.setTextSize(60);
        stroke.setColor(Color.WHITE);
        stroke.setTypeface(Typeface.DEFAULT_BOLD);

        stroke.setTextAlign(Paint.Align.CENTER);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(10);

        setScore();

    }//initGame

    static public void setScore() { // 점수
        sScore = String.format("Score : %s", decFormat.format(score) );

    }

    static private void delay(float span) {
        delaySpan = span;
    }


    static public void makeStage() { // 맵생성
        mBlock.clear();
        mCoin.clear();
        mMonster.clear();
        Stage.makeStage(stageNum);
        delay(0.5f);
    }

    static public void checkOver() { // 게임 종료
        if(--life > 0) return;
        CommonResources.mPlayer.stop(); // 음악 종료
        character.SizedownBitmap(context);// 크기 작게
        isDemo = true;
        character.setMove(false,false);
        delay(0.5f);
    }

    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(imgBack,0,0,null); // 배경 그리기
        // 점수 text 보여주기
        paint.setTextAlign(Paint.Align.LEFT);
        stroke.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(sScore,20,80,stroke);
        canvas.drawText(sScore,20,80,paint);

        //블록 그리기 (동기화)
        synchronized (mBlock)
        {
            for(Block tmp : mBlock)
            {
                canvas.drawBitmap(tmp.img , tmp.x - tmp.w , tmp.y - tmp.h , null);
            }
        }

        //코인 그리기
        synchronized (mCoin)
        {
            for(Coin tmp : mCoin)
            {
                canvas.drawBitmap(tmp.img , tmp.x - tmp.w , tmp.y - tmp.h , null);
            }
        }

        //몬스터 그리기
        synchronized (mMonster)
        {
            for(Monster tmp : mMonster)
            {
                canvas.drawBitmap(tmp.img , tmp.x - tmp.w , tmp.y - tmp.h , null);
            }
        }

        //버튼 그리기
        canvas.drawBitmap(btnLeft.img, btnLeft.x , btnLeft.y,null);
        canvas.drawBitmap(btnRight.img, btnRight.x , btnRight.y,null);
        canvas.drawBitmap(btnJump.img, btnJump.x , btnJump.y,null);

        canvas.save();
        canvas.restore();

        //캐릭터 그리기
        canvas.drawBitmap(character.img,character.x-character.w,character.y - character.h,null);

        if (isDemo) { // 게임 종료일 경우
            paint.setTextSize(90);
            stroke.setTextSize(90);

            paint.setTextAlign(Paint.Align.CENTER);
            stroke.setTextAlign(Paint.Align.CENTER);

            float y = h * 0.4f;
            for (String tmp : msg.split("\n")) {
                canvas.drawText(tmp, w / 2, y, stroke);
                canvas.drawText(tmp, w / 2, y, paint);
                y += h * 0.1f;
            }

            paint.setTextSize(60);
            stroke.setTextSize(60);
        }
    }

    private void moveObject() // 움직임
    {
        character.update(); // 캐릭터 점프 좌우 이동 판단후 위치값 지속적인 업데이트
    }

    private void removeDead() {// 블록 파괴
        synchronized (mBlock)
        {
            for(int i = mBlock.size() - 1 ; i >= 0 ; i--)
            {
                if(mBlock.get(i).isDead)
                {
                    mBlock.remove(i);
                }
            }
        }
        synchronized (mCoin) // 코인 파괴
        {
            for(int i = mCoin.size() - 1 ; i >= 0 ; i--)
            {
                if(mCoin.get(i).isDead)
                {
                    mCoin.remove(i);
                }
            }
        }
        synchronized (mMonster) // 코인 파괴
        {
            for(int i = mMonster.size() - 1 ; i >= 0 ; i--)
            {
                if(mMonster.get(i).isDead)
                {
                    mMonster.remove(i);
                }
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (delaySpan > 0) return true;



        int action = event.getActionMasked();


        switch(action) // 터치 이벤트 스위치문
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: // 누르고 있는 도중
                    isJump = true; // 점프키 터치 판단
                    isTouch = true; // 좌우터치 판단
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: // 터치를 안하는 중
                isTouch = false; // 좌우터치 판단
                isJump = false; // 점프키 터치 판단
                break;
            default:
                return true;
        }

        if (isDemo == true) { // 정지시 시작 유무 결정정
            isDemo = false;
            initGame();
            makeStage(); // 맵 변경 후 자동으로 이동 안하도록
            return true;
        }


        int ptrIdx = MotionEventCompat.getActionIndex(event);
        int id = MotionEventCompat.getPointerId(event,ptrIdx);

        float x = MotionEventCompat.getX(event,ptrIdx);
        float y = MotionEventCompat.getY(event,ptrIdx);


        //버튼 액션 메소드 호출
        btnLeft.action(id,isTouch,x,y);
        btnRight.action(id,isTouch,x,y);
        btnJump.action(id,isJump,x,y);

        character.setMove(btnLeft.isTouch,btnRight.isTouch); // 좌우 이동 동작
        character.setJump(btnJump.isTouch);  // 점프동작
        return true;
    }

    private void makeButton() // 버튼 생성
    {
        Bitmap imgLeft = BitmapFactory.decodeResource(getResources(),R.drawable.left);
        Bitmap imgRight = BitmapFactory.decodeResource(getResources(),R.drawable.right);
        Bitmap imgJump = BitmapFactory.decodeResource(getResources(),R.drawable.jump);

        imgLeft = Bitmap.createScaledBitmap(imgLeft,imgLeft.getWidth()/4,imgLeft.getHeight()/4,true);
        imgRight = Bitmap.createScaledBitmap(imgRight,imgRight.getWidth()/4,imgRight.getHeight()/4,true);
        imgJump = Bitmap.createScaledBitmap(imgJump,imgJump.getWidth()/4,imgJump.getHeight()/4,true);

        int bw = imgLeft.getWidth();
        int bh = imgLeft.getWidth();

        int y = h - bh - 10;
        Point lPos = new Point(10,y);
        Point rPos = new Point(bw+20 , y);
        Point jPos = new Point(w-bw-10 , y);

        btnLeft = new Button(imgLeft,lPos);
        btnRight = new Button(imgRight,rPos);
        btnJump = new Button(imgJump,jPos);

    }
    class GameThread extends Thread { // 스레드

        public boolean canRun = true;
        public void run()
        {
            while(canRun)
            {
                try {
                    Time.update();
                    delaySpan -= Time.deltaTime;
                    moveObject();
                    removeDead();
                    postInvalidate();
                    sleep(5);
                }catch(Exception e)
                {

                }

            }
        }
    }
}

