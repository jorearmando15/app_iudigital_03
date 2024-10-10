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

public class CategoryAdapter extends ArrayAdapter {

    private static final String TAG = "CategoryAdapter";

    private static final String URL = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

    RequestQueue requestQueue;

    List<AppModel> itemsApp;

    public CategoryAdapter(Context context) {
        super(context, 0);
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
                R.layout.activity_adapter_category,
                parent,
                false) : convertView;

        AppModel item = itemsApp.get(position);

        TextView textName = ListItemView.findViewById(R.id.textName);
        TextView textCategory = ListItemView.findViewById(R.id.textCategory);
        TextView textSummary = ListItemView.findViewById(R.id.textSummary);

        textName.setText(item.getName());
        textCategory.setText(item.getCategory());
        textSummary.setText(item.getSummary());

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
                    JSONObject categoryJSON = entryObject.getJSONObject("category");
                    JSONObject atributtescategoryJSON = categoryJSON.getJSONObject("attributes");


                    AppModel category = new AppModel(
                            nameJSON.getString("label"),
                            atributtescategoryJSON.getString("label"),
                            summaryJSON.getString("label")
                    );

                    applist.add(category);

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
