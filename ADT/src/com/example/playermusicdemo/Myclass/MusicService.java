package com.example.playermusicdemo.Myclass;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.playermusicdemo.App;
import com.example.playermusicdemo.MainActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;

/**
 * Describe:<br>
 * <br>负责音乐播放的后台Service
 * <br>1.通过接收来自前台的控制播放信息的广播来播放指定音乐
 * <br>2.加载assets目录下的音乐
 * <br>3.为mediaPlayer的完成事件创建监听器当当前音乐播放
 * <br>完成后自动播放下一首音乐
 * <br>4.为每一首正在播放的音乐创建一个定时器，用于检测播放进度
 * <br>并更新进度条
 * <br>@author jph
 * <br>Date：2014.08.07
 * */
public class MusicService extends Service {
    Timer mTimer;
    TimerTask mTimerTask;
    public static boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突
    //创建一个媒体播放器的对象
    public static MediaPlayer mediaPlayer;
    public List audioList;
    //创建一个Asset管理器的的对象
    AssetManager assetManager;
    //当前的播放的音乐
    public static int current=0;
    //当前播放状态
    int state=ConstUtil.STATE_NON;
    //记录Timer运行状态
    boolean isTimerRunning=false;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();  
        App.getInstance();
		audioList = App.audioList;
        //注册接收器
        MusicSercieReceiver receiver=new MusicSercieReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstUtil.MUSICSERVICE_ACTION);
        registerReceiver(receiver, filter);
        mediaPlayer=new MediaPlayer();
        //assetManager=getAssets();
        //为mediaPlayer的完成事件创建监听器
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {           
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub 
//              mTimer.cancel();//取消定时器
                current++;
                prepareAndPlay(current);               
            }
        });
         
    }  
    /**
     * 装载和播放音乐
     * @param index int index 播放第几首音乐的索引
     * */
    protected void prepareAndPlay(int index) {
        // TODO Auto-generated method stub     
        if(isTimerRunning) {//如果Timer正在运行
            mTimer.cancel();//取消定时器
            isTimerRunning=false;
        }      
        if(index>audioList.size()-1) {
            current=index=0;
        }
        if(index<0) {
            current=index=audioList.size()-1;
        }
        //发送广播停止前台Activity更新界面
        Intent intent=new Intent();
        intent.putExtra("UpDate", false);
        intent.putExtra("current", index);
        intent.setAction(ConstUtil.MUSICBOX_ACTION);
        sendBroadcast(intent);
        Audio audio = (Audio) audioList.get(current);
        try{
            mediaPlayer.reset();//初始化mediaPlayer对象
           Log.e("12312312312", audio.getPath()+".....");
            mediaPlayer.setDataSource(audio.getPath()); 
            //准备播放音乐
            mediaPlayer.prepare();
            //播放音乐
            mediaPlayer.start();
            //getDuration()方法要在prepare()方法之后，否则会出现Attempt to call getDuration without a valid mediaplayer异常
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //----------定时器记录播放进度---------//
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                isTimerRunning=true;
                if(isChanging==true)//当用户正在拖动进度进度条时不处理进度条的的进度
                    return; 
                int position = mediaPlayer.getCurrentPosition();
    			int duration = mediaPlayer.getDuration();
                
    			 //发送广播停止前台Activity更新界面
    	        Intent intent=new Intent();
    	        intent.putExtra("UpDate", true);
    	        intent.putExtra("position", position);
    	        intent.putExtra("duration", duration);
    	        intent.setAction(ConstUtil.MUSICBOX_ACTION);
    	        sendBroadcast(intent);
                
            }
        };
        //每隔10毫秒检测一下播放进度
        mTimer.schedule(mTimerTask, 0, 10);      
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }  
    //创建广播接收器用于接收前台Activity发去的广播
    class MusicSercieReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int control=intent.getIntExtra("control", -1);
            switch(control) {
            case ConstUtil.STATE_PLAY://播放音乐
                if(state==ConstUtil.STATE_PAUSE) {//如果原来状态是暂停
                    mediaPlayer.start();                   
                }else if(state!=ConstUtil.STATE_PLAY) {
                    prepareAndPlay(current);
                }
                state=ConstUtil.STATE_PLAY;
                break;
            case ConstUtil.STATE_PAUSE://暂停播放
                if(state==ConstUtil.STATE_PLAY) {
                    mediaPlayer.pause();
                    state=ConstUtil.STATE_PAUSE;
                }
                break;
            case ConstUtil.STATE_STOP://停止播放
                if(state==ConstUtil.STATE_PLAY||state==ConstUtil.STATE_PAUSE) {
                    mediaPlayer.stop();
                    state=ConstUtil.STATE_STOP;
                }
                break;
            case ConstUtil.STATE_PREVIOUS://上一首
                prepareAndPlay(--current);
                state=ConstUtil.STATE_PLAY;
                break;
            case ConstUtil.STATE_NEXT://下一首            
                prepareAndPlay(++current);
                state=ConstUtil.STATE_PLAY;
                break;
            case ConstUtil.STATE_SECLECT:
            	 int Index=intent.getIntExtra("Index", -1);
            	 current = Index;
            	 prepareAndPlay(current);
                 state=ConstUtil.STATE_PLAY;
            	break;
            default:
                break;
            }
        }

		
         
    }
}
