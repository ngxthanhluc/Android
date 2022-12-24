package com.example.baitap4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Official> officials = new ArrayList<>();
    private RecyclerView list;
    private OfficialAdapter listAdapter = new OfficialAdapter(this, officials);
    private Intent intent;
    private String locationID;
    private HashMap<Integer, String> indexMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list_item);
        list.setAdapter(listAdapter);
    }

    public void InputDialog() {
        if (isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Nhập tên tiểu bang hoặc mã ZIP:");
            builder.setCancelable(true);
            EditText input = new EditText(this);
            input.setGravity(Gravity.CENTER_HORIZONTAL);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (input.getText().toString().equals("")) dialog.cancel();
                    else {
                        String api = "https://civicinfo.googleapis.com/civicinfo/v2/representatives?address=" + input.getText().toString() + "&key=AIzaSyBZp6kRPZc7oEAfGN01qe6z5xFxw97FfF4";
                        myAsyncTasks asyncTask = new myAsyncTasks();
                        asyncTask.execute(api);
                        readData(api);
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            Toast.makeText(MainActivity.this, "Vui lòng kiểm tra lại kết nối mạng", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public class myAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder("");

            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                TextView location = findViewById(R.id.location);
                JSONObject jsonObject = new JSONObject(s);
                JSONObject outputLocation = jsonObject.getJSONObject("normalizedInput");
                locationID = outputLocation.getString("city") + ", " + outputLocation.getString("state") + " " + outputLocation.getString("zip");
                location.setText(locationID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                InputDialog();
                return true;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected())
                return true;
            else
                return false;
        }
        return false;
    }

    public void readData(String api) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, api, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray officeArray = jsonObject.getJSONArray("offices");
                    for (int i = 0; i < officeArray.length(); i++) {
                        JSONObject currentOffice = officeArray.getJSONObject(i);
                        JSONArray officialArray = currentOffice.getJSONArray("officialIndices");
                        for (int j = 0; j < officialArray.length(); j++) {
                            indexMap.put(officialArray.getInt(j), currentOffice.getString("name"));
                        }
                    }
                    addOfficial(indexMap, String.valueOf(response));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void addOfficial(HashMap<Integer, String> map, String temp) {
        if (!officials.isEmpty()) officials.clear();
        try {
            JSONObject jsonObject = new JSONObject(temp);
            JSONArray officialsArray = jsonObject.getJSONArray("officials");
            for (int i = 0; i < officialsArray.length(); i++) {
                Channels channelTemp = new Channels(null, null, null, null);
                JSONObject index = officialsArray.getJSONObject(i);
                officials.add(new Official(indexMap.get(i),
                        index.getString("name"),
                        index.getString("party"),
                        parseAddress(officialsArray, i),
                        parsePhone(officialsArray, i),
                        parseEmail(officialsArray, i),
                        parseUrls(officialsArray, i),
                        parseChannel(officialsArray, i, channelTemp),
                        index.optString("photoUrl")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list.setAdapter(listAdapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addOnItemTouchListener(new RecyclerItemClickListener(this, list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent = new Intent(MainActivity.this, OfficialActivity.class);
                pushIntent(intent, position);
            }


        }));
    }

    public String parseAddress(JSONArray officialsArray, int i) {
        try {
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("geocodingSummaries");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getString("queryString");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No Data Provided";
    }

    public String parseEmail(JSONArray officialsArray, int i) {
        try {
            String temp = "";
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("emails");
            for (int j = 0; j < jsonArray.length(); j++) {
                temp += jsonArray.getString(j);
                if (j < jsonArray.length() - 1) temp += "\n";
            }
            return temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No Data Provided";
    }

    public ArrayList<String> parsePhone(JSONArray officialsArray, int i) {
        try {
            ArrayList<String> temp = new ArrayList<>();
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("phones");
            for (int j = 0; j < jsonArray.length(); j++) {
                temp.add(jsonArray.optString(j));
            }
            return temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> parseUrls(JSONArray officialsArray, int i) {
        try {
            ArrayList<String> temp = new ArrayList<>();
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("urls");
            for (int j = 0; j < jsonArray.length(); j++) {
                temp.add(jsonArray.optString(j));
            }
            return temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Channels parseChannel(JSONArray officialsArray, int i, Channels channel) {
        try {
            JSONObject index = officialsArray.getJSONObject(i);
            JSONArray jsonArray = index.getJSONArray("channels");
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject index2 = jsonArray.getJSONObject(j);
                if (index2.getString("type").equals("Youtube"))
                    channel.setYoutube(index2.getString("id"));
                if (index2.getString("type").equals("Twitter"))
                    channel.setTwitter(index2.getString("id"));
                if (index2.getString("type").equals("Google+"))
                    channel.setGoogle(index2.getString("id"));
                if (index2.getString("type").equals("Facebook"))
                    channel.setFacebook(index2.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public void pushIntent(Intent intent, int position) {
        intent.putExtra("location", locationID);
        intent.putExtra("office", officials.get(position).getOffice());
        intent.putExtra("name", officials.get(position).getName());
        intent.putExtra("party", officials.get(position).getParty());
        intent.putExtra("address", officials.get(position).getAddress());
        intent.putExtra("phones", officials.get(position).getPhones());
        intent.putExtra("email", officials.get(position).getEmail());
        intent.putExtra("website", officials.get(position).getWebsite());
        intent.putExtra("channelsYoutube", officials.get(position).getChannels().getYoutube());
        intent.putExtra("channelsGoogle", officials.get(position).getChannels().getGoogle());
        intent.putExtra("channelsFacebook", officials.get(position).getChannels().getFacebook());
        intent.putExtra("channelsTwitter", officials.get(position).getChannels().getTwitter());
        intent.putExtra("photoUrl", officials.get(position).getPhotoUrl());
        startActivity(intent);
    }

}