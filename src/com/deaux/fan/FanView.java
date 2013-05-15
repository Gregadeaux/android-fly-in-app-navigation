package com.deaux.fan;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
	private LinearLayout mLeftView;
	private LinearLayout mRightView;
	private View mTintView;
	private float px;
	private Animation openAnimation;
	private Animation closeAnimation;
	private Animation alphaAnimation;
	private int animDur;
	private boolean fade;
	private List<FanViewListener> observers;
	private LayoutInflater inflater;

	private boolean isLeftClosing;
	private boolean isRightClosing;

	public FanView(Context context) {
		this(context, null);
	}

	public FanView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FanView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutInflater.from(context).inflate(R.layout.fan_view, this, true);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.FanView);

		px = a.getDimension(R.styleable.FanView_menuSize, TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
						getResources().getDisplayMetrics()));
		animDur = 1000;
		fade = true;
		
		a.recycle();
		
		inflater = LayoutInflater.from(getContext());
	}
	
	public void addListener(FanViewListener l) {
		if(observers == null) {
			observers = new ArrayList<FanViewListener>();
		}
		observers.add(l);
	}
	
	private void notifyOpen() {
		if(observers != null) {
			for(FanViewListener l : observers) {
				l.onFanViewOpen();
			}
		}
	}
	
	private void notifyClose() {
		if(observers != null) {
			for(FanViewListener l : observers) {
				l.onFanViewClose();
			}
		}
	}
	
	public void setMainView(int main) {
		mMainView = (LinearLayout) findViewById(R.id.appView);
		mTintView = findViewById(R.id.tintView);
		if(main != -1) {
			inflater.inflate(main, mMainView);
		}
	}
	
	public void setLeftView(int left) {
		mLeftView = (LinearLayout) findViewById(R.id.fanView);
		if (left != -1) {
			inflater.inflate(left, mLeftView);
		}
	}
	
	public void setRightView(int right) {
		mRightView = (LinearLayout) findViewById(R.id.rightView);
		if (right != -1) {
			inflater.inflate(right, mRightView);
		}
	}

	public void setViews(int main, int fan) {
		mMainView = (LinearLayout) findViewById(R.id.appView);
		mLeftView = (LinearLayout) findViewById(R.id.fanView);

		if (main != -1 && fan != -1) {
			inflater.inflate(main, mMainView);
			inflater.inflate(fan, mLeftView);
		}
	}

	public void setFragments(Fragment main, Fragment fan) {
		mMainView = (LinearLayout) findViewById(R.id.appView);
		mLeftView = (LinearLayout) findViewById(R.id.fanView);
		mTintView = findViewById(R.id.tintView);

		FragmentManager mgr = ((FragmentActivity) getContext())
				.getSupportFragmentManager();
		mgr.beginTransaction().add(R.id.appView, main).commit();
		mgr.beginTransaction().add(R.id.fanView, fan).commit();
	}
	
	public void replaceMainFragment(Fragment replacement){
		FragmentManager mgr = ((FragmentActivity) getContext())
				.getSupportFragmentManager();
		mgr.beginTransaction().replace(R.id.appView, replacement).commit();
	}

	public boolean isLeftOpen() {
		return !(mLeftView == null) && (mLeftView.getVisibility() == VISIBLE && !isLeftClosing);
	}
	
	public boolean isRightOpen() {
		return !(mRightView == null) && (mRightView.getVisibility() == VISIBLE && !isRightClosing);
	}

	public void setAnimationDuration(int duration) {
		animDur = duration;
	}

	public void setFadeOnMenuToggle(boolean fade) {
		this.fade = fade;
	}

	public void setIncludeDropshadow(boolean include) {
		if (include) {
			findViewById(R.id.leftdropshadow).setVisibility(VISIBLE);
			findViewById(R.id.rightdropshadow).setVisibility(VISIBLE);
		} else {
			findViewById(R.id.leftdropshadow).setVisibility(GONE);
			findViewById(R.id.rightdropshadow).setVisibility(GONE);
		}
	}
	
	public void resetMargin() {
		LayoutParams params;
		params = (LayoutParams) mMainView.getLayoutParams();
		params.leftMargin = 0;
		params.rightMargin = 0;
	}

	public void showLeftMenu() {
		if (!isRightOpen() && (mLeftView.getVisibility() == GONE || isLeftClosing)) {
			mLeftView.setVisibility(VISIBLE);
			mTintView.setVisibility(VISIBLE);

			openAnimation = new LeftFanAnimation(0, px, TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, -20, getResources()
							.getDisplayMetrics()), 0, animDur);
			openAnimation.setFillAfter(true);

			if (fade) {
				alphaAnimation = new AlphaAnimation(0.8f, 0.0f);
				alphaAnimation.setDuration((int) 0.75 * animDur);
				alphaAnimation.setFillAfter(true);
				mTintView.startAnimation(alphaAnimation);
			} else {
				mTintView.setVisibility(GONE);
			}

			mMainView.startAnimation(openAnimation);
			isLeftClosing = false;
			notifyOpen();
		} else if (!isRightOpen() && !isLeftClosing && isLeftOpen()) {
			closeAnimation = new LeftFanAnimation(px, 0, 0,
					TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20,
							getResources().getDisplayMetrics()), animDur);
			closeAnimation.setFillAfter(true);
			closeAnimation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mLeftView.setVisibility(GONE);
					mTintView.setVisibility(GONE);
					isLeftClosing = false;
				}
			});

			if (fade) {
				alphaAnimation = new AlphaAnimation(0.0f, 0.8f);
				alphaAnimation.setDuration(750);
				alphaAnimation.setFillAfter(true);
				mTintView.startAnimation(alphaAnimation);
			} else {
				mTintView.setVisibility(GONE);
			}
			mMainView.startAnimation(closeAnimation);
			isLeftClosing = true;
			notifyClose();
		}
	}
	
	public void showRightMenu() {
		if (!isLeftOpen() && (mRightView.getVisibility() == GONE || isRightClosing)) {
			mRightView.setVisibility(VISIBLE);
			mTintView.setVisibility(VISIBLE);

			openAnimation = new RightFanAnimation(0, px, TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, -20, getResources()
							.getDisplayMetrics()), 0, animDur);
			openAnimation.setFillAfter(true);

			if (fade) {
				alphaAnimation = new AlphaAnimation(0.8f, 0.0f);
				alphaAnimation.setDuration((int) 0.75 * animDur);
				alphaAnimation.setFillAfter(true);
				mTintView.startAnimation(alphaAnimation);
			} else {
				mTintView.setVisibility(GONE);
			}

			mMainView.startAnimation(openAnimation);
			isRightClosing = false;
			notifyOpen();
		} else if (!isLeftOpen() && !isLeftClosing && isRightOpen()) {
			closeAnimation = new RightFanAnimation(px, 0, 0,
					TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20,
							getResources().getDisplayMetrics()), animDur);
			closeAnimation.setFillAfter(true);
			closeAnimation.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					mRightView.setVisibility(GONE);
					mTintView.setVisibility(GONE);
					isLeftClosing = false;
				}
			});

			if (fade) {
				alphaAnimation = new AlphaAnimation(0.0f, 0.8f);
				alphaAnimation.setDuration(750);
				alphaAnimation.setFillAfter(true);
				mTintView.startAnimation(alphaAnimation);
			} else {
				mTintView.setVisibility(GONE);
			}
			mMainView.startAnimation(closeAnimation);
			isLeftClosing = true;
			notifyClose();
		}
	}

	private class LeftFanAnimation extends Animation {
		private LayoutParams mainLayoutParams, fanLayoutParams;

		private float mainStartX, mainEndX;
		private float fanStartX, fanEndX;

		public LeftFanAnimation(float fromX, float toX, float fanFromX,
				float fanToX, int duration) {
			setDuration(duration);
			mainEndX = toX;
			mainStartX = fromX;
			mainLayoutParams = (LayoutParams) mMainView.getLayoutParams();

			fanStartX = fanFromX;
			fanEndX = fanToX;
			fanLayoutParams = (LayoutParams) mLeftView.getLayoutParams();
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);

			if (interpolatedTime < 1.0f) {
				// Applies a Smooth Transition that starts fast but ends slowly
				mainLayoutParams.leftMargin = (int) (mainStartX + ((mainEndX - mainStartX) * (Math
						.pow(interpolatedTime - 1, 5) + 1)));
				fanLayoutParams.leftMargin = (int) (fanStartX + ((fanEndX - fanStartX) * (Math
						.pow(interpolatedTime - 1, 5) + 1)));
				mainLayoutParams.rightMargin = -mainLayoutParams.leftMargin;

				mMainView.requestLayout();
				mLeftView.requestLayout();
			}
		}
	}
	
	private class RightFanAnimation extends Animation {
		private LayoutParams mainLayoutParams, fanLayoutParams;

		private float mainStartX, mainEndX;
		private float fanStartX, fanEndX;

		public RightFanAnimation(float fromX, float toX, float fanFromX,
				float fanToX, int duration) {
			setDuration(duration);
			mainEndX = toX;
			mainStartX = fromX;
			mainLayoutParams = (LayoutParams) mMainView.getLayoutParams();

			fanStartX = fanFromX;
			fanEndX = fanToX;
			fanLayoutParams = (LayoutParams) mRightView.getLayoutParams();
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);

			if (interpolatedTime < 1.0f) {
				// Applies a Smooth Transition that starts fast but ends slowly
				mainLayoutParams.rightMargin = (int) (mainStartX + ((mainEndX - mainStartX) * (Math
						.pow(interpolatedTime - 1, 5) + 1)));
				fanLayoutParams.rightMargin = (int) (fanStartX + ((fanEndX - fanStartX) * (Math
						.pow(interpolatedTime - 1, 5) + 1)));
				mainLayoutParams.leftMargin = -mainLayoutParams.rightMargin;

				mMainView.requestLayout();
				mRightView.requestLayout();
			}
		}
	}

}
