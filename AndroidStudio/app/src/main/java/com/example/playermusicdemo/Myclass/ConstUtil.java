package com.example.playermusicdemo.Myclass;

public class ConstUtil {
    //MusicBox接收器所能响应的Action
    public static final String MUSICBOX_ACTION="com.example.playermusicdemo.MAINACTIVITY_ACTION";
    //MusicService接收器所能响应的Action
    public static final String MUSICSERVICE_ACTION="com.example.playermusicdemo.Myclass.MUSICSERVICE_ACTION";
    public static final String MUSIC_STOP_ACTION="STOP_MUSIC";
    //初始化flag
    public static final int STATE_NON=0x122;
    //播放的flag
    public static final int STATE_PLAY=0x123;
    //暂停的flag
    public static final int STATE_PAUSE=0x124;
    //停止放的flag
    public static final int STATE_STOP=0x125;
    //播放上一首的flag
    public static final int STATE_PREVIOUS=0x126;
    //播放下一首的flag
    public static final int STATE_NEXT=0x127;
    //选项的itemId
    public static final int STATE_SECLECT=0x200;
    //菜单退出选的项的itemId
    public static final int MENU_EXIT=0x201;
     
    public ConstUtil() {
        // TODO Auto-generated constructor stub
    }
 
}