package com.echo.onlineshoppingsys.model;

import android.graphics.Bitmap;

public class GuideSite {
	
	private String site_name;  //网站名称
	private String site_url;   //网址
	private Bitmap site_icon;  //网站icon
	
	public GuideSite(String site_name, String site_url, Bitmap site_icon) {
		super();
		this.site_name = site_name;
		this.site_url = site_url;
		this.site_icon = site_icon;
	}

	public String getSite_name() {
		return site_name;
	}
	
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	
	public String getSite_url() {
		return site_url;
	}
	
	public void setSite_url(String site_url) {
		this.site_url = site_url;
	}
	
	public Bitmap getSite_icon() {
		return site_icon;
	}
	
	public void setSite_icon(Bitmap site_icon) {
		this.site_icon = site_icon;
	}
}
