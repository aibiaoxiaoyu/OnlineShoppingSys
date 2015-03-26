package com.echo.onlineshoppingsys.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo.onlineshoppingsys.R;
import com.echo.onlineshoppingsys.activity.MainActivity;
import com.echo.onlineshoppingsys.activity.MessageActivity;
import com.echo.onlineshoppingsys.services.SmsService;
import com.echo.onlineshoppingsys.util.BankUtil;
import com.echo.onlineshoppingsys.view.XListView;
import com.echo.onlineshoppingsys.view.XListView.IXListViewListener;

public class MessageMainTabFragment extends Fragment {
	private View view;
	private XListView list;
	private LayoutInflater mInflater;
	
	public ThreadsAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		view = inflater.inflate(R.layout.fragment_msg, container, false);
		list = (XListView) view.findViewById(R.id.list_thread);
		adapter = new ThreadsAdapter();
		list.setPullLoadEnable(false);
		list.setPullRefreshEnable(true);
		list.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				MainActivity.list_threads = SmsService.getSession(getActivity().getContentResolver());
				SmsService.getCurrentMoney(getActivity().getContentResolver(), MainActivity.list_threads);
				if(MainActivity.currentMoney >= MainActivity.totalMoney)
					((MainActivity)getActivity()).createNotify();
				adapter.notifyDataSetChanged();
				list.stopRefresh();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"HH:mm:ss");
				Date d = new Date(System.currentTimeMillis());
				list.setRefreshTime(dateFormat.format(d));
				if(MainActivity.list_threads.size() == 0)
					view.findViewById(R.id.tv_no_msg_tip).setVisibility(View.VISIBLE);
				else
					view.findViewById(R.id.tv_no_msg_tip).setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
		list.setAdapter(adapter);
		if(MainActivity.list_threads.size() == 0)
			view.findViewById(R.id.tv_no_msg_tip).setVisibility(View.VISIBLE);
		else
			view.findViewById(R.id.tv_no_msg_tip).setVisibility(View.GONE);
		return view;
	}
	
	public class ThreadsAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MainActivity.list_threads.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return MainActivity.list_threads.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = new ViewHolder();
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.list_threads_item, null);
				viewHolder.conv_name = ((TextView)convertView.findViewById(R.id.conv_name));
				viewHolder.conv_note = ((TextView)convertView.findViewById(R.id.conv_note));
				viewHolder.conv_date = ((TextView)convertView.findViewById(R.id.conv_date));
				viewHolder.conv_portrait = ((ImageView)convertView.findViewById(R.id.conv_portrait));
				
				viewHolder.conv_name.setText(MainActivity.list_threads.get(position).getName());
				viewHolder.conv_note.setText(MainActivity.list_threads.get(position).getSnippet());
				viewHolder.conv_date.setText(formatDate(MainActivity.list_threads.get(position).getDate()));
				setConvPortrait(viewHolder.conv_portrait, MainActivity.list_threads.get(position).getPhoneNum());
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
				viewHolder.conv_name.setText(MainActivity.list_threads.get(position).getName());
				viewHolder.conv_note.setText(MainActivity.list_threads.get(position).getSnippet());
				viewHolder.conv_date.setText(formatDate(MainActivity.list_threads.get(position).getDate()));
				setConvPortrait(viewHolder.conv_portrait, MainActivity.list_threads.get(position).getPhoneNum());
			}
			convertView.setOnClickListener(new MyConvertViewListener(position));
			return convertView;
		}
	}
	
	private void setConvPortrait(ImageView conv_portrait, String address) {
		if(address.equals(BankUtil.ICBC)) {
			conv_portrait.setImageResource(R.drawable.gsyh_icon);
			return;
		}
		if(address.equals(BankUtil.ABC)) {
			conv_portrait.setImageResource(R.drawable.nyyh_icon);
			return;
		}if(address.equals(BankUtil.BOCM)) {
			conv_portrait.setImageResource(R.drawable.jtyh_icon);
			return;
		}if(address.equals(BankUtil.CCB)) {
			conv_portrait.setImageResource(R.drawable.jsyh_icon);
			return;
		}if(address.equals(BankUtil.CMB)) {
			conv_portrait.setImageResource(R.drawable.zsyh_icon);
			return;
		}if(address.equals(BankUtil.PSBOC)) {
			conv_portrait.setImageResource(R.drawable.zgyz_icon);
			return;
		}if(address.equals(BankUtil.BOC)) {
			conv_portrait.setImageResource(R.drawable.zgyh_icon);
			return;
		}
		conv_portrait.setImageResource(R.drawable.icon_default_avatar);
	}
	
	private String formatDate(String date) {
		Time time = new Time("GMT+8");       
		time.setToNow();      
		int year = time.year;
		if(Integer.parseInt(date.split("-")[0]) == year)
			return date.substring(5, date.length());
		else
			return date;
	}
	
	private class MyConvertViewListener implements OnClickListener {
		int position;

		public MyConvertViewListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), MessageActivity.class);
			intent.putExtra("thread_id", MainActivity.list_threads.get(position).getId());
			intent.putExtra("msg_name", MainActivity.list_threads.get(position).getName());
			startActivity(intent);
		}
	}
	
	private static class ViewHolder {
		private TextView conv_name;
		private TextView conv_note;
		private TextView conv_date;
		private ImageView conv_portrait;
	}
}
