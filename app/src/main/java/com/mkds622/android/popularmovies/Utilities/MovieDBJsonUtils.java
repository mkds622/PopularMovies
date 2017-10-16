package com.mkds622.android.popularmovies.Utilities;

import android.view.View;
import android.widget.TextView;

import com.mkds622.android.popularmovies.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Meetu on 03/10/17.
 */

public class MovieDBJsonUtils {

    public static MovieItem[] getMovieDataFromJSON(String jsonMovieString)throws JSONException {
        final String results="results";
        final String id="id";
        final String poster_path="poster_path";
        final String movieNameField="title";

        JSONObject J1 = new JSONObject(jsonMovieString);
        JSONArray J2 = J1.getJSONArray(results);
        MovieItem[] RESULT = new MovieItem[J2.length()];
        for (int i = 0; i < RESULT.length; ++i) {
            J1 = J2.getJSONObject(i);

            Long movie_id = Long.valueOf(J1.getString(id));
            String img_link = J1.getString(poster_path);
            String movie_name=J1.getString(movieNameField);
            img_link.replace('\\', '/');
            MovieItem temp = new MovieItem(img_link, movie_id, movie_name);
            RESULT[i] = temp;

        }

        return RESULT;
    }

    public static JSONObject getMovieDetailsFromJSONString(String jsonMovieDetail)throws JSONException {

        JSONObject J1= new JSONObject(jsonMovieDetail);
        return J1;
    }


}
