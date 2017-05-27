package com.splitfee.app.features.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;
import com.splitfee.app.features.summarydebts.SummaryDebtsFragment;
import com.splitfee.app.features.summarydetail.SummaryDetailFragment;

import java.util.List;

/**
 * Created by huteri on 5/20/17.
 */

public class SummaryPagerAdapter extends FragmentStatePagerAdapter {

    private static final CharSequence[] TITLES = {"Detail", "Debts"};
    private List<SummaryDetailViewParam> summaryDetail;
    private List<SummaryDebtsViewParam> summaryDebts;

    public SummaryPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SummaryDetailFragment.newInstance(summaryDetail);
            case 1:
                return SummaryDebtsFragment.newInstance(summaryDebts);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setData(List<SummaryDetailViewParam> summaryDetail, List<SummaryDebtsViewParam> summaryDebts) {
        this.summaryDetail = summaryDetail;
        this.summaryDebts = summaryDebts;

        notifyDataSetChanged();
    }
}
