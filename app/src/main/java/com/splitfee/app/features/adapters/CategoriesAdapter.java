package com.splitfee.app.features.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.CategoryViewParam;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huteri on 5/7/17.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<CategoryViewParam> data = new ArrayList<>();
    private Context context;
    private CategoriesListener listener;
    private String selected = "Transport";

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder holder1 = (ItemViewHolder) holder;

        int drawablle = context.getResources().getIdentifier(data.get(position).getIcon(), "drawable", context.getPackageName());

        holder1.ivIcon.setImageResource(drawablle);
        holder1.tvName.setText(data.get(position).getName());

        if (data.get(position).getName().equalsIgnoreCase(selected)) {
            holder1.vgContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder1.ivIcon.setColorFilter(ContextCompat.getColor(context, android.R.color.white), PorterDuff.Mode.SRC_IN);
            holder1.tvName.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder1.vgContainer.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            holder1.ivIcon.setColorFilter(ContextCompat.getColor(context, android.R.color.black), PorterDuff.Mode.SRC_IN);
            holder1.tvName.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setListener(CategoriesListener listener) {
        this.listener = listener;
    }

    public void setData(List<CategoryViewParam> data) {
        this.data.clear();
        this.data.addAll(data);

        notifyDataSetChanged();
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.container)
        ViewGroup vgContainer;

        @BindView(R.id.iv_icon)
        AppCompatImageView ivIcon;

        @BindView(R.id.tv_title)
        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.tapCategory(data.get(getAdapterPosition()));

            selected = data.get(getAdapterPosition()).getName();

            notifyDataSetChanged();

        }
    }


    public interface CategoriesListener {
        void tapCategory(CategoryViewParam categoryViewParam);
    }
}
