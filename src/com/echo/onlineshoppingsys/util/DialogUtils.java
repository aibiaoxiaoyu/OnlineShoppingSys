package com.echo.onlineshoppingsys.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.echo.onlineshoppingsys.R;
import com.echo.onlineshoppingsys.activity.MainActivity;
import com.echo.onlineshoppingsys.fragment.SetMainTabFragment;

public class DialogUtils {
	
	public static Dialog getLoadingDialog(Activity activity, String msg, boolean cancelable, final DialogInterface.OnCancelListener cancelEvent) {
		final Dialog dialog = new Dialog(activity, R.style.Theme_CustomDialog);
		
		View contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null);
		
		ImageView aniImage = (ImageView)contentView.findViewById(R.id.ani_img);
		TextView msgView = (TextView)contentView.findViewById(R.id.message);
		
		Animation ani = AnimationUtils.loadAnimation(activity, R.anim.loading_ani);
		aniImage.startAnimation(ani);
		if(TextUtils.isEmpty(msg)) {
			msgView.setVisibility(View.GONE);
		}
		else {
			msgView.setVisibility(View.VISIBLE);
			msgView.setText(msg);
		}
		
		if(cancelable) {
			if(cancelEvent != null) {
				dialog.setOnCancelListener(cancelEvent);
			}
		}
		else {
			dialog.setCancelable(false);
		}
		
		dialog.setContentView(contentView);
		return dialog;
	}
	
	public static Dialog getInputDialog(final Activity activity, final SetMainTabFragment fragment) {
		final Dialog dialog = new Dialog(activity, R.style.Theme_CustomDialog);
		final View dialogview = LayoutInflater.from(activity).inflate(
				R.layout.dialog_input, null);
		dialog.setContentView(dialogview);

		final EditText etSubjectView = (EditText) dialogview
				.findViewById(R.id.et_subject);
		
		etSubjectView.setText(MainActivity.totalMoney+"");
		etSubjectView.setSelection(etSubjectView.getText().toString().length());

		Button okButton = (Button)dialogview.findViewById(R.id.dialog_ok_btn);

		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(etSubjectView.getText().equals("")) {
					dialog.dismiss();
				} else {
					if(Integer.parseInt(etSubjectView.getText().toString()) < MainActivity.currentMoney) {
						fragment.mHandler.sendEmptyMessage(1);
					} else {
						SharedPreferencesUtils.saveTotalMoney(activity, Integer.parseInt(etSubjectView.getText().toString()));
						fragment.mHandler.sendEmptyMessage(0);
						dialog.dismiss();
					}
				}
			}
		});

		final Button cancelButton = (Button) dialogview
				.findViewById(R.id.dialog_cancel_btn);

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(true);
		return dialog;
	}
	
	public static Dialog getAboutDialog(final Activity activity) {
		final Dialog dialog = new Dialog(activity, R.style.Theme_CustomDialog);
		final View dialogview = LayoutInflater.from(activity).inflate(
				R.layout.dialog_about, null);
		dialog.setContentView(dialogview);

		Button okButton = (Button)dialogview.findViewById(R.id.dialog_ok_btn);

		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.setCancelable(true);
		return dialog;
	}
}
