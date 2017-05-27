package com.splitfee.app.features.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.ExpenseViewParam;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;
import com.splitfee.app.data.usecase.viewparam.SplitViewParam;
import com.splitfee.app.utils.PriceUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huteri on 5/4/17.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    List<ExpenseViewParam> list = new ArrayList<>();
    private ExpenseListener listener;

    public ExpenseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ExpenseViewParam expenseViewParam = list.get(position);

        ItemViewHolder holder1 = (ItemViewHolder) holder;

        holder1.tvName.setText(expenseViewParam.getNote());
        holder1.tvAmount.setText(PriceUtil.showPrice(context, expenseViewParam.getAmount(), false));
        holder1.ivIcon.setImageResource(
                context.getResources().getIdentifier(expenseViewParam.getCategory().getIcon(), "drawable", context.getPackageName()));

        List<PersonViewParam> persons = new ArrayList<>();
        for (SplitViewParam splitViewParam : expenseViewParam.getPayers()) {
            if (splitViewParam.getAmount().compareTo(BigDecimal.ZERO) > 0)
                persons.add(splitViewParam.getPerson());
        }

        holder1.payersAdapter.setData(persons);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<ExpenseViewParam> data) {
        list.clear();
        list.addAll(data);

        notifyDataSetChanged();
    }

    public void setListener(ExpenseListener listener) {
        this.listener = listener;
    }

    public void addExpense(ExpenseViewParam expenseViewParam) {
        list.add(0, expenseViewParam);

        notifyItemInserted(0);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ParticipantAdapter payersAdapter;

        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_amount)
        TextView tvAmount;

        @BindView(R.id.rv_payers)
        RecyclerView rvPayers;

        public ItemViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            payersAdapter = new ParticipantAdapter(context);

            rvPayers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rvPayers.setAdapter(payersAdapter);

            view.setOnClickListener(v -> {
                if (listener != null)
                    listener.onExpenseClick(list.get(getAdapterPosition()));

            });
        }
    }


    public interface ExpenseListener {
        void onExpenseClick(ExpenseViewParam expenseViewParam);
    }
}
