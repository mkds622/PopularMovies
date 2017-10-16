package com.mkds622.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkds622.android.popularmovies.Utilities.MovieDBJsonUtils;
import com.mkds622.android.popularmovies.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.security.AccessController.getContext;

public class DetailsActivity extends AppCompatActivity {
    JSONObject movieDetails;
    URL img_url;
    private View mDetailView;
    private TextView mErrorMessageDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailView=(View)this.findViewById(R.id.movie_detail_view);
        mErrorMessageDisplay = (TextView)findViewById(R.id.error_msg_tv);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_details);
        String id=new String();

        Intent intent = this.getIntent();
        if(intent !=null && intent.hasExtra(Intent.EXTRA_TEXT) )
        {
            id=intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        URL movieDetailRequest=NetworkUtils.buildUrl(id,BuildConfig.MovieDB_Api_Key);

        try{
            if (isOnline()) {
                String jsonMovieDetailsString= NetworkUtils.getResponseFromHttpUrl(movieDetailRequest);

                movieDetails = MovieDBJsonUtils.getMovieDetailsFromJSONString(jsonMovieDetailsString);
                ImageView I1=(ImageView) this.findViewById(R.id.MoviePosterThumbnail);
                final String baseurl="http://image.tmdb.org/t/p/w780/";
                String img_link=movieDetails.getString("poster_path");
                String builduri="";
                if(img_link==null) {
                    I1.setImageResource(R.mipmap.image_not_found);
                }
                else {
                    builduri = baseurl + img_link;
                    try {
                        img_url = new URL(builduri.toString());
                        Picasso.with(this).load(img_url.toString()).into(I1);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                //setting Title
                TextView Title=(TextView)this.findViewById(R.id.Title);
                Title.setText(movieDetails.getString("title"));
                //setting Synopsis
                TextView Synopsis=(TextView)this.findViewById(R.id.Synopsis);
                Synopsis.setText(movieDetails.getString("overview"));
                //setting User Rating
                TextView UserRating=(TextView)this.findViewById(R.id.UserRating);
                UserRating.setText(movieDetails.getString("vote_average"));
                //setting Release Date
                TextView ReleaseDate=(TextView)this.findViewById(R.id.ReleaseDate);
                ReleaseDate.setText(movieDetails.getString("release_date"));

            }
            else{
                showErrorMessage();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void showErrorMessage(){
        mDetailView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
