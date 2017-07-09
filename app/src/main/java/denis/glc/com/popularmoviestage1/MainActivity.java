package denis.glc.com.popularmoviestage1;

import android.app.ProgressDialog;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import denis.glc.com.popularmoviestage1.adapter.MovieListAdapter;
import denis.glc.com.popularmoviestage1.app.Config;
import denis.glc.com.popularmoviestage1.model.MovieModel;

public class MainActivity extends AppCompatActivity {

    private List<MovieModel> movieList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        movieList = new ArrayList<>();

        adapter = new MovieListAdapter(movieList, getApplicationContext());
//      recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fetchDataMovies("now_playing");
        setTitle("List Movie");
    }

    private void fetchDataMovies(String movieReq) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.themoviedb.org/3/movie/"+ movieReq +"?api_key=xxx",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, getString(R.string.networkError), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(MainActivity.this, getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(MainActivity.this, getString(R.string.authFailureError), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(MainActivity.this, getString(R.string.parseError), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(MainActivity.this, getString(R.string.noConnectionError), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(MainActivity.this, getString(R.string.timeoutError), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);

    }

    private void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.RESULTS);
            if (result.length() > 0) {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject data = result.getJSONObject(i);
                    MovieModel movie = new MovieModel();
                    movie.setIdMovie(data.getString(Config.ID_MOVIE));
                    movie.setPosterPath(data.getString(Config.POSTERPATH));
                    movie.setOverView(data.getString(Config.OVERVIEW));
                    movie.setReleaseDate(data.getString(Config.RELEASE_DATE));
                    movie.setTitle(data.getString(Config.TITLE));
                    movie.setBackdropPath(data.getString(Config.BACKDROP_PATH));
                    movie.setPopularity(data.getString(Config.POPULARITY));
                    movie.setVoteCount(data.getString(Config.VOTE_COUNT));
                    movie.setVoteAverage(data.getString(Config.VOTE_AVERAGE));

                    movieList.add(movie);

                }
                adapter = new MovieListAdapter(movieList, MainActivity.this);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "Movie Not Available", Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popularity) {
            movieList.clear();
            fetchDataMovies("popular");
            setTitle("Popularity");
        } else {
            movieList.clear();
            fetchDataMovies("top_rated");
            setTitle("Top Ratings");
        }

        return super.onOptionsItemSelected(item);
    }
}
