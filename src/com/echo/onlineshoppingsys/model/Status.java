package com.echo.onlineshoppingsys.model;

public class Status {
	private String person;		//名字
	private String content;		//内容
	private String lastReceive; //日期
	private String phoneNum;    //电话
	
	public Status() {
		// TODO Auto-generated constructor stub
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLastReceive() {
		return lastReceive;
	}

	public void setLastReceive(String lastReceive) {
		this.lastReceive = lastReceive;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
}
