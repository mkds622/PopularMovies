package com.mkds622.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Meetu on 03/10/17.
 */

public class MovieItem implements Parcelable {
    private final String LOG_TAG = MovieItem.class.getSimpleName();

    URL imageUrl;
    String imageLink,movieName;
    long movieID;
    static final String baseURL="http://image.tmdb.org/t/p/w185/";

    public MovieItem(String imageLink,long movieID,String movieName) throws NullPointerException{
        this.imageLink=imageLink;
        this.movieID=movieID;
        this.movieName=movieName;
        if(imageLink==null){
            imageUrl=null;
        }else{
            String builtUri=baseURL+imageLink;
            try{
                imageUrl=new URL(builtUri);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        }
    }

    private MovieItem(Parcel in){
        this.imageLink=in.readString();
        this.movieID=in.readLong();
        this.movieName=in.readString();
        if(imageLink==null){
            imageUrl=null;
        }else{
            String builtUri=baseURL+imageLink;
            try{
                imageUrl=new URL(builtUri);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        }
        Log.w(LOG_TAG,"URL:"+imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString(){ return this.imageUrl + "--" + this.movieID;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageLink);
        dest.writeLong(movieID);
        dest.writeString(movieName);
    }

    public final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>(){

        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
