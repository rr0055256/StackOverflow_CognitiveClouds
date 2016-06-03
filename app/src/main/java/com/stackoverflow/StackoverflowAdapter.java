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

import org.w3c.dom.Text;

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
        Button rating;

//        @BindView(R.id.like)
        Button  like;

//        @BindView(R.id.share)
        Button share;

        TextView timeStamp;

        TextView userName;

        StackOverflowViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            profileImage = (ImageView) itemView.findViewById(R.id.imageView);
            question = (TextView) itemView.findViewById(R.id.question);
            tagName = (TextView) itemView.findViewById(R.id.tags);
            rating = (Button) itemView.findViewById(R.id.rating);
            like = (Button) itemView.findViewById(R.id.like);
            share = (Button) itemView.findViewById(R.id.share);
            timeStamp = (TextView) itemView.findViewById(R.id.timestamp);
            userName = (TextView) itemView.findViewById(R.id.user);

        }

    }

    @Nullable
    @Override
    public void onBindViewHolder(StackOverflowViewHolder holder, int position) {
        Item item = list.get(position);

        holder.question.setText(item.getTitle());
        holder.tagName.setText(item.getTags());

        holder.rating.setText(String.valueOf(item.getScore()));
        Picasso.with(context).load(item.getProfile_image()).into(holder.profileImage);
        holder.tagName.setText(item.getTags());

        //set time stamp
        //get activity last date
        long activityLastDate = item.getActivityLastDate();
        //current time
        long time1 = System.currentTimeMillis()/1000;
        long uptime=time1+activityLastDate;
        int minutes = (int) ((uptime % 3600) / 60);
        holder.timeStamp.setText("Last Updated :"+String.valueOf(minutes)+" minutes ago");

        holder.userName.setText(item.getDisplay_name());

        /*int titleLength = item.getTitle().length();
        if(titleLength>65){
            String s = item.getTitle().substring(0,65)+"...";
            holder.question.setText(s);
        }*/
        //set user name
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
