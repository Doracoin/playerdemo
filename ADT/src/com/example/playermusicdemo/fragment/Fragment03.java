package com.example.playermusicdemo.fragment;

import com.example.playermusicdemo.R;
import com.example.playermusicdemo.view.LyricView;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment03 extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment03, container, false);
	}

	public static com.example.playermusicdemo.view.LyricView myLyricView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		myLyricView = (LyricView) getView().findViewById(R.id.myLyricView);
	}

	
}
