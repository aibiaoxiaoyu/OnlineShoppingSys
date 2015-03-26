package com.echo.onlineshoppingsys.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.echo.onlineshoppingsys.R;
import com.echo.onlineshoppingsys.activity.MainActivity;
import com.echo.onlineshoppingsys.activity.SiteBrowserActivity;

public class GuideMainTabFragment extends Fragment {
	private View view;
	private LayoutInflater inflater;
	private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.fragment_guide, container, false);
		list = (ListView) view.findViewById(R.id.list_guide);
		list.setAdapter(new SiteAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SiteBrowserActivity.class);
				intent.putExtra("site_url", MainActivity.list_sites.get(position).getSite_url());
				intent.putExtra("site_name", MainActivity.list_sites.get(position).getSite_name());
				startActivity(intent);
			}
			
		});
		return view;
	}
	
	private class SiteAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MainActivity.list_sites.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return MainActivity.list_sites.get(position);
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
				convertView = inflater.inflate(R.layout.list_guide_item, null, false);
				viewHolder.site_name = ((TextView) convertView.findViewById(R.id.site_name));
				viewHolder.site_icon = ((ImageView) convertView.findViewById(R.id.site_icon));
				
				viewHolder.site_icon.setImageBitmap(MainActivity.list_sites.get(position).getSite_icon());
				viewHolder.site_name.setText(MainActivity.list_sites.get(position).getSite_name());
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
				viewHolder.site_icon.setImageBitmap(MainActivity.list_sites.get(position).getSite_icon());
				viewHolder.site_name.setText(MainActivity.list_sites.get(position).getSite_name());
			}
			
			return convertView;
		}
	}
	
	private static class ViewHolder {
		private TextView site_name;
		private ImageView site_icon;
	}
	
}
