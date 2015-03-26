package com.echo.onlineshoppingsys.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.echo.onlineshoppingsys.R;

public class SiteBrowserActivity extends Activity {
	
	private WebView mWebView;
	private ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_site);
		
		initView();
		initListener();
	}
	
	private void initView() {
		mWebView = (WebView) findViewById(R.id.wv_site);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_web);
		
		// 设置WebView属性，能够执行Javascript脚本
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
				return false;
				
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				if(mProgressBar.getVisibility() == View.GONE)
					mProgressBar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				mProgressBar.setVisibility(View.GONE);
			}
			
			
		});
		mWebView.clearCache(true);
		mWebView.clearHistory();
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.loadUrl(getIntent().getStringExtra("site_url"));
		
		((TextView) findViewById(R.id.tv_tab_site_back)).setText(getIntent().getStringExtra("site_name"));
	}
	
	private void initListener() {
		findViewById(R.id.back_tab_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goBack();
			}
		});
		
		findViewById(R.id.forward_tab_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.goForward();
			}
		});
		
		findViewById(R.id.refresh_tab_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWebView.reload();
			}
		});
	}
	
	public void doBack(View v) {
		finish();
	}
}
