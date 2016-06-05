package com.stackoverflow;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stackoverflow.adapter.StackoverflowAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    String url = "https://api.stackexchange.com/2.2/questions/unanswered?order=desc&sort=activity&tagged=android&site=stackoverflow";

    private ArrayList<Item> listOfitems;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private RadioButton activity,creation, votes, relevance, asc, dsc;

    private RadioGroup firstGroup, secondGroup;

    private Parcelable parcelable;

    private int radioGroupId1,radioGroupId2;

    private String query,replacedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView  = (RecyclerView) findViewById(R.id.recycler_view_search);

        linearLayoutManager = new LinearLayoutManager(SearchResultsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //search
        handleIntent(getIntent());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultsActivity.this);

                LayoutInflater alertInflater = LayoutInflater.from(SearchResultsActivity.this);

                 View sortView = alertInflater.inflate(R.layout.sort_selection,null);

                activity = (RadioButton) sortView.findViewById(R.id.activity);
                creation = (RadioButton) sortView.findViewById(R.id.creation);
                votes = (RadioButton) sortView.findViewById(R.id.votes);
                relevance = (RadioButton) sortView.findViewById(R.id.relevance);
                dsc = (RadioButton) sortView.findViewById(R.id.dsc);
                asc = (RadioButton) sortView.findViewById(R.id.asc);
                radioGroupId1 = R.id.activity;
                radioGroupId2 = R.id.dsc;
                RadioGroup firstGroup = (RadioGroup) sortView.findViewById(R.id.firstRadioGroup);
                RadioGroup secondGroup = (RadioGroup) sortView.findViewById(R.id.secondRadioGroup);

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
                            String urlSort = replacedUrl.replace("activity","activity");
                            Log.d("URLChanged",urlSort);
                            initiateVolley(urlSort);
                        }
                        else if(radioGroupId1==R.id.creation && radioGroupId2==R.id.dsc){
                            String urlSort = replacedUrl.replace("activity","creation");
                            Log.d("URLChanged",urlSort);
                            initiateVolley(urlSort);
                        }
                        else if(radioGroupId1==votes.getId() && radioGroupId2==R.id.dsc){
                            String urlSort = replacedUrl.replace("activity","votes");
                            Log.d("URLChanged",urlSort);
                            initiateVolley(urlSort);
                        }
                        else if (radioGroupId1==relevance.getId() && radioGroupId2==R.id.dsc){
                            String urlSort = replacedUrl.replace("activity","activity");
                            Log.d("URLChanged",urlSort);
                            initiateVolley(urlSort);
                        }
                        else if(radioGroupId1==activity.getId() && radioGroupId2==R.id.asc){
                            String urlSort = replacedUrl.replace("activity","activity");
//                            Log.d("URLChanged",url.replace("desc","asc"));
                            initiateVolley(urlSort.replace("desc","asc"));
                        }
                        else if(radioGroupId1==creation.getId() && radioGroupId2==R.id.asc){
                            String urlSort = replacedUrl.replace("activity","creation");
                            Log.d("URLChanged",urlSort.replace("desc","asc"));
                            initiateVolley(urlSort.replace("desc","asc"));
                        }
                        else if(radioGroupId1==votes.getId() && radioGroupId2==R.id.asc){
                            String urlSort = replacedUrl.replace("activity","votes");
                            Log.d("URLChanged",urlSort.replace("desc","asc"));
                            initiateVolley(urlSort.replace("desc","asc"));
                        }
                        else if (radioGroupId1==relevance.getId() && radioGroupId2==R.id.asc){
                            String urlSort = replacedUrl.replace("activity","activity");
                            Log.d("URLChanged",urlSort.replace("desc","asc"));
                            initiateVolley(urlSort.replace("desc","asc"));
                        }
                        //dismiss the window
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            Log.d("Query",query);
            replacedUrl = url.replace("android",query);

            initiateVolley(replacedUrl);


        }
    }

    private void initiateVolley(String urlString){

        Log.d("URLReceived",urlString);

        //object of the singleton class for volley
        MySingleton mySingleton = MySingleton.getInstance(SearchResultsActivity.this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                JsonManipulationTask jsonManipulationTask = new JsonManipulationTask(SearchResultsActivity.this);

                jsonManipulationTask.setJsonManipulationInterfaceVariable(new JsonManipulationTaskInterface() {
                    @Override
                    public void fetchResult(Item[] items) {
                        listOfitems=new ArrayList<>();
                        for(Item object : items){
                            listOfitems.add(object);
                        }
//                        return  listOfitems;
                        recyclerView.setAdapter(new StackoverflowAdapter(listOfitems,SearchResultsActivity.this));
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

    //restoring the state position


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save list state
        parcelable = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable("state",parcelable);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null){
            parcelable = savedInstanceState.getParcelable("state");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(parcelable != null){
            linearLayoutManager.onRestoreInstanceState(parcelable);
        }
    }
}
