package com.wxk.leads.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

public class HomeActivity extends BaseActivity {

    private RevealFrameLayout reveal_layout;
    private RelativeLayout layout_sign_up;
    private RelativeLayout rl_title;
    private TextView tv_sign_up;
    private TextView tv_success;
    private View line;
    private ImageView im, iv_menu, iv_me;
    private FrameLayout scale_layout;

    @Override
    protected void setUpData(Bundle savedInstanceState) {

        setContentView(R.layout.activity_home);

        reveal_layout = (RevealFrameLayout) findViewById(R.id.reveal_layout);
        scale_layout = (FrameLayout) findViewById(R.id.scale_layout);
        layout_sign_up = (RelativeLayout) findViewById(R.id.layout_sign_up);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        tv_success = (TextView) findViewById(R.id.tv_success);
        line = findViewById(R.id.line_view);
        im = (ImageView) findViewById(R.id.bg_img);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_me = (ImageView) findViewById(R.id.iv_me);

        tv_sign_up.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                layout_sign_up.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                startRevel();
            }
        });
    }

    int measuredWidth;

    private void startRevel() {

        //得到successtextview的宽
        measuredWidth = tv_success.getMeasuredWidth();
        //拿到上一个activity中textview的属性
        Intent intent = getIntent();
        int left = intent.getIntExtra("left", 0);
        int top = intent.getIntExtra("top", 0);
        int width = intent.getIntExtra("width", 0);
        int height = intent.getIntExtra("height", 0);

        //获取到当前控件的属性
        int[] location = new int[2];
        layout_sign_up.getLocationOnScreen(location);
        int curX = location[0];
        int curY = location[1];
        //计算出差值
        int transX = left - curX;
        int transY = top - curY;
        //把当前的控件先移动到上一个activity中textview所处的位置
        layout_sign_up.setX(layout_sign_up.getX() + transX);
        layout_sign_up.setY(layout_sign_up.getY() + transY);

        int cx = left + width / 2;
        int cy = top - height / 2;
        float radius = (float) Math.hypot(reveal_layout.getWidth(), reveal_layout.getHeight());
        Animator animator;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            animator = ViewAnimationUtils.createCircularReveal(reveal_layout, cx, cy, 0, radius, View.LAYER_TYPE_HARDWARE);
            animator.setDuration(700);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    reveal_layout.setVisibility(View.VISIBLE);
                }
            });
            animator.start();
//        } else {


//        }

        Point start = new Point(layout_sign_up.getX(), layout_sign_up.getY());
        Point end = new Point(curX, curY - getStatusBarHeight(this));
        ValueAnimator animator1 = ValueAnimator.ofObject(new PointEvaluator(), start, end);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                Point curPoint = (Point) animation.getAnimatedValue();

                layout_sign_up.setX(curPoint.getX());
                layout_sign_up.setY(curPoint.getY());
            }
        });
        animator1.setDuration(700);
        animator1.start();

        final ObjectAnimator lineAnim = ObjectAnimator.ofFloat(line, "ScaleX", 0f, 0.6f, 0.9f, 1f);
        lineAnim.setDuration(1500);
        lineAnim.setInterpolator(new DecelerateInterpolator());
        line.setPivotX(0);

        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                line.setVisibility(View.VISIBLE);
                lineAnim.start();
            }
        });

        lineAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                loopAnim();
            }
        });
    }

    int index = 0;

    private void loopAnim() {

        if (index % 2 == 0) {

            successEnter();
        } else {

            singupEnter();
        }
        index++;
    }

    //success进入
    private void successEnter() {

        if (tv_success.getVisibility() != View.VISIBLE) {
            tv_success.setVisibility(View.VISIBLE);
        }

        //得到success的宽度,然后设置起始位置以及终点位置
        ObjectAnimator sucessTran = ObjectAnimator.ofFloat(tv_success, "TranslationX", -measuredWidth * 1.2f, -measuredWidth * 0.2f, 0f);
        //透明度渐变从0-1
        ObjectAnimator sucessAlpha = ObjectAnimator.ofFloat(tv_success, "Alpha", 0.1f, 0.8f, 1f);

        //此处同理
        ObjectAnimator signupTran = ObjectAnimator.ofFloat(tv_sign_up, "TranslationX", 0, measuredWidth * 0.7f, measuredWidth * 1.5f);
        signupTran.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator signupAplha = ObjectAnimator.ofFloat(tv_sign_up, "Alpha", 1.0f, 1.0f, 0.4f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(sucessTran, sucessAlpha, signupTran, signupAplha);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.sendEmptyMessageDelayed(0, 800);
            }
        });
    }

    private void singupEnter() {

        ObjectAnimator signupTran = ObjectAnimator.ofFloat(tv_sign_up, "TranslationX", -measuredWidth * 1.2f, -measuredWidth * 0.2f, 0f);
        ObjectAnimator signupAplha = ObjectAnimator.ofFloat(tv_sign_up, "Alpha", 0.1f, 0.8f, 1f);

        ObjectAnimator successTran = ObjectAnimator.ofFloat(tv_success, "TranslationX", 0, measuredWidth * 0.7f, measuredWidth * 1.5f);
        successTran.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator successAlpha = ObjectAnimator.ofFloat(tv_success, "Alpha", 1.0f, 1.0f, 0.4f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(signupTran, signupAplha, successTran, successAlpha);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (index >= 3) {

                enterHome();
            } else {

                loopAnim();
            }
        }
    };

    private void enterHome() {

        im.setVisibility(View.VISIBLE);
        rl_title.setVisibility(View.VISIBLE);
        scale_layout.setVisibility(View.VISIBLE);
        reveal_layout.setVisibility(View.GONE);
        layout_sign_up.setVisibility(View.GONE);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(scale_layout.getHeight(), 0);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale_layout.getLayoutParams().height = (int) animation.getAnimatedValue();
                scale_layout.requestLayout();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator,
                ObjectAnimator.ofFloat(iv_me, "Alpha", 0.1f, 1.0f),
                ObjectAnimator.ofFloat(iv_menu, "Alpha", 0.1f, 1.0f));
        animatorSet.setDuration(1200);
        animatorSet.start();
    }

    private int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
