package com.stackoverflow;

import android.os.Bundle;
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
    ArrayList<Item> items=new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recyclerView  = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        /*jsonManipulationTask = new JsonManipulationTask(getApplicationContext());
        jsonManipulationTask.execute(androidUrl);
*/
//        output = jsonManipulationTask.getOutput();
//        Log.d("Output",output.toString());

//        convertToList();


        MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());


//        Image,Question,time stamp,User name,tags,votes
        //JSON activity

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, androidUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Inner Response", response.toString());
                Item[] items2 = parseJson(response.toString());

                items.clear();
                for(Item object : items2){
                    items.add(object);
                }

                recyclerView.setAdapter(new StackoverflowAdapter(items,getApplicationContext()));
//                JsonManipulationTask.this.response = response;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response",error.toString());
            }
        });

        mySingleton.addToRequestQueue(jsonObjectRequest);

//        recyclerView.setAdapter(new StackoverflowAdapter(items));

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

    private Item[] parseJson(String jsonString) {
        try {
            JSONObject object=new JSONObject(jsonString);
            JSONArray itemArray=object.getJSONArray("items");
            Item[] output=new Item[itemArray.length()];
            String image;
            String name;
            int score;
            String link;
            String title;
            long last_date;

            //for loop to get all the necessary details
            for(int i=0;i<itemArray.length();i++) {
                JSONObject root = itemArray.getJSONObject(i);
                score=root.getInt("score");
                link=root.getString("link");
                title=root.getString("title");
                last_date=root.getLong("last_activity_date");
                JSONObject owner=root.getJSONObject("owner");
                image=owner.getString("profile_image");
                name=owner.getString("display_name");
                JSONArray tag=root.getJSONArray("tags");

                //String image, String name, int score, String link, String title, long time, String[] tags)
                Item f = new Item(image,name,score,link,title,last_date,tag);
                Log.d("f",String.valueOf(f.getDisplay_name()));
                output[i]=f;
            }
            return output;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
