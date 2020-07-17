package com.omdbapi.www;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.omdbapi.www.database.DbHelper;
import com.omdbapi.www.models.Details;
import com.omdbapi.www.util.RetrofitClient;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import retrofit2.Call;
//import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    APIInterface apiInterface;
    TextView name,year_country,rated,genre,runtime,director,writer,actor,plot,language,awards,metascore,imdbVotes,type,DVDs,BoxOffice,Production,Website,imdbRating;
    String imdbID;
    ImageView poster;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        imdbID=getIntent().getExtras().getString("imdbID");

        if (dbHelper.rowIdExists(imdbID)) {

            name.setText(dbHelper.getDetail(imdbID).get(dbHelper.titleColumn));
            year_country.setText(dbHelper.getDetail(imdbID).get(dbHelper.countryColumn)+" / "+ dbHelper.getDetail(imdbID).get(dbHelper.releaseColumn));
            genre.setText(dbHelper.getDetail(imdbID).get(dbHelper.genreColumn));
            runtime.setText(dbHelper.getDetail(imdbID).get(dbHelper.runtimeColumn));
            rated.setText(dbHelper.getDetail(imdbID).get(dbHelper.ratedColumn));
            director.setText(dbHelper.getDetail(imdbID).get(dbHelper.directorColumn));
            writer.setText(dbHelper.getDetail(imdbID).get(dbHelper.writerColumn));
            actor.setText(dbHelper.getDetail(imdbID).get(dbHelper.actorsColumn));
            plot.setText(dbHelper.getDetail(imdbID).get(dbHelper.plotColumn));
            language.setText(dbHelper.getDetail(imdbID).get(dbHelper.languageColumn));
            awards.setText(dbHelper.getDetail(imdbID).get(dbHelper.awardsColumn));
            metascore.setText(dbHelper.getDetail(imdbID).get(dbHelper.metascoreColumn));
            imdbVotes.setText(dbHelper.getDetail(imdbID).get(dbHelper.imdbVotesColumn));
            type.setText(dbHelper.getDetail(imdbID).get(dbHelper.typeColumn));
            DVDs.setText(dbHelper.getDetail(imdbID).get(dbHelper.dVDColumn));
            BoxOffice.setText(dbHelper.getDetail(imdbID).get(dbHelper.boxOfficeColumn));
            Production.setText(dbHelper.getDetail(imdbID).get(dbHelper.productionColumn));
            Website.setText(dbHelper.getDetail(imdbID).get(dbHelper.websiteColumn));
            imdbRating.setText("Rate: "+dbHelper.getDetail(imdbID).get(dbHelper.imdbRatingColumn));



            Picasso.with(DetailActivity.this).load(dbHelper.getDetail(imdbID).get(dbHelper.posterColumn)).networkPolicy(NetworkPolicy.OFFLINE).into(poster);
        }
        else if (isOnline()) {
            getDetail();
        } else {
            dialogNoInternet();
        }


    }

    public void init() {

        dbHelper = new DbHelper(DetailActivity.this);
        apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        name = findViewById(R.id.name);
        year_country = findViewById(R.id.year_country);
        rated = findViewById(R.id.rated);
        genre = findViewById(R.id.Genr);
        runtime=findViewById(R.id.Runtim);
        poster=findViewById(R.id.poster_image);
        director = findViewById(R.id.director);
        writer=findViewById(R.id.writer);
        actor=findViewById(R.id.actors);
        plot=findViewById(R.id.plot);
        language = findViewById(R.id.language);
        awards = findViewById(R.id.awards);
        metascore = findViewById(R.id.metascore);
        imdbVotes = findViewById(R.id.imdbVotes);
        type=findViewById(R.id.type);
        DVDs=findViewById(R.id.DVDs);
        BoxOffice = findViewById(R.id.BoxOffice);
        Production=findViewById(R.id.Production);
        Website=findViewById(R.id.Website);
        imdbRating=findViewById(R.id.imdbRating);

    }

    public void getDetail() {
        Call<Details> call = apiInterface.getDetails("3e974fca", imdbID);
        call.enqueue(new retrofit2.Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {


                Details resource = response.body();
                name.setText(resource.getTitle());
                year_country.setText(resource.getCountry()+" / "+ resource.getReleased());
                genre.setText(resource.getGenre());
                runtime.setText(resource.getRuntime());
                rated.setText(resource.getRated());
                director.setText(resource.getDirector());
                writer.setText(resource.getWriter());
                actor.setText(resource.getActors());
                plot.setText(resource.getPlot());
                language.setText(resource.getLanguage());
                awards.setText(resource.getAwards());
                metascore.setText(resource.getMetascore());
                imdbVotes.setText(resource.getImdbVotes());
                type.setText(resource.getType());
                DVDs.setText(resource.getDVD());
                BoxOffice.setText(resource.getBoxOffice());
                Production.setText(resource.getProduction());
                Website.setText(resource.getWebsite());
                imdbRating.setText("Rate: "+resource.getImdbRating());

                Picasso.with(DetailActivity.this)
                        .load(resource.getPoster())
                        .into(poster);



                dbHelper.insertDataDetail(resource.getTitle(),
                        resource.getYear(),
                        resource.getRated(),
                        resource.getReleased(),
                        resource.getRuntime(),
                        resource.getGenre(),
                        resource.getDirector(),
                        resource.getWriter(),
                        resource.getActors(),
                        resource.getPlot(),
                        resource.getLanguage(),
                        resource.getCountry(),
                        resource.getAwards(),
                        resource.getPoster(),
                        resource.getMetascore(),
                        resource.getImdbRating(),
                        resource.getImdbVotes(),
                        resource.getImdbID(),
                        resource.getType(),
                        resource.getDVD(),
                        resource.getBoxOffice(),
                        resource.getProduction(),
                        resource.getWebsite(),
                        resource.getResponse()
                      );

            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {

                dialogNoInternet();
                call.cancel();
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void dialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);

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
