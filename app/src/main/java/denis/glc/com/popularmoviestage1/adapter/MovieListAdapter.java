package denis.glc.com.popularmoviestage1.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import denis.glc.com.popularmoviestage1.DetailsActivity;
import denis.glc.com.popularmoviestage1.R;
import denis.glc.com.popularmoviestage1.app.Config;
import denis.glc.com.popularmoviestage1.listener.ItemOnClickListener;
import denis.glc.com.popularmoviestage1.model.MovieModel;

/**
 * Created by moham on 06/07/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context context;
    List<MovieModel> movieList;

    public MovieListAdapter(List<MovieModel> movieList, Context context){
        super();
        //Getting all the superheroes
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MovieModel movie =  movieList.get(position);
        holder.textTitle.setText(movie.getTitle());
        Context contextHolder = holder.imageMovie.getContext();
        Glide.with(contextHolder).load(Config.URL_PATH+movie.getPosterPath())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageMovie);
        holder.setClickListener(new ItemOnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailCatalog = new Intent(context, DetailsActivity.class);
                detailCatalog.putExtra(Config.ID_MOVIE, movie.getIdMovie());
                context.startActivity(detailCatalog);
            }
        });
    }
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageMovie;
        public TextView textTitle;
        public CheckBox checkBoxFav;
        private ItemOnClickListener clickListener;

        public ViewHolder(View itemView){
            super(itemView);
            imageMovie = (ImageView) itemView.findViewById(R.id.poster);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemOnClickListener itemOnclickListener){
            this.clickListener = itemOnclickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view);
        }
    }
}