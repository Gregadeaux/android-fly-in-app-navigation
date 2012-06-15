package com.deaux.fan;

import com.gregadeaux.fan.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FanView extends RelativeLayout {

	private LinearLayout mMainView;
	private LinearLayout mFanView;
	private Context context;
	private TranslateAnimation openAnimation;
	private TranslateAnimation closeAnimation;
	
	public FanView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public FanView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public FanView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.context = context;
		LayoutInflater.from(getContext()).inflate(R.layout.fan_view, this, true);
	}
	
	public void setViews(int main, int fan) {
		mMainView = (LinearLayout) findViewById(R.id.appView);
		mFanView = (LinearLayout) findViewById(R.id.fanView);

		if(main != -1 && fan != -1) {
			ViewGroup parent = (ViewGroup) findViewById(R.id.layout);
			
			System.out.println("HERE I AM");
			LayoutInflater inflater = LayoutInflater.from(getContext());
			
			inflater.inflate(main, mMainView);
			inflater.inflate(fan, mFanView);
			
			//mMainView.setVisibility(VISIBLE);
			//mFanView.setVisibility(INVISIBLE);
			initAnimations(context);
		}
	}
	
	private void initAnimations(Context context) {
		int width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		int offset = (width - 200);
		
		openAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, offset,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);

		openAnimation.setDuration(250);
		openAnimation.setFillAfter(true);
		
		closeAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -offset,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);

		closeAnimation.setDuration(250);
		closeAnimation.setFillAfter(true);
		closeAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				mFanView.setVisibility(GONE);
			}
		});
	}
	
	public void showMenu(boolean show) {
		if(show) {
			mFanView.setVisibility(VISIBLE);
			
			//mMainView.startAnimation(openAnimation);
			//mMainView.
		}else {
			mFanView.setVisibility(GONE);
			//mMainView.startAnimation(closeAnimation);
		}
	}

}
