package com.project.my.finalproject;

public class Stage {

    /*
        맵 설정에 관련된 클래스
     */
    static private String[][] map = { //맵 문자열 배열로 설정
      new String[] {
              " 1 1 1 1 1 1 1 1 1 1",
              ". . . . . . . . . .",
              ". . . . . . . . . .",
              ". . . 2 1 2  .  . . ."
      },
      new String[]
              {
                      " 1 1 1 1 1 1 1 1 1 1",
                      ". . . . . . . . . .",
                      ". . . . . . . . . .",
                      ". . 3 . . 1 . . . ."
              },
       new String[]
       {
               "1 1 1 . . 1 1 1 1 1",
               ". . . . . . . . . .",
               ". . . . . . . . . ."
               ,". . 1 1 . . . . . .",
               ". . . 1 . . . . . ."
        },
            new String[]
                    {
                            "1 1 . . 1 1 1 1 1 1",
                            ". . . . . . . . . .",
                            ". . . . . . . . . .",
                            ". . 1 1 1 1 . . . . .",
                            ". . . . . . . . . ."
                    },
            new String[]
                    {
                            "1 1 1 1 1 1 1 1 1 1",
                            ". . . . . .  . . . .",
                            ". . . . . . . . . .",
                            ". . . 3 3 3 . . . . .",
                            ". . . . . . . . . ."
                    },
            new String[]
                    {
                            "1 1 1 1 1 1 1 1 1 1",
                            ". . . . . . . . . .",
                            ". . . . . . . . . .",
                            ". . 1 3 1 1 . . . . .",
                            ". . . . .  . . . . ."
                    }
    };

    static public void makeStage(int stageNum) // 스테이지 생성
    {
        if(stageNum/map.length == 1)
        {

            GameView.life = 0;
            GameView.checkOver();
            return;
        }

        stageNum = stageNum%map.length; // 무한 반복


        int w = CommonResources.bw;
        int h = CommonResources.bh;

        float y = GameView.backH - (h * 2);

        String[] tmp = map[stageNum];

        int n = 0;

        for(int i = 0 ; i < tmp.length ; i++) // 스테이지 그리기
        {
            String s = tmp[i].trim(); // 띄어쓰기 무시
            float x = 0;

            for(int j = 0 ; j < s.length() ; j++)
            {
                switch (s.charAt(j)){
                    case '.' : // . 일 경우 공백
                        x += w * 2;
                        break;
                    case '1': // 일반 블록
                        n = Integer.parseInt(s.substring(j,j+1));
                        GameView.mBlock.add(new Block(n,x+w,y+h));
                        x += w * 2;
                        break;
                    case '2': // 사용 블록
                        n = Integer.parseInt(s.substring(j,j+1));
                        GameView.mBlock.add(new Block(n,x+w,y+h));
                        x += w * 2;
                        break;
                    case '3': // 아이템 블록
                        n = Integer.parseInt(s.substring(j,j+1));
                        GameView.mBlock.add(new Block(n,x+w,y+h));
                        x += w * 2;
                        break;
                    case '4':
                        GameView.mMonster.add(new Monster(1,x+w,y+h));
                        x += w * 2;
                    default :
                        break;
                }
            }
            y -= h * 2;
        }
    }
}
