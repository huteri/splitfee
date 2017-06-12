package com.splitfee.app.features.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.SummaryDebtsViewParam;
import com.splitfee.app.utils.PriceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huteri on 5/23/17.
 */

public class SummaryDebtsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SummaryDebtsViewParam> data = new ArrayList<>();
    private Context context;

    public SummaryDebtsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_debts, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder holder1 = (ItemViewHolder) holder;

        SummaryDebtsViewParam debt = data.get(position);
        holder1.tvInitial.setText(debt.getPayer().getName().substring(0, 1).toUpperCase());
        holder1.tvPayer.setText(debt.getPayer().getName());
        holder1.tvReceiver.setText(debt.getReceiver().getName());
        holder1.tvAmpunt.setText(PriceUtil.showPrice(context, debt.getAmount(), false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SummaryDebtsViewParam> data) {
        this.data.clear();
        this.data.addAll(data);

        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_payer)
        TextView tvPayer;

        @BindView(R.id.tv_receiver)
        TextView tvReceiver;

        @BindView(R.id.tv_initial)
        TextView tvInitial;

        @BindView(R.id.tv_amount)
        TextView tvAmpunt;

        public ItemViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }
}
