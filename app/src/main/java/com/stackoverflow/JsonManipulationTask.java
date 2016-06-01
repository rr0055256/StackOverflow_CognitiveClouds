package com.stackoverflow;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raman on 1/6/16.
 */

public class JsonManipulationTask extends AsyncTask<String,Void,Item[]>{

    private Context context;
    JSONObject response;
    Item[] output;

    public JsonManipulationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Item[] doInBackground(String... params) {
        String urlString = params[0];

        Log.d("URL",urlString);
        //object of the singleton class for volley
        MySingleton mySingleton = MySingleton.getInstance(context);


//        Image,Question,time stamp,User name,tags,votes
        //JSON activity

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                parseJson(response.toString());
//                JsonManipulationTask.this.response = response;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response",error.toString());
            }
        });

        mySingleton.addToRequestQueue(jsonObjectRequest);
        return parseJson(response.toString());
    }

    private Item[] parseJson(String jsonString) {
        try {
            JSONObject object=new JSONObject(jsonString);
            JSONArray itemArray=object.getJSONArray("items");
            output=new Item[itemArray.length()];
            String image;
            String name;
            int score;
            String link;
            String title;
            long last_date;
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

    @Nullable
    public Item[] getOutput(){
        Log.d("JsonManiOutput",(String.valueOf(output.length)));
        return output;
    }
}
