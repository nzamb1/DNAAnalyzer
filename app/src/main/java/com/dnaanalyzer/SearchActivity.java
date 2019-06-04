package com.dnaanalyzer;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dnaanalyzer.api.ApiResponse;
import com.dnaanalyzer.util.RxUtils;

import io.reactivex.disposables.Disposable;


public class SearchActivity extends BaseActivity {
    private ListView listView;
    private TextView textViewStatus;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = findViewById(R.id.lvSearchItems);
        textViewStatus = findViewById(R.id.tvStatus);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");

        if (RxUtils.isDisposed(disposable)) {
            searchItems("");
        }
    }

    @Override
    protected void onStop() {
        RxUtils.dispose(disposable);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchItems(s);
                return false;
            }
        });
        return result;
    }

    private void searchItems(String query) {
        RxUtils.dispose(disposable);
        String uid = ((DnaApplication) this.getApplication()).getUid();
        disposable = DnaApplication.get(this).getApi()
                .search(Constants.USER_NAME, query)
                .retry()
                .compose(RxUtils.applySchedulersToSingle())
                .doOnSubscribe(__ -> showLoading())
                .subscribe(this::showItems, e -> showStatus("Error: " + e.toString()));
    }

    private void showItems(ApiResponse apiResponse) {
        if (apiResponse.getSize() == 0) {
            showStatus("No items");
        } else {
            hideLoading();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, apiResponse.toStringList(100));

        listView.setAdapter(adapter);
    }

    private void showLoading() {
        listView.setVisibility(View.GONE);
        showStatus("Loading");
    }

    private void hideLoading() {
        hideStatus();
        listView.setVisibility(View.VISIBLE);
    }

    private void showStatus(String status) {
        textViewStatus.setVisibility(View.VISIBLE);
        textViewStatus.setText(status);
    }

    private void hideStatus() {
        textViewStatus.setVisibility(View.GONE);
    }
}
