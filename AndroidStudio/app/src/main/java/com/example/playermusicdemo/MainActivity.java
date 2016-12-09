package com.example.playermusicdemo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.playermusicdemo.Myclass.Audio;
import com.example.playermusicdemo.Myclass.ConstUtil;
import com.example.playermusicdemo.Myclass.MediaUtils;
import com.example.playermusicdemo.Myclass.MusicService;
import com.example.playermusicdemo.Myclass.MusicUtils;
import com.example.playermusicdemo.Myclass.PixelUtil;
import com.example.playermusicdemo.fragment.Fragment01;
import com.example.playermusicdemo.fragment.Fragment02;
import com.example.playermusicdemo.fragment.Fragment03;
import com.example.playermusicdemo.view.AlwaysMarqueeTextView;
import com.example.playermusicdemo.view.LyricView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {
    private ViewPager viewpager;
    private ImageView bitpic01, bitpic02, bitpic03, img2, img3, img4;
    static TextView author, beging_tv, total_tv;
    static com.example.playermusicdemo.view.AlwaysMarqueeTextView name;
    static SeekBar skbProgress;
    private Fragment[] fragments = {new Fragment01(), new Fragment02(),
            new Fragment03()};
    // 是否正在播放
    boolean isPlaying = false;
    public List audioList;
    private MusicBoxReceiver mReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        App.getInstance();
        audioList = App.audioList;
        bitpic01 = (ImageView) findViewById(R.id.bitpic01);
        bitpic02 = (ImageView) findViewById(R.id.bitpic02);
        bitpic03 = (ImageView) findViewById(R.id.bitpic03);
        name = (AlwaysMarqueeTextView) findViewById(R.id.name);
        author = (TextView) findViewById(R.id.author);
        beging_tv = (TextView) findViewById(R.id.beging_tv);
        total_tv = (TextView) findViewById(R.id.total_tv);
        skbProgress = (SeekBar) findViewById(R.id.skbProgress);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.marquee_image_viewpager);

        Audio audio = (Audio) audioList.get(0);
        name.setText(audio.getDisplayName());// 更新音乐标题
        author.setText(audio.getArtist());// 更新音乐作者
        // 注册接收器
        mReceiver = new MusicBoxReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstUtil.MUSICBOX_ACTION);
        registerReceiver(mReceiver, filter);
        // 启动后台Service
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        skbProgress.setOnSeekBarChangeListener(sChangeListener);

        KickerFragmentAdapter adpater = new KickerFragmentAdapter(
                getSupportFragmentManager(), this);
        viewpager.setAdapter(adpater);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(1);//
        celectBottom(2);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                celectBottom(arg0 + 1);
            }
        });

    }

    public static TextView getbeging_tv() {
        return beging_tv;
    }

    public static TextView gettotal_tv() {
        return total_tv;
    }

    // 底部导航菜单
    public void celectBottom(int num) {
        switch (num) {
            case 1:
                bitpic01.setImageResource(R.drawable.page_indicator_focused);
                bitpic02.setImageResource(R.drawable.page_indicator_unfocused);
                bitpic03.setImageResource(R.drawable.page_indicator_unfocused);
                break;
            case 2:
                bitpic01.setImageResource(R.drawable.page_indicator_unfocused);
                bitpic02.setImageResource(R.drawable.page_indicator_focused);
                bitpic03.setImageResource(R.drawable.page_indicator_unfocused);
                break;
            case 3:
                bitpic01.setImageResource(R.drawable.page_indicator_unfocused);
                bitpic02.setImageResource(R.drawable.page_indicator_unfocused);
                bitpic03.setImageResource(R.drawable.page_indicator_focused);
                break;
            default:
                break;
        }
    }

    // viewpaper数据适配器
    class KickerFragmentAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public KickerFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int arg0) {

            return fragments[arg0];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.img2:// 上一首
                img4.setImageResource(R.drawable.pause_button);
                sendBroadcastToService(ConstUtil.STATE_PREVIOUS);
                isPlaying = true;
                break;
            case R.id.img3:// 下一首
                img4.setImageResource(R.drawable.pause_button);
                sendBroadcastToService(ConstUtil.STATE_NEXT);
                isPlaying = true;
                break;
            case R.id.img4:// 播放或暂停
                if (!isPlaying) {
                    img4.setImageResource(R.drawable.pause_button);
                    sendBroadcastToService(ConstUtil.STATE_PLAY);
                    isPlaying = true;
                } else {
                    img4.setImageResource(R.drawable.playing_button);
                    sendBroadcastToService(ConstUtil.STATE_PAUSE);
                    isPlaying = false;
                }
                break;
            default:
                break;
        }
    }

    /**
     * SeekBar进度改变事件
     */
    OnSeekBarChangeListener sChangeListener = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            // 当拖动停止后，控制mediaPlayer播放指定位置的音乐
            MusicService.mediaPlayer.seekTo(seekBar.getProgress());
            MusicService.isChanging = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            MusicService.isChanging = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

        }
    };

    /**
     * 向后台Service发送控制广播
     *
     * @param state int state 控制状态码
     */
    protected void sendBroadcastToService(int state) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction(ConstUtil.MUSICSERVICE_ACTION);
        intent.putExtra("control", state);
        // 向后台Service发送播放控制的广播
        sendBroadcast(intent);
    }

    // 创建一个广播接收器用于接收后台Service发出的广播
    class MusicBoxReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 获取Intent中的current消息，current代表当前正在播放的歌曲
            boolean UpDate = intent.getBooleanExtra("UpDate", false);
            if (UpDate) {
                int position = intent.getIntExtra("position", -1);
                //int duration = intent.getIntExtra("duration", -1);
                beging_tv.setText(timestr(position));
                skbProgress.setProgress(position);
                Fragment03.myLyricView.setOffsetY(Fragment03.myLyricView.getOffsetY()
                        - Fragment03.myLyricView.SpeedLrc());
                Fragment03.myLyricView.SelectIndex(position);
                Fragment03.myLyricView.invalidate(); // 更新视图

            } else {
                int current = intent.getIntExtra("current", -1);
                Audio audio = (Audio) audioList.get(current);
                name.setText(audio.getDisplayName());// 更新音乐标题
                author.setText(audio.getArtist());// 更新音乐作者
                total_tv.setText(timestr(audio.getDuration()));
                skbProgress.setMax(audio.getDuration());
                img4.setImageResource(R.drawable.pause_button);
                Fragment01.UpdateAdapter(current);

                //专辑页面
                Bitmap bm = MusicUtils.getArtwork(MainActivity.this, audio.getId(), audio.getAlbumId(), true);
                Fragment02.setMusicImg(bm);
                String lrcPath = "/storage/emulated/0/Musiclrc/";

                //歌词页面
                /*File Musiclrc = new File("/storage/sdcard1/Musiclrc/");*/
                File Musiclrc = new File(lrcPath);
                if (!Musiclrc.exists())
                    Musiclrc.mkdirs();
                String lrc = lrcPath + audio.getDisplayName();
                SerchLrc(lrc.replace(".mp3", ".lrc"));
            }

        }

    }

    private String searchFile(String keyword) {
        String result = "";
        File[] files = new File("/").listFiles();
        for (File file : files) {
            if (file.getName().indexOf(keyword) >= 0) {
                result += file.getPath() + "\n";
            }
        }
        if (result.equals("")) {
            result = "找不到文件!!";
        }
        return result;
    }

    private String timestr(int im) {

        SimpleDateFormat sp = new SimpleDateFormat("mm:ss");
        sp.format(im);
        return sp.format(im);
    }

    /*
     * 设置歌词路径
     */
    public void SerchLrc(String lrc) {
        lrc = lrc.substring(0, lrc.length() - 4).trim() + ".lrc".trim();
        LyricView.read(lrc);
        Fragment03.myLyricView.setSIZEWORD(PixelUtil.dp2px(18));
        Fragment03.myLyricView.setOffsetY(180);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog dlg = new AlertDialog.Builder(this).create();
            dlg.setTitle("退出");
            dlg.setMessage("您点击了退出，是否同时关闭播放服务？");
            dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dlg.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendBroadcast(new Intent(ConstUtil.MUSIC_STOP_ACTION));
                    finish();
                }
            });
            dlg.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
