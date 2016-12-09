package com.example.playermusicdemo.fragment;

import com.example.playermusicdemo.R;
import com.example.playermusicdemo.Myclass.MusicUtils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Fragment02 extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment02, container, false);
	}

	public static ImageView MusicImg;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		MusicImg = (ImageView) getView().findViewById(R.id.MusicImg);
	}
	
	public static void setMusicImg(Bitmap bm){
		
		if(bm != null){
			Log.d("12312","bm is not null==========================");
			MusicImg.setImageBitmap(bm);
		}else{
			Log.d("123123","bm is null============================");
		}
		
	}
}
