package com.codewithdeva.pagination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<MainData> dataArrayList = new ArrayList<>();    //create modle class.
    MainAdapter adapter;
    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);

        //Initialize adapter
        adapter = new MainAdapter(MainActivity.this, dataArrayList);
        //set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set adapter
        recyclerView.setAdapter(adapter);

        try {
            //create getdata method
            getData(page, limit);

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        //when reach last item position
                        //Increase page size
//                        page++;
//                        //show progress bar
                       progressBar.setVisibility(View.VISIBLE);
//                        //call method
//                        getData(page, limit);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData(int page, int limit) {

        //create Retrofit.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //create main Interface
        MainInterface mainInterface = retrofit.create(MainInterface.class);
        //Initialize call
        Call<List<MainData>> call = mainInterface.stringCall(page, limit);

        try {
            call.enqueue(new Callback<List<MainData>>() {
                @Override
                public void onResponse(Call<List<MainData>> call, Response<List<MainData>> response) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful() && response.body() != null) {
                        progressBar.setVisibility(View.GONE);
                        try {   //Initialize Json Array
                            dataArrayList = response.body();
                            //parse json array
                            adapter = new MainAdapter(MainActivity.this, dataArrayList);
                            recyclerView.setAdapter(adapter);

//                            parseResult(jsonArray);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<MainData>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    //   private void parseResult(JSONArray jsonArray) {
//        for(int i=0;i<jsonArray.length();i++) {
//            try {
//                //Initialize json object
//                JSONObject object = jsonArray.getJSONObject(i);
//                //Initialize main data
//                MainData data = new MainData();
//                //set Image
//                data.setImage(object.getString("thumbnailUrl"));
//                //add data in Array list
//                data.setName(object.getString("title"));
//
//
//            //Initialize Adapter
//            adapter=new MainAdapter(MainActivity.this,dataArrayList);
//            //set Adapter
//            recyclerView.setAdapter(adapter);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}