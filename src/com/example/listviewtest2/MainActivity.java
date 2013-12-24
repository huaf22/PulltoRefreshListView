package com.example.listviewtest2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.listviewtest2.MyListView.RefreshListener;

public class MainActivity extends Activity {
	private Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyListView listView = (MyListView) findViewById(R.id.listView);

		String[] strings = { "0", "1", "1", "1", "1", "1", "1", "1", "1", "1",
				"1", "1", "1", "1", "1", "1" };
		listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_dropdown_item_1line, strings));

		listView.setRefreshListener(new RefreshListener() {
			@Override
			public void handle(Handler mhandler) {
				handler = mhandler;
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						handler.sendEmptyMessage(0);
					}
				}).start();
				
//				Toast.makeText(MainActivity.this, "main refresh...ll",
//						Toast.LENGTH_SHORT).show();
			}
		});

	}
}
