package com.mkds622.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mkds622.android.popularmovies.Utilities.MovieDBJsonUtils;
import com.mkds622.android.popularmovies.Utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    Toast movieToast;

    @Override
    public void onStart()
    {
        super.onStart();
        loadMovieData();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView=(RecyclerView)findViewById(R.id.moviep_recycler_view);

        mErrorMessageDisplay = (TextView)findViewById(R.id.error_msg_tv);

        StaggeredGridLayoutManager layoutManager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this.getApplicationContext(),this);

        mRecyclerView.setAdapter(mMovieAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public void onClick(MovieItem movieSelected) {
        if(movieToast!=null){
            movieToast.cancel();
        }
        Context context=this;
        movieToast=Toast.makeText(context,movieSelected.movieName,Toast.LENGTH_LONG);
        movieToast.show();
        Intent intent=new Intent(this,DetailsActivity.class).putExtra(Intent.EXTRA_TEXT,String.valueOf(movieSelected.movieID));
        startActivity(intent);
    }

    private void loadMovieData(){
        showMovieDataView();
        String filter="";

        RadioGroup radioGroup=(RadioGroup)this.findViewById(R.id.RadioSort);
        switch(radioGroup.getCheckedRadioButtonId()){
            case R.id.popular_radio:
                filter="popular";
                break;

            case R.id.toprated_radio:
                filter="top_rated";
                break;
        };

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(RadioGroup group, int checkedId)
                                                  {
                                                      switch(checkedId) {
                                                          case R.id.popular_radio:
                                                              loadMovieData();
                                                              break;

                                                          case R.id.toprated_radio:
                                                              loadMovieData();
                                                              break;
                                                      }
                                                  }
                                              }
        );

        new ExtractMovieTask().execute(filter);
    }

    private void showMovieDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class ExtractMovieTask extends AsyncTask<String,Void,MovieItem[]>{
        private final String LOG_TAG =ExtractMovieTask.class.getSimpleName();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected MovieItem[] doInBackground(String...params){
            if(params.length == 0){
                return null;
            }
            String filter = params[0];
            String apiKey = BuildConfig.MovieDB_Api_Key;

            URL movieRequestUrl= NetworkUtils.buildUrl(filter,apiKey);

            try{
                if(isOnline()) {
                    String jsonMovieString = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                    MovieItem[] moviesArray = MovieDBJsonUtils.getMovieDataFromJSON(jsonMovieString);

                    return moviesArray;
                }
                else return null;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(MovieItem[] movieItems){
            mProgressBar.setVisibility(View.INVISIBLE);

            if(movieItems != null){
                showMovieDataView();
                mMovieAdapter.setMovieData(movieItems);
            }
            else{
                showErrorMessage();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();

        inflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.refresh){
            mMovieAdapter.setMovieData(null);
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
