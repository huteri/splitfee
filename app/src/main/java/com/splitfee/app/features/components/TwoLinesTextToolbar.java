package com.splitfee.app.features.components;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.splitfee.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huteri on 1/11/17.
 */
public class TwoLinesTextToolbar extends LinearLayout {


    @BindView(R.id.header_view_title)
    TextView title;

    @BindView(R.id.header_view_sub_title)
    TextView subTitle;

    public TwoLinesTextToolbar(Context context) {
        super(context);
    }

    public TwoLinesTextToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoLinesTextToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TwoLinesTextToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String title) {
        bindTo(title, "");
    }

    public void bindTo(String title, String subTitle) {
        hideOrSetText(this.title, title);
        hideOrSetText(this.subTitle, subTitle);
    }

    private void hideOrSetText(TextView tv, String text) {
        if (text == null || text.equals(""))
            tv.setVisibility(GONE);
        else
            tv.setText(text);
    }
}
