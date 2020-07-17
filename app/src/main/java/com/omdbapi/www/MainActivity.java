package com.omdbapi.www;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.omdbapi.www.adapters.MainAdapter;
import com.omdbapi.www.database.DbHelper;
import com.omdbapi.www.models.MainList;
import com.omdbapi.www.models.MainListResponse;
import com.omdbapi.www.util.RetrofitClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnItemClicked {

    APIInterface apiInterface;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public List<MainList> mainLists = new ArrayList<MainList>();
    MainListResponse resource;
    DbHelper dbHelper;
    File database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if (database.exists()) {
            mainLists = dbHelper.getMainList();
            mainAdapter = new MainAdapter(mainLists);
            recyclerView.setAdapter(mainAdapter);
            mainAdapter.setOnClick(MainActivity.this);
        } else if (isOnline()) {
            getList();
        } else {
            dialogNoInternet();
        }

    }

    public void init() {

        dbHelper = new DbHelper(MainActivity.this);
        database = getDatabasePath(DbHelper.DB_NAME);
        apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);


    }

    public void getList() {
        Call<MainListResponse> call = apiInterface.getMainList("3e974fca", "batman");
        call.enqueue(new Callback<MainListResponse>() {
            @Override
            public void onResponse(Call<MainListResponse> call, Response<MainListResponse> response) {

                resource = response.body();

                for (int i = 0; i < resource.getSearch().size(); i++) {

                    mainLists.add(new MainList(resource.getSearch().get(i).getImdbID(),
                            resource.getSearch().get(i).getTitle(),
                            resource.getSearch().get(i).getYear(),
                            resource.getSearch().get(i).getType(),
                            resource.getSearch().get(i).getPoster())
                    );

                    dbHelper.insertData(resource.getSearch().get(i).getImdbID(),
                            resource.getSearch().get(i).getTitle(),
                            resource.getSearch().get(i).getYear(),
                            resource.getSearch().get(i).getType(),
                            resource.getSearch().get(i).getPoster());

                }


                mainAdapter = new MainAdapter(mainLists);
                recyclerView.setAdapter(mainAdapter);
                mainAdapter.setOnClick(MainActivity.this);


            }

            @Override
            public void onFailure(Call<MainListResponse> call, Throwable t) {

                dialogNoInternet();

                call.cancel();
            }
        });

    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("imdbID", mainLists.get(position).getImdbID());
        startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void dialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("There is a problem , please check you internet");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


}
