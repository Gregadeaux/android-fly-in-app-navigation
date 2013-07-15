package com.deaux.fan;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class FanView extends RelativeLayout {
    private static final String TAG = "FanView";
    public static final int HORIZONTAL_FAN = 0;
    public static final int VERTICAL_FAN = 1;
    private int mOrientation = HORIZONTAL_FAN;
    private LinearLayout mMainView;
    private LinearLayout mFanView;
    private View mTintView;
    private float px;
    private FanAnimation openAnimation;
    private FanAnimation closeAnimation;
    private Animation alphaAnimation;
    private int animDur;
    private boolean fade;
    private List<FanViewListener> observers;
     /**
     * Context
     */
    private Context mContext = null;
    private boolean isClosing;

    public FanView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public FanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public FanView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }
    /**
     * Internal initialization.
     */
    private void init(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.FanView);
        px = ta.getDimension(R.styleable.FanView_menuSize, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200,
                        getResources().getDisplayMetrics()));
        mOrientation = ta.getInt(R.styleable.FanView_fanOrientation, HORIZONTAL_FAN);
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(mOrientation == HORIZONTAL_FAN ?
                R.layout.fan_view_horizontal : R.layout.fan_view_vertical, this);
        ta.recycle();

        animDur = 1000;
        fade = true;


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

    public void setOrientation(int orientation){
        switch(orientation){
        case HORIZONTAL_FAN:
        case VERTICAL_FAN:
            mOrientation = orientation;
            break;
            default:
                Log.w(TAG, "Orientation invalid, must be HORIZONTAL_FAN " +
                        "|| VERTICAL_FAN, setting to default HORIZONTAL_FAN");
                mOrientation = HORIZONTAL_FAN;
                break;
        }
    }
    public void setViews(int main, int fan) {
        mMainView = (LinearLayout) findViewById(R.id.appView);
        mFanView = (LinearLayout) findViewById(R.id.fanView);
        mTintView = findViewById(R.id.tintView);

        if (main != -1 && fan != -1) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            inflater.inflate(main, mMainView);
            inflater.inflate(fan, mFanView);
        }
    }

    public void setFragments(Fragment main, Fragment fan) {
        mMainView = (LinearLayout) findViewById(R.id.appView);
        mFanView = (LinearLayout) findViewById(R.id.fanView);
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

    public boolean isOpen() {
        return mFanView.getVisibility() == VISIBLE && !isClosing;
    }

    public void setAnimationDuration(int duration) {
        animDur = duration;
    }

    public void setFadeOnMenuToggle(boolean fade) {
        this.fade = fade;
    }

    public void setIncludeDropshadow(boolean include) {
        if (include) {
            findViewById(R.id.dropshadow).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.dropshadow).setVisibility(GONE);
        }
    }

    public void resetMargin() {
        LayoutParams params;
        params = (LayoutParams) mMainView.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
    }

    public void showMenu() {
        if (mFanView.getVisibility() == GONE || isClosing) {
            mFanView.setVisibility(VISIBLE);
            mTintView.setVisibility(VISIBLE);

            openAnimation = new FanAnimation(0, px, TypedValue.applyDimension(
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
            isClosing = false;
            notifyOpen();
        } else if (!isClosing && isOpen()) {
            closeAnimation = new FanAnimation(px, 0, 0,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -20,
                            getResources().getDisplayMetrics()), animDur);
            closeAnimation.setFillAfter(true);
            closeAnimation.setAnimationListener(new AnimationListener() {

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    mFanView.setVisibility(GONE);
                    mTintView.setVisibility(GONE);
                    isClosing = false;
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
            isClosing = true;
            notifyClose();
        }
    }

    private class FanAnimation extends Animation {
        /**
         * The layout of the main view
         */
        private LayoutParams mainLayoutParams;

        /**
         * The layout of the fan view
         */
        private LayoutParams fanLayoutParams;

        /**
         * The margin offset of the either left/right, {@link FanView.HORIZONTAL},
         * or bottom/top, {@link FanView.VERTICAL}, edges  for the main layout
         */
        private float mainStart, mainEnd;

        /**
         * The margin offset of the either left/right, {@link FanView.HORIZONTAL},
         * or bottom/top, {@link FanView.VERTICAL}, edges  for the fan layout
         */
        private float fanStart, fanEnd;

        /**
         * Construct the animation
         * @param mainFrom X||Y main view edge location at start of animation
         * @param mainTo X||Y main view edge location at end of animation
         * @param fanFrom X||Y fan view edge location at start of animation
         * @param fanTo X||Y fan view edge location at end of animation
         * @param duration The length of time of the animation in ms
         * @param orientation Define the orientation of fan out, either
         * 			{@link FanView.HORIZONTAL} or {@link FanView.VERTICAL}
         */
        public FanAnimation(float mainFrom, float mainTo, float fanFrom,
                float fanTo, int duration, int orientation) {
            setDuration(duration);
            mainStart = mainFrom;
            mainEnd = mainTo;
            mainLayoutParams = (LayoutParams) mMainView.getLayoutParams();

            fanStart = fanFrom;
            fanEnd = fanTo;
            fanLayoutParams = (LayoutParams) mFanView.getLayoutParams();
        }

        /**
         * Define the animation views and duration, passes orientation as null
         * to {@link #FanView(float, float, float, float, int, int)}
         */
        public FanAnimation(float mainFrom, float mainTo, float fanFrom,
                float fanTo, int duration) {
            this(mainFrom, mainTo, fanFrom, fanTo, duration, HORIZONTAL_FAN);
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            if (interpolatedTime < 1.0f) {
                // Applies a Smooth Transition that starts fast but ends slowly
                setEdgeMargins(interpolatedTime);
                invalFan();
            }
        }

        /**
         * Set the edges of the fan and main view during {@link #applyTransformation(float, Transformation)},
         * depending on the value of the orientation
         * @param interpolatedTime The fraction of time of the animation
         */
        private void setEdgeMargins(float interpolatedTime){
            switch(mOrientation){
            case VERTICAL_FAN:
                mainLayoutParams.bottomMargin = (int) (mainStart + ((mainEnd - mainStart) * (Math
                        .pow(interpolatedTime - 1, 5) + 1)));
                fanLayoutParams.bottomMargin = (int) (fanStart + ((fanEnd - fanStart) * (Math
                        .pow(interpolatedTime - 1, 5) + 1)));
                mainLayoutParams.topMargin = -mainLayoutParams.bottomMargin;
                break;
            case HORIZONTAL_FAN:
                default:
                    mainLayoutParams.leftMargin = (int) (mainStart + ((mainEnd - mainStart) * (Math
                            .pow(interpolatedTime - 1, 5) + 1)));
                    fanLayoutParams.leftMargin = (int) (fanStart + ((fanEnd - fanStart) * (Math
                            .pow(interpolatedTime - 1, 5) + 1)));
                    mainLayoutParams.rightMargin = -mainLayoutParams.leftMargin;
                    break;
            }
        }

        /**
         * Invalidate the child views during animation {@link #applyTransformation(float, Transformation)}
         */
        private void invalFan(){
            mMainView.requestLayout();
            mFanView.requestLayout();
        }
    }
}
