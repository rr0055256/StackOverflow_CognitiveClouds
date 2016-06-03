package com.stackoverflow;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by raman on 1/6/16.
 */

public class JsonManipulationTask extends AsyncTask<String,String,Item[]>{

    private Context context;
    private JsonManipulationTaskInterface jsonManipulationTaskInterface;
    private JSONObject response;
    private Item[] items,output;
    private RequestQueue requestQueue;

    public JsonManipulationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Item[] doInBackground(String... params) {

        parseJson(params[0]);
        return output;
    }


    @Override
    protected void onPostExecute(Item[] items) {
        super.onPostExecute(items);
        if(jsonManipulationTaskInterface!=null) {
            jsonManipulationTaskInterface.fetchResult(items);
        }
    }

    //to set the interface variable
    public void setJsonManipulationInterfaceVariable(JsonManipulationTaskInterface jsonManipulationInterfaceVariable){
        this.jsonManipulationTaskInterface = jsonManipulationInterfaceVariable;
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

}
