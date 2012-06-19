package com.deaux.fan;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FanView extends RelativeLayout {

	private LinearLayout mMainView;
	private LinearLayout mFanView;
	private float px;
	private int width;
	private FanAnimation openAnimation;
	private FanAnimation closeAnimation;
	
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
		
		px = a.getDimension(R.styleable.FanView_menuSize, 200);
	}
	
	public void setViews(int main, int fan) {
		mMainView = (LinearLayout) findViewById(R.id.appView);
		mFanView = (LinearLayout) findViewById(R.id.fanView);

		if(main != -1 && fan != -1) {			
			LayoutInflater inflater = LayoutInflater.from(getContext());
			
			inflater.inflate(main, mMainView);
			inflater.inflate(fan, mFanView);
		}
	}
	
	public void showMenu() {
		width = mMainView.getWidth();
		if(mFanView.getVisibility() == GONE) {
			mFanView.setVisibility(VISIBLE);
			openAnimation = new FanAnimation(0,px,1000,mMainView);
			openAnimation.setFillAfter(true);
			
			mMainView.startAnimation(openAnimation);
		}else {
			closeAnimation = new FanAnimation(px,0,1000,mMainView);
			closeAnimation.setFillAfter(true);
			closeAnimation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {}

				public void onAnimationRepeat(Animation animation) {}

				public void onAnimationEnd(Animation animation) {
					mFanView.setVisibility(GONE);
				}
			});
			mMainView.startAnimation(closeAnimation);
		}
	}
	
	private class FanAnimation extends Animation {
		private View mView;

        private LayoutParams mLayoutParams;

        private float startX, endX;

        public FanAnimation(float fromX, float toX, int duration, View view) {
            setDuration(duration);
            mView = view;
            endX = toX;
            startX = fromX;
            mLayoutParams = (LayoutParams) mView.getLayoutParams();
        }
		
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
		    super.applyTransformation(interpolatedTime, t);

		    if (interpolatedTime < 1.0f) {
		    	// Applies a Smooth Transition that starts fast but ends slowly
		    	mLayoutParams.leftMargin = (int) ( startX + ((endX - startX) * (Math.pow(interpolatedTime - 1, 5)+1)));
		    	mLayoutParams.rightMargin = -mLayoutParams.leftMargin;
		    	mView.requestLayout();
		    }
		}
	}

}
