package com.splitfee.app.features.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.Gravity;
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
import butterknife.OnClick;

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

        holder1.tvTime.setText(DateUtils.getRelativeTimeSpanString(context, expenseViewParam.getCreatedAt().getTime(), true));
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


    public void deleteExpense(int pos) {

        list.remove(pos);

        notifyItemRemoved(pos);
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

        @BindView(R.id.iv_menu)
        ImageView ivMenu;

        @BindView(R.id.tv_time)
        TextView tvTime;

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

        @OnClick(R.id.iv_menu)
        void tapMenuMore() {
            PopupMenu menu = new PopupMenu(context, ivMenu, Gravity.RIGHT);
            menu.inflate(R.menu.menu_more_expense);
            menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        listener.onExpenseDelete(getAdapterPosition(), list.get(getAdapterPosition()));
                        break;
                }
                return true;
            });

            menu.show();
        }
    }


    public interface ExpenseListener {
        void onExpenseClick(ExpenseViewParam expenseViewParam);

        void onExpenseDelete(int position, ExpenseViewParam expenseViewParam);
    }
}
