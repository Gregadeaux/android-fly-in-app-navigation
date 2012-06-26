package com.deaux.fan;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FanView extends RelativeLayout {

	private LinearLayout mMainView;
	private LinearLayout mFanView;
	private View mTintView;
	private float px;
	private FanAnimation openAnimation;
	private FanAnimation closeAnimation;
	private Animation alphaAnimation;
	private int animDur;
	private boolean fade;
	
	public FanView(Context context) {
		this(context, null);
	}
	
	public FanView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public FanView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		LayoutInflater.from(context).inflate(R.layout.fan_view, this, true);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FanView);
		
		px = a.getDimension(R.styleable.FanView_menuSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
		animDur = 1000;
		fade = true;
	}
	
	public void setViews(int main, int fan) {
		mMainView = (LinearLayout) findViewById(R.id.appView);
		mFanView = (LinearLayout) findViewById(R.id.fanView);
		mTintView = findViewById(R.id.tintView);

		if(main != -1 && fan != -1) {			
			LayoutInflater inflater = LayoutInflater.from(getContext());
			
			inflater.inflate(main, mMainView);
			inflater.inflate(fan, mFanView);
		}
	}
	
	public void setAnimationDuration(int duration) {
		animDur = duration;
	}
	
	public void setFadeOnMenuToggle(boolean fade) {
		this.fade = fade;
	}
	
	public void setIncludeDropshadow(boolean include) {
		if(include) {
			findViewById(R.id.dropshadow).setVisibility(VISIBLE);
		}else {
			findViewById(R.id.dropshadow).setVisibility(GONE);
		}
	}
	
	public void showMenu() {
		if(mFanView.getVisibility() == GONE) {
			mFanView.setVisibility(VISIBLE);
			mTintView.setVisibility(VISIBLE);
			
			openAnimation = new FanAnimation(0, px,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20, getResources().getDisplayMetrics()), 0, animDur);
			openAnimation.setFillAfter(true);
			
			if(fade) {
				alphaAnimation = new AlphaAnimation(0.8f, 0.0f);
				alphaAnimation.setDuration((int)0.75*animDur);
				alphaAnimation.setFillAfter(true);
				mTintView.startAnimation(alphaAnimation);
			} else {
				mTintView.setVisibility(GONE);
			}
			
			mMainView.startAnimation(openAnimation);
		}else {
			closeAnimation = new FanAnimation(px, 0, 0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20, getResources().getDisplayMetrics()), animDur);
			closeAnimation.setFillAfter(true);
			closeAnimation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {}

				public void onAnimationRepeat(Animation animation) {}

				public void onAnimationEnd(Animation animation) {
					mFanView.setVisibility(GONE);
					mTintView.setVisibility(GONE);
				}
			});
			
			if(fade) {
				alphaAnimation = new AlphaAnimation(0.0f, 0.8f);
				alphaAnimation.setDuration(750);
				alphaAnimation.setFillAfter(true);
				mTintView.startAnimation(alphaAnimation);
			} else {
				mTintView.setVisibility(GONE);
			}
			mMainView.startAnimation(closeAnimation);
		}
	}
	
	private class FanAnimation extends Animation {
        private LayoutParams mainLayoutParams, fanLayoutParams;

        private float mainStartX, mainEndX;
        private float fanStartX, fanEndX;

        public FanAnimation(float fromX, float toX, float fanFromX, float fanToX, int duration) {
            setDuration(duration);
            mainEndX = toX;
            mainStartX = fromX;
            mainLayoutParams = (LayoutParams) mMainView.getLayoutParams();
            
            fanStartX = fanFromX;
            fanEndX = fanToX;
            fanLayoutParams = (LayoutParams) mFanView.getLayoutParams();
        }
		
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
		    super.applyTransformation(interpolatedTime, t);

		    if (interpolatedTime < 1.0f) {
		    	// Applies a Smooth Transition that starts fast but ends slowly
		    	mainLayoutParams.leftMargin = (int) ( mainStartX + ((mainEndX - mainStartX) * (Math.pow(interpolatedTime - 1, 5)+1)));
		    	fanLayoutParams.leftMargin = (int) ( fanStartX + ((fanEndX - fanStartX) * (Math.pow(interpolatedTime - 1, 5)+1)));
		    	mainLayoutParams.rightMargin = -mainLayoutParams.leftMargin;
		    	
		    	mMainView.requestLayout();
		    	mFanView.requestLayout();
		    }
		}
	}

}
