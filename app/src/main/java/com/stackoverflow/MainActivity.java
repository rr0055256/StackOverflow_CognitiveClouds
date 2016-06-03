package com.stackoverflow;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static String androidUrl = "https://api.stackexchange.com/2.2/questions/unanswered?order=desc&sort=activity&tagged=android&site=stackoverflow";

//    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    JsonManipulationTask jsonManipulationTask;
//    Item[] items;
    private ArrayList<Item> listOfitems;

    private Item[] items;

    private LinearLayoutManager linearLayoutManager;

    private Parcelable parcelable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recyclerView  = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        initiateVolley(androidUrl);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (parcelable != null) {
            linearLayoutManager.onRestoreInstanceState(parcelable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void convertToList(){
        for(Item object:output){
            list.add(object);
        }
    }*/

    private void initiateVolley(String urlString){

        Log.d("URL",urlString);

        //object of the singleton class for volley
        MySingleton mySingleton = MySingleton.getInstance(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                JsonManipulationTask jsonManipulationTask = new JsonManipulationTask(MainActivity.this);

                jsonManipulationTask.setJsonManipulationInterfaceVariable(new JsonManipulationTaskInterface() {
                    @Override
                    public void fetchResult(Item[] items) {
                        listOfitems=new ArrayList<>();
                        for(Item object : items){
                            listOfitems.add(object);
                        }
//                        return  listOfitems;
                        recyclerView.setAdapter(new StackoverflowAdapter(listOfitems,MainActivity.this));
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        parcelable = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable("myState", parcelable);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null)
        parcelable = savedInstanceState.getParcelable("myState");
    }
}
