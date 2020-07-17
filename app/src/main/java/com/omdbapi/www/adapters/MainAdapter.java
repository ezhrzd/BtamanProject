package com.omdbapi.www.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.omdbapi.www.ApplicationCotexts;
import com.omdbapi.www.R;
import com.omdbapi.www.models.MainList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements View.OnClickListener {

    private List<MainList> mDataset;

    public MainAdapter(List<MainList> dataset) {
        mDataset = dataset;
    }

    private OnItemClicked onClick;


    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MainAdapter.ViewHolder holder, final int position) {
        holder.mTitle.setText(mDataset.get(position).getTitle());
        holder.mType.setText(mDataset.get(position).getType());
        holder.mYear.setText(mDataset.get(position).getYear());


        Picasso.with(ApplicationCotexts.context)
                .load(mDataset.get(position).getPoster())
                .placeholder(R.drawable.empty)
                .into(holder.mPic);

        (holder.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle, mType, mYear;
        public ImageView mPic;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.name_text);
            mType = (TextView) itemView.findViewById(R.id.type_text);
            mYear = (TextView) itemView.findViewById(R.id.year_text);
            mPic = (ImageView) itemView.findViewById(R.id.poster_image);
            cardView = (CardView) itemView.findViewById(R.id.cardview);

        }
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }
}
