package com.splitfee.app.features.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.SplitViewParam;
import com.splitfee.app.utils.PriceUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huteri on 5/7/17.
 */

public class ExpenseSplitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SplitViewParam> data = new ArrayList<>();

    ExpenseSplitListener listener;
    private Context context;

    public ExpenseSplitAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PayerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense_split, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PayerViewHolder holder1 = (PayerViewHolder) holder;

        holder1.tvName.setText(data.get(position).getPerson().getName());
        holder1.tvAmount.setText(PriceUtil.showPrice(context, data.get(position).getAmount(), false));
        holder1.tvInitial.setText(data.get(position).getPerson().getName().substring(0, 1));

        if(data.get(position).getAmount().compareTo(BigDecimal.ZERO) > 0) {
            holder1.ivCheck.setImageResource(R.drawable.ic_check_circle);
            holder1.ivCheck.mutateBackground(true);
            holder1.ivCheck.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder1.ivCheck.setImageResource(android.R.color.transparent);
            holder1.ivCheck.mutateBackground(true);
            holder1.ivCheck.setBackgroundColor(Color.parseColor("#DBDCDE"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setListener(ExpenseSplitListener listener) {
        this.listener = listener;
    }

    public void setData(List<SplitViewParam> data) {
        this.data.clear();
        this.data.addAll(data);

        notifyDataSetChanged();
    }

    public class PayerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.iv_profile)
        RoundedImageView ivProfile;

        @BindView(R.id.tv_amount)
        TextView tvAmount;

        @BindView(R.id.tv_initial)
        TextView tvInitial;

        @BindView(R.id.iv_check)
        RoundedImageView ivCheck;

        public PayerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_amount)
        void tapTvAmount() {
            if (listener != null)
                listener.onClickExpense(getAdapterPosition());
        }

        @OnClick(R.id.fl_container)
        void tapFlContainer() {
            if(listener != null)
                listener.onClickContainer(getAdapterPosition());
        }
    }

    public interface ExpenseSplitListener {
        void onClickExpense(int position);

        void onClickContainer(int adapterPosition);
    }
}
