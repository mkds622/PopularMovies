package com.mkds622.android.popularmovies.Utilities;

import android.net.Uri;
import android.support.compat.BuildConfig;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Meetu on 03/10/17.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String Api_key_param = "api_key";

    private static String apikey = null;

    public static URL buildUrl(String sortCategory,String apiKey){
        apikey=apiKey;
        String Url=BASE_URL+sortCategory;
        Uri builturi = Uri.parse(Url).buildUpon()
                .appendQueryParameter(Api_key_param, apikey).build();
        URL url=null;

        try {
            url=new URL(builturi.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();;
        }
        Log.v(LOG_TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        BufferedReader reader=null;
        try{
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
            // But it does make debugging a *lot* easier if you print out the completed
            // buffer for debugging.
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        String jsonMovieString = buffer.toString();

        return jsonMovieString;
        } catch (IOException e) {
            Log.w(LOG_TAG, "Error ", e);
            return null;
        } finally {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.w(LOG_TAG, "Error closing stream", e);
            }
        }
        }
    }

}
