package com.stackoverflow.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stackoverflow.Item;
import com.stackoverflow.JsonManipulationTask;
import com.stackoverflow.JsonManipulationTaskInterface;
import com.stackoverflow.MySingleton;
import com.stackoverflow.R;
import com.stackoverflow.adapter.StackoverflowAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private  String androidUrl;

    private static String androidUrlFirstPart = "https://api.stackexchange.com";

    private Bundle savedState;
    //    @BindView(R.id.recycler_view)
    private RecyclerView recyclerView;

    private int lastFirstVisiblePosition;

    JsonManipulationTask jsonManipulationTask;
    //    Item[] items;
    private ArrayList<Item> listOfitems;

    private Item[] items;

    private LinearLayoutManager linearLayoutManager;

    private Parcelable parcelable;

    private View view,sortView;

    private FloatingActionButton fabSort;

    private RadioGroup firstGroup, secondGroup;

    private RadioButton activity,creation,relevance,votes,asc,dsc;

    int radioGroupId1,radioGroupId2;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blank, container, false);

        recyclerView  = (RecyclerView) view.findViewById(R.id.recycler_view);
        fabSort = (FloatingActionButton) view.findViewById(R.id.fab);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        androidUrl = androidUrlFirstPart+"/2.2/questions/unanswered?order=desc&sort=activity&tagged=android&site=stackoverflow";

        initiateVolley(androidUrl);

        ///2.2/questions/unanswered?order=desc&sort=activity&site=stackoverflow



        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                LayoutInflater alertInflater = LayoutInflater.from(view.getContext());

                sortView = alertInflater.inflate(R.layout.sort_selection,null);

                activity = (RadioButton) sortView.findViewById(R.id.activity);
                creation = (RadioButton) sortView.findViewById(R.id.creation);
                votes = (RadioButton) sortView.findViewById(R.id.votes);
                relevance = (RadioButton) sortView.findViewById(R.id.relevance);
                dsc = (RadioButton) sortView.findViewById(R.id.dsc);
                asc = (RadioButton) sortView.findViewById(R.id.asc);
                radioGroupId1 = R.id.activity;
                radioGroupId2 = R.id.dsc;
                firstGroup = (RadioGroup) sortView.findViewById(R.id.firstRadioGroup);
                secondGroup = (RadioGroup) sortView.findViewById(R.id.secondRadioGroup);

                builder.setView(sortView);

                firstGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                            radioGroupId1 = checkedId;
                    }
                });

                secondGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radioGroupId2 = checkedId;
                    }
                });



                //cancel button/negative button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Update", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(radioGroupId1==R.id.activity && radioGroupId2==R.id.dsc){
                            String url = androidUrl.replace("activity","activity");
                            Log.d("URLChanged",url);
                            initiateVolley(url);
                        }
                        else if(radioGroupId1==R.id.creation && radioGroupId2==R.id.dsc){
                            String url = androidUrl.replace("activity","creation");
                            Log.d("URLChanged",url);
                            initiateVolley(url);
                        }
                        else if(radioGroupId1==votes.getId() && radioGroupId2==R.id.dsc){
                            String url = androidUrl.replace("activity","votes");
                            Log.d("URLChanged",url);
                            initiateVolley(url);
                        }
                        else if (radioGroupId1==relevance.getId() && radioGroupId2==R.id.dsc){
                            String url = androidUrl.replace("activity","activity");
                            Log.d("URLChanged",url);
                            initiateVolley(url);
                        }
                        else if(radioGroupId1==activity.getId() && radioGroupId2==R.id.asc){
                            String url = androidUrl.replace("activity","activity");
//                            Log.d("URLChanged",url.replace("desc","asc"));
                            initiateVolley(url.replace("desc","asc"));
                        }
                        else if(radioGroupId1==creation.getId() && radioGroupId2==R.id.asc){
                            String url = androidUrl.replace("activity","creation");
                            Log.d("URLChanged",url.replace("desc","asc"));
                            initiateVolley(url.replace("desc","asc"));
                        }
                        else if(radioGroupId1==votes.getId() && radioGroupId2==R.id.asc){
                            String url = androidUrl.replace("activity","votes");
                            Log.d("URLChanged",url.replace("desc","asc"));
                            initiateVolley(url.replace("desc","asc"));
                        }
                        else if (radioGroupId1==relevance.getId() && radioGroupId2==R.id.asc){
                            String url = androidUrl.replace("activity","activity");
                            Log.d("URLChanged",url.replace("desc","asc"));
                            initiateVolley(url.replace("desc","asc"));
                        }
                        //dismiss the window
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;

    }

    private void initiateVolley(String urlString){

        Log.d("URLReceived",urlString);

        //object of the singleton class for volley
        MySingleton mySingleton = MySingleton.getInstance(view.getContext());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                JsonManipulationTask jsonManipulationTask = new JsonManipulationTask(view.getContext());

                jsonManipulationTask.setJsonManipulationInterfaceVariable(new JsonManipulationTaskInterface() {
                    @Override
                    public void fetchResult(Item[] items) {
                        listOfitems=new ArrayList<>();
                        for(Item object : items){
                            listOfitems.add(object);
                        }
//                        return  listOfitems;
                        recyclerView.setAdapter(new StackoverflowAdapter(listOfitems,view.getContext()));
                    }

                });
                jsonManipulationTask.execute(response.toString());

//                ArrayList arrayList = fetchResult(items);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response",error.toString());
            }
        });

        mySingleton.addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onPause() {
        super.onPause();
        lastFirstVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        linearLayoutManager.scrollToPosition(lastFirstVisiblePosition);
    }
}