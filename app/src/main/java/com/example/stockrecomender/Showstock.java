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
    ArrayList<Data> dataset = new ArrayList<>();
    String base_url = "https://newsdata.io/api/1/news?apikey=pub_89180c8e3eb93a40cb354f8f7084cb971ba3&country=in&category=business&language=en";
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
                            //the respone is recieved here
                            tmp = response.getJSONArray("results");
                            // each object is iterated to match the pattern
                            for(int i=0;i<tmp.length();i++){
                                JSONObject obj = tmp.getJSONObject(i);
                                String desc = obj.getString("description");

                                if(patPos.matcher(desc).matches()){
                                    if(patNeg.matcher(desc).matches()){
                                        String title = obj.getString("title");
                                        String link = obj.getString("link");
                                        dataset.add(new Data(title, link));
                                    }
                                }
                            }
                            recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
                            recyclerAdapter = new RecyclerAdapter(this, dataset);
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