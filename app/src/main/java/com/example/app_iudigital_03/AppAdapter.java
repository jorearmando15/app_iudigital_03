package com.example.app_iudigital_03;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppAdapter extends ArrayAdapter {

    private static final String URL = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

    RequestQueue requestQueue;

    List<AppModel> itemsApp = new ArrayList<AppModel>();

    public AppAdapter(Context context){
        super(context,0);

        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        itemsApp = parseJSON(response);
                        notifyDataSetChanged();
                        Log.e("AppAdapter Response: ", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AppAdapter ERROR: ", error.toString());
                    }
                }
        );


        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public int getCount() {
        return itemsApp!=null?itemsApp.size():0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View ListItemView;

        ListItemView = null == convertView ? layoutInflater.inflate(
                R.layout.activity_appadapter,
                parent,
                false) : convertView;

        AppModel item = itemsApp.get(position);

        TextView textName = ListItemView.findViewById(R.id.textName);
        TextView textSummary = ListItemView.findViewById(R.id.textSummary);
        final ImageView image = ListItemView.findViewById(R.id.image);

        textName.setText(item.getName());
        textSummary.setText(item.getSummary());

        ImageRequest request = new ImageRequest(
                item.getImage(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image.setImageBitmap(response);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error cargando imagen ", Objects.requireNonNull(error.getMessage()));
                    }
                }
        );
        requestQueue.add(request);

        return ListItemView;
    }

    public List<AppModel> parseJSON(JSONObject jsonObject){

        List<AppModel> applist = new ArrayList<>();

        try{
            JSONObject feedJSON = jsonObject.getJSONObject("feed");
            JSONArray entryJSON = feedJSON.getJSONArray("entry");

            for(int i=0;i<entryJSON.length();i++){

                try{
                    JSONObject entryObject = entryJSON.getJSONObject(i);
                    JSONObject nameJSON = entryObject.getJSONObject("im:name");
                    JSONObject summaryJSON = entryObject.getJSONObject("summary");
                    JSONObject rightsJSON = entryObject.getJSONObject("rights");

                    JSONArray imageJSON = entryObject.getJSONArray("im:image");
                    JSONObject imageObject = imageJSON.getJSONObject(2);

                    AppModel app = new AppModel(
                            nameJSON.getString("label"),
                            imageObject.getString("label"),
                            rightsJSON.getString("label"),
                            summaryJSON.getString("label")
                    );

                    applist.add(app);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return applist;
    }
}
