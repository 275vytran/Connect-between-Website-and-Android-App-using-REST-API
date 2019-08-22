package com.vytran.restapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private List<Model> list;
    private RecyclerAdapter adapter;
    private String baseURL = "http://vyarchanics.co.nf";

    //Tutorial: https://www.youtube.com/watch?v=W0zTZK3vCeg
    //Field on categories: http://vyarchanics.co.nf/wp-json/wp/v2/posts/?filter[category_name]=android&per_page=100&fields=title,content


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        list = new ArrayList<Model>();
        //call retrofit to pass content to list
        getRetrofit();


    }

    public void getRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<List<WPPost>> call = service.getPostInfo();

        // to make call to dynamic URL

        // String yourURL = yourURL.replace(BaseURL,"");
        // Call<List<WPPost>>  call = service.getPostInfo( yourURL);

        /// to get only 6 post from your blog
        // http://your-blog-url/wp-json/wp/v2/posts?per_page=2

        // to get any specific blog post, use id of post
        //  http://www.blueappsoftware.in/wp-json/wp/v2/posts/1179

        // to get only title and id of specific
        // http://www.blueappsoftware.in/android/wp-json/wp/v2/posts/1179?fields=id,title

        call.enqueue(new Callback<List<WPPost>>() {
            @Override
            public void onResponse(Call<List<WPPost>> call, Response<List<WPPost>> response) {

                //Disable progress bar whenever receive the response
                progressBar.setVisibility(View.GONE);
                //list.add(new Model (response.body().getPostInfo(), response.body().getPostInfo()));

                //Loop for all the body content
                for (int i=0; i < response.body().size(); i++) {
                    //response.body().get(i) //first ith post - for example, get post 1, post 2
                    //String tempDetails = response.body.get(i).getException().getRendered();
                    //tempDetails = tempDetails.replace("<p>", ""); //replace the things you dont want
                    //tempdetails.replaceAll("<.*?>", ""); to remove all HTML tags.
                    String title = response.body().get(i).getTitle().getRendered();
                    title = Jsoup.parse(title).text();
                    String content = response.body().get(i).getContent().getRendered();
                    content = Jsoup.parse(content).text();
                    list.add(new Model(title, content));
                }

                adapter = new RecyclerAdapter(list, MainActivity.this);
                recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<WPPost>> call, Throwable t) {
                Log.e("MainActivity", "failure" + t.getMessage());
            }

        });
    }

}
