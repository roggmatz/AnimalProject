package us.nc.forsyth.co.animalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import us.nc.forsyth.co.animalproject.Animal;

public class GetJsonData extends AsyncTask<String, Integer, ArrayList<Animal>> {

    private Context currentContext;
    private ProgressDialog dialog;
    JSONArray jsonResult;

    public GetJsonData(Context context) {
        currentContext = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(currentContext, currentContext.getString(R.string.progress_dialog_title),
                currentContext.getString(R.string.progress_dialog_details), true);
    }

    protected ArrayList<Animal> doInBackground(String... urls) {
        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httpPost = new HttpPost(urls[0]);
        httpPost.setHeader("Content-type", "application/json");
        InputStream inputStream = null;
        String tempString = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 30);
            tempString = reader.readLine();
        }
        catch (IOException e) {
            Log.i("AnimalList", "getData() - HTTP request failed or IO stuff failed.");
        }
        finally {
            try {
                if (inputStream != null)
                    inputStream.close();

            }
            catch (Exception squish) {
                Log.i("GetJsonData.class", "Closing inputStream error.");
            }
        }
        try {
            jsonResult = new JSONArray(tempString);
        }
        catch (JSONException jExcept) {
            Log.i("GetJsonData.class", "Error while creating JSON Object. Exception Dump:\n" + jExcept);
        }
        return parseJsonArray(jsonResult);
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    private void decider(Animal a, String attr, JSONObject o) {
       try{
           if(attr.equals("kennel_no")) {
               a.kennelNo = o.getString(attr);}
           else if(attr.equals("tag_type")) {
               a.tagType = o.getString(attr);}
           else if(attr.equals("animal_id")) {
               a.id = o.getString(attr);}
           else if(attr.equals("animal_name")) {
               a.name = o.getString(attr);}
           else if(attr.equals("animal_type")) {
               a.type = o.getString(attr);}
           else if(attr.equals("sex")) {
               a.sex = o.getString(attr);}
           else if(attr.equals("years_old")) {
               a.age = o.getString(attr);}
           else if(attr.equals("color_group")) {
               a.colorGroup = o.getString(attr);}
           else if(attr.equals("primary_color")) {
               a.primaryColor = o.getString(attr);}
           else if(attr.equals("secondary_color")) {
               a.secondaryColor = o.getString(attr);}
           else if(attr.equals("breed_group")) {
               a.breedGroup = o.getString(attr);}
           else if(attr.equals("primary_breed")) {
               a.primaryBreed = o.getString(attr);}
           else if(attr.equals("secondary_breed")) {
               a.secondaryBreed = o.getString(attr);}
           else if(attr.equals("markings")) {
               a.markings = o.getString(attr);}
           else if(attr.equals("animal_size")) {
               a.size = o.getString(attr);}
           else if(attr.equals("intake_date")) {
               a.dateAdded = o.getString(attr);}
       }
       catch (JSONException f) {
           Log.i("GetJsonData.class", "decider():\n" + f);
       }
    }

    private ArrayList<Animal> parseJsonArray(JSONArray j) {
        ArrayList<Animal> data = new ArrayList<Animal>();
        JSONObject object = new JSONObject();
        String s;
        try{
            if(j.isNull(0)) {
                Log.i("GetJsonData.class", "Returning Empty ArrayList.");
                return data;
            }
            object = j.getJSONObject(0);
        }
        catch (JSONException jExcept0) {
            Log.i("GetJsonData.class", "parseJsonArray() 1-\n", jExcept0);
        }
        Iterator<String> fields = object.keys();

        for(int i = 0; i < j.length(); i++) {
            try{
                object = j.getJSONObject(i);
                fields = object.keys();
            }
            catch (JSONException jExcept) {
                Log.i("GetJsonData.class", "parseJsonArray() 2-\n", jExcept);
            }
            Animal animal = new Animal();
            for(int k = 0; k < object.length(); k++) {
                s = fields.next();
                decider(animal, s, object);
            }
            data.add(i, animal);
        }

        return data;
    }

    protected void onPostExecute(ArrayList<Animal> result) {
        dialog.dismiss();

    }

}
