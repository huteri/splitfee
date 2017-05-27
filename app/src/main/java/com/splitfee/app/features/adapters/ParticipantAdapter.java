package com.splitfee.app.features.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.PersonViewParam;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huteri on 4/12/17.
 */

public class ParticipantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<PersonViewParam> data = new ArrayList<>();

    public ParticipantAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).tvInitial.setText(data.get(position).getName().substring(0, 1));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<PersonViewParam> persons) {
        this.data.clear();
        this.data.addAll(persons);

        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        RoundedImageView ivPhoto;

        @BindView(R.id.tv_initial)
        TextView tvInitial;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ivPhoto.setBackgroundColor(ContextCompat.getColor(context, R.color.rounded_background));
            ivPhoto.mutateBackground(true);
        }
    }
}
