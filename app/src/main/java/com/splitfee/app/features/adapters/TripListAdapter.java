package com.splitfee.app.features.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.splitfee.app.R;
import com.splitfee.app.data.usecase.viewparam.TripViewParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huteri on 4/11/17.
 */

public class TripListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<TripViewParam> data = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
    private ItemClickListener itemClickListener;


    public TripListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder holder1 = (ItemViewHolder) holder;

        holder1.tvTitle.setText(data.get(position).getName());
        holder1.tvSubtitle.setText(dateFormat.format(data.get(position).getCreatedAt()));

        holder1.participantAdapter.setData(data.get(position).getPersons());

        Glide.with(context).load(data.get(position).getCover()).apply(RequestOptions.placeholderOf(Color.parseColor("#eeeeee"))).into(holder1.ivImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<TripViewParam> data) {
        this.data.clear();
        this.data.addAll(data);

        notifyDataSetChanged();
    }

    public void addData(TripViewParam trip) {
        data.add(0, trip);
        notifyItemInserted(0);
    }

    public void refreshTrip(TripViewParam tripViewParam) {

        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).getId().contentEquals(tripViewParam.getId())) {
                data.set(i, tripViewParam);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ParticipantAdapter participantAdapter;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_subtitle)
        TextView tvSubtitle;

        @BindView(R.id.rv_participant)
        RecyclerView rvParticipant;

        @BindView(R.id.iv_image)
        ImageView ivImage;

        @BindView(R.id.iv_menu)
        ImageView ivMenu;

        @BindView(R.id.card_item)
        ViewGroup cardView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            participantAdapter = new ParticipantAdapter(context);

            rvParticipant.setLayoutManager(layoutManager);
            rvParticipant.setAdapter(participantAdapter);
        }

        @Override
        public void onClick(View v) {

            if (itemClickListener != null) {
                itemClickListener.onTripClick(data.get(getAdapterPosition()));
            }
        }

        @OnClick(R.id.iv_menu)
        void onMenuMoreClick() {
            PopupMenu menu = new PopupMenu(context, ivMenu, Gravity.RIGHT);
            menu.inflate(R.menu.menu_more_trip);
            menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_edit:

                        if(itemClickListener != null) {
                            itemClickListener.onMenuEdit(data.get(getAdapterPosition()));
                        }
                        break;
                }
                return true;
            });

            menu.show();
        }
    }

    public interface ItemClickListener {
        void onTripClick(TripViewParam trip);
        void onMenuEdit(TripViewParam tripViewParam);
    }
}
