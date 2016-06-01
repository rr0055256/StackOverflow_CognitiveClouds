package com.stackoverflow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by raman on 1/6/16.
 */

public class StackoverflowAdapter extends RecyclerView.Adapter<StackoverflowAdapter.StackOverflowViewHolder> {
    Context context;
    List<Item> list;
    public StackoverflowAdapter(List<Item> list, Context context) {
        this.context = context;
        this.list=list;
    }

    @Override
    public StackOverflowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stackoverflow_card,parent,false);
        StackOverflowViewHolder stackOverflowViewHolder = new StackOverflowViewHolder(v);
        return stackOverflowViewHolder;
    }

    public static class StackOverflowViewHolder extends RecyclerView.ViewHolder{

//        @BindView(R.id.card_view)
        CardView cv;

//        @BindView(R.id.imageView)
        ImageView profileImage;

//        @BindView(R.id.question)
        TextView question;

//        @BindView(R.id.tags)
        TextView tagName;

//        @BindView(R.id.rating)
        TextView rating;

//        @BindView(R.id.like)
        Button  like;

//        @BindView(R.id.share)
        Button share;

        StackOverflowViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            profileImage = (ImageView) itemView.findViewById(R.id.imageView);
            question = (TextView) itemView.findViewById(R.id.question);
            tagName = (TextView) itemView.findViewById(R.id.tags);
            rating = (TextView) itemView.findViewById(R.id.rating);
            like = (Button) itemView.findViewById(R.id.like);
            share = (Button) itemView.findViewById(R.id.share);

        }

    }

    @Nullable
    @Override
    public void onBindViewHolder(StackOverflowViewHolder holder, int position) {
        Item item = list.get(position);

        holder.question.setText(item.getTitle());
        holder.tagName.setText(item.getTags());
//        holder.rating.setText(Item.getScore());
        Picasso.with(context).load(item.getProfile_image()).into(holder.profileImage);
//        holder.rating.setText(item.get);
        //time is missing

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
