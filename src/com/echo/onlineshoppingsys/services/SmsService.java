package com.echo.onlineshoppingsys.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.echo.onlineshoppingsys.activity.MainActivity;
import com.echo.onlineshoppingsys.model.Status;
import com.echo.onlineshoppingsys.model.Threads;
import com.echo.onlineshoppingsys.util.BankUtil;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class SmsService {
	
	private static final String SMS_URI_ALL = "content://sms/"; 
	private static final String SMS_ADDRESS_URI = "content://mms-sms/canonical-addresses";
	private static final String SMS_THREADS_URI = "content://mms-sms/conversations?simple=true";
	
	//根据电话号码获取本地通讯录名称
	public static String getNameByPhone(ContentResolver cr, String phoneNum) {
		Cursor cursor = cr.query(Uri.withAppendedPath(
				PhoneLookup.CONTENT_FILTER_URI, phoneNum), new String[] {
			PhoneLookup._ID,
			PhoneLookup.NUMBER,
			PhoneLookup.DISPLAY_NAME,
			PhoneLookup.TYPE, PhoneLookup.LABEL}, null, null,   null );
		
		if(cursor.getCount() == 0) {
			//没找到电话号码
			return phoneNum;
		} else if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			return cursor.getString(2); //获取姓名
		}
		return phoneNum;
	}
	
	//根据会话id获取会话的所有短信实体
	public static List<Status> getSmsInphone(ContentResolver cr, Integer thread_id) {
		List<Status> sms_list = new ArrayList<Status>();
		
		String[] projection = new String[] { "_id", "address",
				"body", "date", "type" };
		Uri uri = Uri.parse(SMS_URI_ALL);
		Cursor cursor = cr.query(uri, projection, "thread_id=?",
				new String[] { Integer.toString(thread_id) }, "date desc");
		if (cursor.moveToFirst()) {
			String phoneNumber;
			String smsBody;
			String date;
			int phoneNumberColumn = cursor.getColumnIndex("address");
			int smsBodyColumn = cursor.getColumnIndex("body");
			int dateColumn = cursor.getColumnIndex("date");
			int typeColumn = cursor.getColumnIndex("type");
			do {
				int typeId = cursor.getInt(typeColumn);
				if (typeId == 1) {
					Status status = new Status();
					//name = cursor.getString(nameColumn);
					phoneNumber = cursor.getString(phoneNumberColumn);
					smsBody = cursor.getString(smsBodyColumn);
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date d = new Date(Long.parseLong(cursor.getString(dateColumn)));
					date = dateFormat.format(d);
					if (smsBody == null) {
						smsBody = "";
					}
					status.setPhoneNum(phoneNumber);
					status.setContent(smsBody);
					status.setLastReceive(date);
					status.setPerson(getNameByPhone(cr, phoneNumber));
					sms_list.add(status);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return sms_list;
	}
	
	//获取本地所有短信会话
	public static List<Threads> getSession(ContentResolver cr) {
		String[] projection = new String[] { "_id", "date", "recipient_ids",
		"snippet"};
		Cursor cursor = cr.query(Uri.parse(SMS_THREADS_URI),
				projection, null, null, "date desc");
		ArrayList<Threads> list = new ArrayList<Threads>();
		if (cursor.moveToFirst()) {
			int idColumn = cursor.getColumnIndex("_id");
			int dateColumn = cursor.getColumnIndex("date");
			int recipientColumn = cursor.getColumnIndex("recipient_ids");
			int snippetColumn = cursor.getColumnIndex("snippet");
			do {
				Threads	threads = new Threads();
				threads.setId(cursor.getInt(idColumn));
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				Date d = new Date(Long.parseLong(cursor.getString(dateColumn)));
				threads.setDate(dateFormat.format(d));
				String[] proj = new String[] {"_id", "address"};
				Cursor cur = cr.query(Uri.parse(SMS_ADDRESS_URI), proj, "_id=?",
						new String[] { Integer.toString(cursor.getInt(recipientColumn)) }, null);
				if(cur.moveToNext()) {
					threads.setPhoneNum(cur.getString(cur.getColumnIndex("address")));
					threads.setName(getNameByPhone(cr, cur.getString(cur.getColumnIndex("address"))));
				}
				threads.setSnippet(cursor.getString(snippetColumn));
				if(BankUtil.BANK_NUM.contains(cur.getString(cur.getColumnIndex("address")))) {
					list.add(threads);
				}
				cur.close();
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	
	//根据短信获取银行总支出金额
	public static void getCurrentMoney(ContentResolver cr, List<Threads> list) {
		MainActivity.currentMoney = 0;
		for(Threads threads : list) {
			List<Status> statues = getSmsInphone(cr, threads.getId());
			for(Status status : statues) {
				int year = 0;
				int month = 0;
				try {
					year = Integer.parseInt(status.getLastReceive().substring(0, 4));
					month = Integer.parseInt(status.getLastReceive().substring(5, 7));
				} catch (Exception e) {
					
				}
				if(MainActivity.year == year && MainActivity.month == month) {
					String content = status.getContent();
					if(content.contains("支出")) {
						if(content.contains(")")) {
							try{
								MainActivity.currentMoney += Float.parseFloat(content.substring(content.indexOf(")")+1, content.indexOf("元")));
							} catch (Exception e) {
								Log.d("bobo", "exception");
							}
						} else{
							if(content.contains("支出人民币")) {
								try{
									MainActivity.currentMoney += Float.parseFloat(content.substring(content.indexOf("支出人民币")+5, content.indexOf("元")));
								} catch (Exception e) {
									Log.d("bobo", "exception");
								}
							}
						}
					}
				}
			}
		}
	}
}

