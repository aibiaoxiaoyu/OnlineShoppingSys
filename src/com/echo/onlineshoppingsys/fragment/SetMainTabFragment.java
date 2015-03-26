package com.echo.onlineshoppingsys.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.onlineshoppingsys.R;
import com.echo.onlineshoppingsys.activity.MainActivity;
import com.echo.onlineshoppingsys.util.DialogUtils;
import com.echo.onlineshoppingsys.view.DonutProgress;

public class SetMainTabFragment extends Fragment implements OnClickListener{
	private View view;
	private DonutProgress mDonutProgress;
	private TextView tv_current;
	private TextView tv_total;
	private TextView tv_total_tip;
	
	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
				case 0:
					initData();
					break;
				case 1:
					Toast.makeText(getActivity(), "上限金额不能小于消费金额!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_set, container, false);
		initView();
		initData();
		return view;
	}
	
	private void initView() {
		mDonutProgress = (DonutProgress) view.findViewById(R.id.dp_money_useage);
		tv_current = (TextView) view.findViewById(R.id.tv_current);
		tv_total = (TextView) view.findViewById(R.id.tv_total);
		tv_total_tip = (TextView) view.findViewById(R.id.tv_total_tip);
		
		view.findViewById(R.id.rl_set_total).setOnClickListener(this);
		view.findViewById(R.id.rl_set_about).setOnClickListener(this);
	}
	
	private void initData() {
		tv_current.setText(MainActivity.year+"年"+MainActivity.month+"月消费金额:"+MainActivity.currentMoney+"元");
		tv_total.setText(MainActivity.year+"年"+MainActivity.month+"月上限金额:"+MainActivity.totalMoney+"元");
		tv_total_tip.setText(MainActivity.totalMoney+"元");
		if(MainActivity.currentMoney >= MainActivity.totalMoney)
			mDonutProgress.setProgress(100);
		else
			mDonutProgress.setProgress((int)((MainActivity.currentMoney/MainActivity.totalMoney)*100));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.rl_set_total) {
			DialogUtils.getInputDialog(getActivity(), this).show();
		} else if(v.getId() == R.id.rl_set_about)
			DialogUtils.getAboutDialog(getActivity()).show();
	}
	
}
