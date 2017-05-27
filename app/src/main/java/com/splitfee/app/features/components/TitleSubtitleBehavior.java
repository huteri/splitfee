package com.splitfee.app.features.components;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.splitfee.app.R;

/**
 * Created by huteri on 1/11/17.
 */

public class TitleSubtitleBehavior extends CoordinatorLayout.Behavior<TwoLinesTextToolbar> {


    private final Context mContext;
    private float startMarginBottom;
    private float endMarginLeft;
    private int startMarginLeft;
    private boolean isHide;
    private int marginRight;

    public TitleSubtitleBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TwoLinesTextToolbar child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TwoLinesTextToolbar child, View dependency) {
        shouldInitProperties(child, dependency);

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

        float childPosition = dependency.getHeight()
                + dependency.getY()
                - child.getHeight()
                - (getToolbarHeight() - child.getHeight()) * percentage / 2;


        childPosition = childPosition - startMarginBottom * (1f - percentage);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.leftMargin = (int) (percentage * endMarginLeft) + startMarginLeft;
        lp.rightMargin = marginRight;
        child.setLayoutParams(lp);

        child.setY(childPosition);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (isHide && percentage < 1) {
                child.setVisibility(View.VISIBLE);
                isHide = false;
            } else if (!isHide && percentage == 1) {
                child.setVisibility(View.GONE);
                isHide = true;
            }
        }
        return true;
    }


    private void shouldInitProperties(TwoLinesTextToolbar child, View dependency) {

        if (startMarginLeft == 0)
            startMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_left);

        if (endMarginLeft == 0)
            endMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_left);

        if (startMarginBottom == 0)
            startMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom);

        if (marginRight == 0)
            marginRight = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_right);

    }


    public int getToolbarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        return result;
    }
}
