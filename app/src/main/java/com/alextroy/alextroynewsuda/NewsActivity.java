package com.alextroy.alextroynewsuda;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String USGS_REQUEST_URL = "http://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 1;
    private static final String PAGE_SIZE = "page-size";
    private static final String API_KEY = "api-key";
    private static final String KEY = "test";
    private static final String SHOW_FIELDS = "show-fields";
    private static final String THUMBNAIL_TRAIL_TEXT_BYLINE = "thumbnail,trailText,byline";
    private static final String NONE = "none";
    private static final String SECTION = "section";
    private static final String ORDER_BY = "order-by";
    private static final String NEWEST = "newest";
    private static final String RELEVANCE = "relevance";
    private static final String QUERY = "q";

    private ArrayList<News> news;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private NewsLoader loader;

    private TextView emptyStateTextView;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        news = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        emptyStateTextView = findViewById(R.id.empty_view);

        adapter = new NewsAdapter(this, news);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        loader = (NewsLoader) getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, searchResult(null));
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        emptyStateTextView.setText(R.string.no_news);
        news.clear();

        if (data != null && !data.isEmpty()) {
            news.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        news.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String searchResult(String query) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String pageSize = sharedPreferences.getString(getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));
        if (TextUtils.isEmpty(pageSize)) {
            pageSize = "0";
        }
        uriBuilder.appendQueryParameter(PAGE_SIZE, pageSize);
        uriBuilder.appendQueryParameter(API_KEY, KEY);
        uriBuilder.appendQueryParameter(SHOW_FIELDS, THUMBNAIL_TRAIL_TEXT_BYLINE);
        String section = sharedPreferences.getString(getString(R.string.settings_only_show_key), getString(R.string.settings_only_show_default));
        if (!section.equals(NONE)) {
            uriBuilder.appendQueryParameter(SECTION, section);
        }

        if (query == null) {
            uriBuilder.appendQueryParameter(ORDER_BY, NEWEST);
            return uriBuilder.toString();
        }
        uriBuilder.appendQueryParameter(ORDER_BY, RELEVANCE);
        uriBuilder.appendQueryParameter(QUERY, query);

        return uriBuilder.toString();
    }
}
