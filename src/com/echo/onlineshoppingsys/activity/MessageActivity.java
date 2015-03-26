package com.echo.onlineshoppingsys.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo.onlineshoppingsys.R;
import com.echo.onlineshoppingsys.model.Status;
import com.echo.onlineshoppingsys.services.SmsService;
import com.echo.onlineshoppingsys.util.BankUtil;
import com.echo.onlineshoppingsys.view.XListView;
import com.echo.onlineshoppingsys.view.XListView.IXListViewListener;

public class MessageActivity extends Activity {
	private XListView list;
	
	private List<Status> status;
	private LayoutInflater mInflater;
	private StatusAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		
		initView();
		initData();
		initListener();
	}
	
	private void initView() {
		list = (XListView) findViewById(R.id.list_msg);
		list.setPullLoadEnable(false);
		list.setPullRefreshEnable(true);
		status = new ArrayList<Status>();
		mInflater = LayoutInflater.from(this);
		mAdapter = new StatusAdapter();
		
		((TextView)findViewById(R.id.tv_tab_msg_back)).setText(getIntent().getStringExtra("msg_name"));
	}
	
	private void initData() {
		status = SmsService.getSmsInphone(getContentResolver(), getIntent().getIntExtra("thread_id", 0));
		list.setAdapter(mAdapter);
	}
	
	private void initListener() {
		list.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				status = SmsService.getSmsInphone(getContentResolver(), getIntent().getIntExtra("thread_id", 0));
				mAdapter.notifyDataSetChanged();
				list.stopRefresh();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"HH:mm:ss");
				Date d = new Date(System.currentTimeMillis());
				list.setRefreshTime(dateFormat.format(d));
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void doBack(View v) {
		finish();
	}
	
	private class StatusAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return status.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return status.get(position);
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
				convertView = mInflater.inflate(R.layout.list_msg_item, null);
				viewHolder.msg_content = ((TextView)convertView.findViewById(R.id.tv_msg_incoming));
				viewHolder.msg_date = ((TextView)convertView.findViewById(R.id.im_item_date_other));
				viewHolder.msg_portrait = ((ImageView)convertView.findViewById(R.id.im_item_photo_other));
				
				viewHolder.msg_content.setText(status.get(position).getContent());
				viewHolder.msg_date.setText(status.get(position).getLastReceive());
				setConvPortrait(viewHolder.msg_portrait, status.get(position).getPhoneNum());
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
				viewHolder.msg_content.setText(status.get(position).getContent());
				viewHolder.msg_date.setText(status.get(position).getLastReceive());
				setConvPortrait(viewHolder.msg_portrait, status.get(position).getPhoneNum());
			}
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
	
	private static class ViewHolder {
		private TextView msg_content;
		private TextView msg_date;
		private ImageView msg_portrait;
	}
}
