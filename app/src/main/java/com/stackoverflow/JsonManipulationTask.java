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

                if(root.isNull("score")){
                    score = 0;
                }else{
                    score=root.getInt("score");
                }

                if(root.isNull("link")){
                    link = "http://stackoverflow.com/questions";
                }else{
                    link=root.getString("link");
                }

                if(root.isNull("title")){
                    title = "Stackoverflow Question";
                }else{
                    title=root.getString("title");
                }

                if(root.isNull("last_activity_date")){
                    last_date = 0;
                }else{
                    last_date=root.getLong("last_activity_date");
                }

                JSONObject owner=root.getJSONObject("owner");

                if(owner.isNull("profile_image")){
                    image = "https://cnet4.cbsistatic.com/hub/i/2011/10/27/a66dfbb7-fdc7-11e2-8c7c-d4ae52e62bcc/android-wallpaper5_2560x1600_1.jpg";
                }else {
                    image = owner.getString("profile_image");
                }
                if(owner.isNull("display_name")){
                    name = "User";
                }else {
                    name = owner.getString("display_name");
                }
                JSONArray tag=root.getJSONArray("tags");
                //String image, String name, int score, String link, String title, long time, String[] tags)
                Item f = new Item(image,name,score,link,title,last_date,tag,null);
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
