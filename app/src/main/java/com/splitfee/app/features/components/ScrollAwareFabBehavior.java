package com.splitfee.app.features.components;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.splitfee.app.R;

/**
 * Created by huteri on 1/24/17.
 */

public class ScrollAwareFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private int toolbarHeight = 0;
    private int totalDy;
    private boolean isTransitioning = false;

    public ScrollAwareFabBehavior() {
    }

    public ScrollAwareFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dx, int dy, int[] consumed) {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        int distanceToScroll = child.getHeight() + fabBottomMargin;

        if (dy > 0 && totalDy < 0 || dy < 0 && totalDy > 0) {
            totalDy = 0;
        }

        totalDy += dy;
        if (totalDy > 1 && !isTransitioning) {
            child.animate().translationY(distanceToScroll).setInterpolator(new AccelerateInterpolator(5)).setListener(getAnimationListener());
        } else if (totalDy < 0 && !isTransitioning) {
            child.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
        }
    }

    private Animator.AnimatorListener getAnimationListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isTransitioning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isTransitioning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isTransitioning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }
}
