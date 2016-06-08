package com.stackoverflow.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.stackoverflow.Item;
import com.stackoverflow.R;

import java.util.List;

/**
 * Created by raman on 4/6/16.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    Context context;
    List<Item> list;

    public FavoritesAdapter(List<Item> list, Context context) {
        this.context = context;
        this.list=list;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_display,parent,false);
        FavoritesAdapter.FavoritesViewHolder favoritesViewHolder = new FavoritesViewHolder(v);
        return favoritesViewHolder;
    }

    @Nullable
    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        final Item item = list.get(position);

        holder.question.setText(item.getTitle());
        holder.tagName.setText(item.getFavoriteTags());

        holder.rating.setText(String.valueOf(item.getScore()));
        try {
            //Loaded rounded images
//            Picasso.with(context).load(item.getProfile_image()).transform(new CircleTransform()).into(holder.profileImage);
            Picasso.with(context).load(item.getProfile_image()).into(holder.profileImage);
        }catch(Exception e) {
            holder.tagName.setText(item.getFavoriteTags());
        }


        holder.userName.setText(item.getDisplay_name());

        //open window to share the link when clicked
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Share text:
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.setType("text/plain");
                messageIntent.putExtra(Intent.EXTRA_TEXT, item.getLink() );
                context.startActivity(Intent.createChooser(messageIntent, "Share via"));

            }
        });

        //open the link in the broswer when clicked on  the card
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class FavoritesViewHolder extends RecyclerView.ViewHolder{

        CardView cv;

        ImageView profileImage;

        TextView question,tagName,userName;

        Button rating,share;

        FavoritesViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view_fav);
            profileImage = (ImageView) itemView.findViewById(R.id.imageView_fav);
            question = (TextView) itemView.findViewById(R.id.question_fav);
            tagName = (TextView) itemView.findViewById(R.id.tags_fav);
            rating = (Button) itemView.findViewById(R.id.rating_fav);
            share = (Button) itemView.findViewById(R.id.share_fav);
            userName = (TextView) itemView.findViewById(R.id.user_fav);

        }

    }
}
