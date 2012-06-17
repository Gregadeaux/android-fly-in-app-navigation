package com.deaux.fan;

import com.gregadeaux.fan.R;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
			LayoutInflater inflater = LayoutInflater.from(getContext());
			
			inflater.inflate(main, mMainView);
			inflater.inflate(fan, mFanView);
			
			initAnimations(context);
		}
	}
	
	private void initAnimations(Context context) {
		Resources r = getResources();
		int width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
		int offset = (int) (width - px);
		
		openAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, offset,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);

		openAnimation.setDuration(1000);
		//openAnimation.setFillAfter(true);
		
		closeAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -offset,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);

		closeAnimation.setDuration(250);
		closeAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {}

			public void onAnimationRepeat(Animation animation) {}

			public void onAnimationEnd(Animation animation) {
				mFanView.setVisibility(GONE);
			}
		});
	}
	
	public void showMenu(boolean show) {
		if(show) {
			mFanView.setVisibility(VISIBLE);
			
			mMainView.startAnimation(openAnimation);
		}else {
			//mFanView.setVisibility(GONE);
			mMainView.startAnimation(closeAnimation);
		}
	}

}
