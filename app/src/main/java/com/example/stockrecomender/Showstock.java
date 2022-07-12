package com.example.stockrecomender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class Showstock extends AppCompatActivity {
    ImageView backtohome;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<String> data = new ArrayList<>();
    String[] dataFin = {"TATA MOTORS", "WIPRO", "RIL"};
    String base_url = "https://newsdata.io/api/1/news?apikey=pub_89180c8e3eb93a40cb354f8f7084cb971ba3&country=in&category=business&language=en";
    //String base_url = "https://newsapi.org/v2/top-headlines?category=business&language=en&country=in&apiKey=c0f282d0b87348e3b58b22de9d5d2605";
    JSONArray tmp = new JSONArray();
    Pattern patPos = Pattern.compile("^.*[\b(invested|funded|profit|growth|decreade in debt|profit growth| sales growth| invest| dividend issued | approval| update| growth in sales| profit margin| )\b].*$");
    Pattern patNeg = Pattern.compile("^.*[\b(fell|bankcrupt|stepped away|decrease in sales|loss|no capital|all time low|workers strike|workers protest)\b].*$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showstock);

        backtohome = findViewById(R.id.backtohome);

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Showstock.this, MainActivity.class);
                startActivity(it);
            }
        });

        loadNews();
    }

    private void loadNews(){
        RequestQueue queue = Volley.newRequestQueue(this);
        try{
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, base_url, null, response -> {
                        try {

                            tmp = response.getJSONArray("results");
                            Log.d("ll", tmp.length() + " tmp");
//                            dataFin = new String[tmp.length()];
//                            Log.d("ll", dataFin.length + " datafin");
//                            for(int i=0;i<tmp.length();i++){
//                                JSONObject obj = tmp.getJSONObject(i);
//                                dataFin[i] = obj.getString("title");
//                                Log.d("ll", i +" " + dataFin[i]);
//                            }
                            for(int i=0;i<tmp.length();i++){
                                //matchObject(tmp.getJSONObject(i));
                                JSONObject obj = tmp.getJSONObject(i);
                                String desc = obj.getString("description");
                                boolean a = patPos.matcher(desc).matches();
                                Log.d("ll", a + " pos");
                                if(patPos.matcher(desc).matches()){
                                    Log.d("ll", "matches pos");
                                    if(patNeg.matcher(desc).matches()){
                                        Log.d("ll", "does not matches neg");
                                        data.add(obj.getString("title"));
                                    }
                                }
                            }
                            Log.d("ll", data.size() +" data");
                            dataFin = new String[data.size()];
                            for(int i=0;i<data.size();i++){
                                dataFin[i] = data.get(i);
                            }
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
                            recyclerAdapter = new RecyclerAdapter(this, dataFin);
                            recyclerView.setAdapter(recyclerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        // TODO: Handle error
                        Toast.makeText(getBaseContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        } catch (Exception e){
            Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}