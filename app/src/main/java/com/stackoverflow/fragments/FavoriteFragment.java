package com.stackoverflow.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stackoverflow.Item;
import com.stackoverflow.R;
import com.stackoverflow.adapter.FavoritesAdapter;
import com.stackoverflow.db.FavoriteDbHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;

    private View view;

    private ArrayList<Item> listOfitems;

    private LinearLayoutManager linearLayoutManager;
    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite, container, false);


        Item[] items = getFavoritesData();
        listOfitems = new ArrayList<>();
        String image;
        String name;
        int score;
        String link;
        String title;
        long last_date;
        String tags;
        for (int i = 0; i < items.length; i++) {
            image=items[i].getProfile_image();
            name=items[i].getDisplay_name();
            score=items[i].getScore();
            link=items[i].getLink();
            title=items[i].getTitle();
            last_date=items[i].getActivityLastDate();
            tags=items[i].getFavoriteTags();
            Item j=new Item(image,name,score,link,title,last_date,null,tags);
            listOfitems.add(j);
        }

        //recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favourites);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new FavoritesAdapter(listOfitems,view.getContext()));

        return view;
    }

    public Item[] getFavoritesData() {
        FavoriteDbHelper helper = new FavoriteDbHelper(view.getContext());

        SQLiteDatabase db = helper.getReadableDatabase();

        String [] columns={FavoriteDbHelper.DISPLAY_NAME,FavoriteDbHelper.PROFILE_PIC,FavoriteDbHelper.TITLE,FavoriteDbHelper.TAGS,FavoriteDbHelper.SCORE,FavoriteDbHelper.LINK,FavoriteDbHelper.TIME};

        Cursor c = db.query(FavoriteDbHelper.TABLE_NAME, null, null, null, null, null, null);

        //read data from the data base
        Item[] output = new Item[c.getCount()];
        int i = 0;
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(FavoriteDbHelper.DISPLAY_NAME));
            String pic=c.getString(c.getColumnIndex(FavoriteDbHelper.PROFILE_PIC));
            String title=c.getString(c.getColumnIndex(FavoriteDbHelper.TITLE));
            String tags=c.getString(c.getColumnIndex(FavoriteDbHelper.TAGS));
            String link=c.getString(c.getColumnIndex(FavoriteDbHelper.LINK));
            int score=c.getInt(c.getColumnIndex(FavoriteDbHelper.SCORE));
            long time=c.getLong(c.getColumnIndex(FavoriteDbHelper.TIME));

            Item k=new Item(pic,name,score,link,title,time,null,tags);

            output[i++]=k;
        }
        db.close();
        return output;
    }
}
