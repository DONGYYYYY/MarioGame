package com.project.my.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

/*
    데이터 속성들 저장. 블록 , 배경 , 코인, 몬스터 (캐릭터는 character클래스에서 설정함)
 */
public class CommonResources {

    static public Bitmap[] character = new Bitmap[2]; // 캐릭터
    static public float[] cw = new float[2]; // 캐릭터 너비
    static public float[] ch = new float[2];
    static public float[] cr = new float[2];

    static public Bitmap blockimg; // 블록이미지
    static public Bitmap useblockimg; // 블록이미지
    static public Bitmap itemblockimg;
    static public int bw , bh;
    static public Bitmap backimg;

    static public int coinw , coinh;
    static public Bitmap coinimg;
    static public Bitmap itemimg;

    static public int monsterw , monsterh;
    static public Bitmap monsterimg;
    //사운드
    static public MediaPlayer mPlayer; // 배경음악
    static public SoundPool mSound; // 사운드

    static public void set(Context context , int scrW , int scrH)
    {
        //해당 메소드 호출
        makeBlock(context,scrW,scrH);
        makeBackImg(context,scrW,scrH);
        makeCoin(context,scrW,scrH);
        makeMonster(context,scrW,scrH);
        makeSound(context);
    }

    private static void makeBackImg(Context context  , int scrW , int scrH) // 배경 설정
    {
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.backimg);

        backimg = Bitmap.createScaledBitmap(tmp,scrW,scrH,true);
    }

    private static void makeBlock(Context context , int scrW , int scrH) // 블록 설정
    {
        int w = scrW / 10;
        int h = scrH / 10;
        //기본블록
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(),R.drawable.brickblock);
        blockimg = Bitmap.createScaledBitmap(temp, w , h , true);

        temp = BitmapFactory.decodeResource(context.getResources(),R.drawable.usedblock);
        useblockimg = Bitmap.createScaledBitmap(temp, w , h , true);

        temp = BitmapFactory.decodeResource(context.getResources(),R.drawable.itemblock);
        itemblockimg = Bitmap.createScaledBitmap(temp, w , h , true);
        bw = w / 2;
        bh = h / 2;
    }


    private static void makeCoin(Context context , int scrW , int scrH) // 코인 설정
    {
        int w = scrW / 15;
        int h = scrH / 15;

        Bitmap temp = BitmapFactory.decodeResource(context.getResources(),R.drawable.coinicon);
        coinimg = Bitmap.createScaledBitmap(temp, w , h , true);
        temp = BitmapFactory.decodeResource(context.getResources(),R.drawable.item);
        itemimg = Bitmap.createScaledBitmap(temp, w , h , true);
        coinw = w / 2;
        coinh = h / 2;
    }

    private static void makeMonster(Context context , int scrW , int scrH) // 코인 설정
    {
        int w = scrW / 14;
        int h = scrH / 14;

        Bitmap temp = BitmapFactory.decodeResource(context.getResources(),R.drawable.monster);
        monsterimg = Bitmap.createScaledBitmap(temp, w , h , true);

        monsterw = w / 2;
        monsterh = h / 2;
    }
    private static void makeSound(Context context)
    {
        //사운드 설정
        mPlayer = MediaPlayer.create(context,R.raw.mainbgm);
        mPlayer.setLooping(true);
        mPlayer.start();

        // 롤리팝 이전 버전인가?
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        } else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            mSound = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(5).build();
        }
    }
}
