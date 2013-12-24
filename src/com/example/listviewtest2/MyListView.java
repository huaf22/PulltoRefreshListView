package com.example.listviewtest2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyListView extends ListView {
	private float myLastY = -1;
	private boolean isScroll = false;
	private RefreshListener listener = null;
	private ImageView head_image = null;
	private ProgressBar head_progress = null;
	private View headView = null;
	private boolean isFirst = true;

	@SuppressLint("NewApi")
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//设置不能超出滑动
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		// 添加滑动头
		headView = inflate(context, R.layout.list_head, null);
		head_image = (ImageView) headView.findViewById(R.id.image);
		head_progress = (ProgressBar) headView.findViewById(R.id.bar);
		headView.setLayoutParams(new android.widget.AbsListView.LayoutParams(0,
				0));
		headView.setVisibility(View.GONE);
		addHeaderView(headView);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 设置阀值为listview高度的2/3
		float Count = (float) (getHeight() * (2.0 / 3));
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获得第一个点的x坐标
			myLastY = ev.getY(0);
			// 按下时判断listview是否在最顶端
			if (getFirstVisiblePosition() == 0) {
				isFirst = true;
			} else {
				isFirst = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// 得到手指向下滑动的距离
			float deltaY = ev.getY(ev.getPointerCount() - 1) - myLastY;
			if (isFirst) {
				// 当滑动状态为下滑时
				if (deltaY > 0) {
					// 设置list的头可见
					headView.setVisibility(View.VISIBLE);
					headView.setLayoutParams(new android.widget.AbsListView.LayoutParams(
							android.widget.AbsListView.LayoutParams.MATCH_PARENT,
							10));
					// 根据下滑的长度设置蓝色长条的长度
					android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(
							(int) ((getWidth() / Count) * deltaY), 10);
					head_image.setLayoutParams(params);
					head_image.setX((getWidth() - head_image.getWidth()) / 2);
				}
				// 当下滑距离大于阀值时显示不定长的横向进度条并且调用接口
				if (deltaY > Count) {
					// 显示进度条
					head_image.setVisibility(View.GONE);
					head_progress.setVisibility(View.VISIBLE);
					head_progress.setIndeterminate(true);
					//
					if (!isScroll) {
						if (listener != null) {
							// 调用接口
							listener.handle(handler);
						}
						isScroll = true;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (isScroll) {
				isScroll = false;
			} else {
				headView.setVisibility(View.GONE);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	// 当刷新操作完毕,调用handler.sendEmptyMessage(0)方法隐藏进度条
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 隐藏headView
			head_progress.setVisibility(View.GONE);
			head_image.setVisibility(View.VISIBLE);
			headView.setVisibility(View.GONE);
			Toast.makeText(getContext(), "refresh  ok", 200).show();
		}
	};

	public void setRefreshListener(RefreshListener mlistener) {
		listener = mlistener;
	}

	public interface RefreshListener {
		public void handle(Handler handler);
	}
}
