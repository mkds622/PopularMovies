package com.mkds622.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Meetu on 03/10/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Context context;
    private List<MovieItem> mMovieList;


    private final MovieAdapterOnClickHandler mMovieClickHandler;



    public interface MovieAdapterOnClickHandler{
        void onClick(MovieItem movieSelected);
    }

    public MovieAdapter(Context context,MovieAdapterOnClickHandler handler){
        this.context=context;
        this.mMovieClickHandler=handler;
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mMovieItemImageView;
        public final TextView mMovieItemTextView;
        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieItemImageView=(ImageView)itemView.findViewById(R.id.movie_poster);
            mMovieItemTextView=(TextView) itemView.findViewById(R.id.movie_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition=getAdapterPosition();
            MovieItem movieSelected=mMovieList.get(adapterPosition);
            mMovieClickHandler.onClick(movieSelected);
        }
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        int layoutIdForListItem= R.layout.movie_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;

        View view=inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        MovieItem movieItem=mMovieList.get(position);
        Picasso.with(context).load(movieItem.imageUrl.toString()).into(holder.mMovieItemImageView);
        holder.mMovieItemTextView.setText(movieItem.movieName);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
