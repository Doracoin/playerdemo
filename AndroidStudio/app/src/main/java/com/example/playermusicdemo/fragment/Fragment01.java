package com.example.playermusicdemo.fragment;


import java.text.SimpleDateFormat;
import java.util.List;

import com.example.playermusicdemo.App;
import com.example.playermusicdemo.R;
import com.example.playermusicdemo.Myclass.Audio;
import com.example.playermusicdemo.Myclass.ConstUtil;
import com.example.playermusicdemo.Myclass.MediaUtils;
import com.example.playermusicdemo.view.AlwaysMarqueeTextView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment01 extends Fragment{
	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			return inflater.inflate(R.layout.fragment01, container, false);
		}
	public List audioList;
	private static ListView MyList;
	private static MyAdaper myAdaper;
	private static int playerNum = 0;
@Override
public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
	App.getInstance();
	audioList = App.audioList;
	 MyList = (ListView) getView().findViewById(R.id.MyList);
	 myAdaper = new MyAdaper(audioList);
	 MyList.setAdapter(myAdaper);
	 MyList.setOnItemClickListener(new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			sendBroadcastToService(arg2);
		}});

		
}
/**
 * 向后台Service发送切换歌曲控制广播
 * 
 * @param Index
 *  
 * */
protected void sendBroadcastToService(int Index) {
	// TODO Auto-generated method stub
	Intent intent = new Intent();
	intent.setAction(ConstUtil.MUSICSERVICE_ACTION);
	intent.putExtra("control", ConstUtil.STATE_SECLECT);
	intent.putExtra("Index", Index);
	// 向后台Service发送播放控制的广播
	getActivity().sendBroadcast(intent);
}

/**
 * 在列表中标志正在播放歌曲的位置
 * @param selctct
 */
public static void UpdateAdapter(int selctct){
	playerNum = selctct;
	myAdaper.notifyDataSetChanged();
	int begin = MyList.getFirstVisiblePosition();
	int last = MyList.getLastVisiblePosition();
	if(selctct>begin&&selctct<last){
		
	}else{
		MyList.setSelection(selctct);
	}
	
	
}
class MyAdaper extends BaseAdapter{
	private List mList;
	private ViewHolder holder;
	public MyAdaper(List audioList){
		this.mList = audioList;
	}
	class ViewHolder {
		
		private TextView tv2;
		private TextView tv3;
		private com.example.playermusicdemo.view.AlwaysMarqueeTextView tv1;
		private ImageView imgicon;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
	    if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item,
					null);
			holder.tv1 = (AlwaysMarqueeTextView) convertView.findViewById(R.id.name);
			holder.tv2 = (TextView) convertView.findViewById(R.id.author);
			holder.tv3 = (TextView) convertView.findViewById(R.id.time);
			holder.imgicon = (ImageView) convertView.findViewById(R.id.isPlaying);
			convertView.setTag(holder);
		}
	    Audio audio = (Audio) mList.get(position);
	    holder.tv1.setText(audio.getDisplayName());
	    holder.tv2.setText(audio.getArtist());
	    holder.tv3.setText(timestr(audio.getDuration()));
	    if(position == playerNum){
	    	holder.imgicon.setVisibility(View.VISIBLE);
	    }else{
	    	holder.imgicon.setVisibility(View.GONE);
	    }
	    return convertView;
	}}
private String timestr(int im) {

	SimpleDateFormat sp = new SimpleDateFormat("mm:ss");
	sp.format(im);
	return sp.format(im);
}
}
