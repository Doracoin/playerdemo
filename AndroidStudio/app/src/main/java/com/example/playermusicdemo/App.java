package com.example.playermusicdemo;


import java.util.List;

import com.example.playermusicdemo.Myclass.MediaUtils;

import android.app.Application;

public class App extends Application{
	public static App instance;
	public static  List audioList;
@Override
public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	instance =this;
	audioList = MediaUtils.getAudioList(this);
}
public static App getInstance() {
	if (instance==null) {
		instance=new App();
	}
	return instance;
}
public  static void setaudioList(List list){
	audioList = list;
}
public static List getaudioList(){
	return audioList ;
}
}
