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
		//���ò��ܳ�������
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		// ��ӻ���ͷ
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
		// ���÷�ֵΪlistview�߶ȵ�2/3
		float Count = (float) (getHeight() * (2.0 / 3));
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��õ�һ�����x����
			myLastY = ev.getY(0);
			// ����ʱ�ж�listview�Ƿ������
			if (getFirstVisiblePosition() == 0) {
				isFirst = true;
			} else {
				isFirst = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// �õ���ָ���»����ľ���
			float deltaY = ev.getY(ev.getPointerCount() - 1) - myLastY;
			if (isFirst) {
				// ������״̬Ϊ�»�ʱ
				if (deltaY > 0) {
					// ����list��ͷ�ɼ�
					headView.setVisibility(View.VISIBLE);
					headView.setLayoutParams(new android.widget.AbsListView.LayoutParams(
							android.widget.AbsListView.LayoutParams.MATCH_PARENT,
							10));
					// �����»��ĳ���������ɫ�����ĳ���
					android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(
							(int) ((getWidth() / Count) * deltaY), 10);
					head_image.setLayoutParams(params);
					head_image.setX((getWidth() - head_image.getWidth()) / 2);
				}
				// ���»�������ڷ�ֵʱ��ʾ�������ĺ�����������ҵ��ýӿ�
				if (deltaY > Count) {
					// ��ʾ������
					head_image.setVisibility(View.GONE);
					head_progress.setVisibility(View.VISIBLE);
					head_progress.setIndeterminate(true);
					//
					if (!isScroll) {
						if (listener != null) {
							// ���ýӿ�
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

	// ��ˢ�²������,����handler.sendEmptyMessage(0)�������ؽ�����
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ����headView
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
