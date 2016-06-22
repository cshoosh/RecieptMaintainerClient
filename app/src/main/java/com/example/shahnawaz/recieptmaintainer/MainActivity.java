package com.example.shahnawaz.recieptmaintainer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shahnawaz.recieptmaintainer.adapter.ViewPagerAdapter;
import com.example.shahnawaz.recieptmaintainer.model.CalculateModel;
import com.example.shahnawaz.recieptmaintainer.retro.API;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    public static CalculateModel calculateModel = new CalculateModel(0, 0, 0);
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateFrag frag = UpdateFrag.newInstance(0, mPager.getCurrentItem(), 0, "");
                frag.show(getSupportFragmentManager(), null);
            }
        });

        TabLayout tabs = (TabLayout) findViewById(android.R.id.tabs);
        mPager = (ViewPager) findViewById(android.R.id.tabhost);
        assert tabs != null && mPager != null;

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(mPager);
    }

    public void showMessage() {
        Snackbar.make(findViewById(R.id.coordinator), "No data available", Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void refresh() {
        new API().getCalculatedData(new API.ListResponse<CalculateModel>() {
            @Override
            public void onResponse(CalculateModel data) {
                calculateModel = data;
                TextView textView = (TextView) findViewById(android.R.id.text1);
                textView.setText("Difference: " + NUMBER_FORMAT.format(Math.abs(calculateModel.getDiff()))
                        + " " + (calculateModel.getDiff() < 0 ? "Over" : "Remaining"));
                mPagerAdapter.refresh();
            }

            @Override
            public void onFailure() {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static ProgressBar mProgressBar;

    public static ProgressBar getProgressBar() {
        return mProgressBar;
    }
}
