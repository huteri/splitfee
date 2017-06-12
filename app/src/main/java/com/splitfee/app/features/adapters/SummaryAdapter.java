package com.splitfee.app.features.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.SummaryDetailViewParam;
import com.splitfee.app.utils.PriceUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huteri on 5/20/17.
 */

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<SummaryDetailViewParam> list = new ArrayList<>();
    private Context context;

    public SummaryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder holder1 = (ItemViewHolder) holder;

        SummaryDetailViewParam data = list.get(position);
        holder1.tvInitial.setText(data.getPerson().getName().substring(0, 1).toUpperCase());
        holder1.tvName.setText(data.getPerson().getName());
        holder1.tvDetail.setText("Paid: " + PriceUtil.showPrice(context, data.getPaid(), false) + " Spent: " + PriceUtil.showPrice(context, data.getSpent(), false));
        holder1.tvSummary.setText(PriceUtil.showPrice(context, data.getSummary(), false));

        if (data.getSummary().compareTo(BigDecimal.ZERO) > 0) {
            holder1.tvSummary.setTextColor(ContextCompat.getColor(context, R.color.price_positive));
        } else {
            holder1.tvSummary.setTextColor(ContextCompat.getColor(context, R.color.price_negative));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<SummaryDetailViewParam> list) {
        this.list.clear();
        this.list.addAll(list);

        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_detail)
        TextView tvDetail;

        @BindView(R.id.tv_initial)
        TextView tvInitial;

        @BindView(R.id.tv_summary)
        TextView tvSummary;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

