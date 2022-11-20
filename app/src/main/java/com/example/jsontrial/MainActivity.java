package com.example.jsontrial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Model> list = new ArrayList<>();
    private RequestQueue mRequestQueue;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;

    TextInputEditText imageSearchBox;
    Button searchButton;
    public String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        recyclerView = findViewById(R.id.recycler_view);
        searchButton = findViewById(R.id.search_btn);
        imageSearchBox = findViewById(R.id.image_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageSearchBox.getText().toString().isEmpty()) {
                    Toasty.info(MainActivity.this, "Please type to search...", Toasty.LENGTH_SHORT).show();
                } else {
                    key = imageSearchBox.getText().toString();
                    Toasty.info(MainActivity.this, "Showing images of " + key, Toasty.LENGTH_SHORT).show();
                    jsonParse();
                }
            }
        });

    }

    public void jsonParse() {

        String url = "https://pixabay.com/api/?key=18102436-caa538948718f4a1524ff24a6&q=" + key + "&image_type=photo&pretty=true";
        list.clear();
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray arr = response.getJSONArray("hits");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject hit = arr.getJSONObject(i);

                        String creatorName = hit.getString("user");
                        String imageURL = hit.getString("webformatURL");
                        String userImageURL = hit.getString("userImageURL");
                        int likeCount = hit.getInt("likes");

                        list.add(new Model(imageURL, userImageURL, creatorName, likeCount));
                    }

                    recyclerViewAdapter = new RecyclerViewAdapter(list, MainActivity.this);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        mRequestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        androidx.appcompat.widget.SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search by creator...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;

    }
}
