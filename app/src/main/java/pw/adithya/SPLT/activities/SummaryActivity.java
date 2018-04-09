package pw.adithya.SPLT.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Toolbar;

import java.util.ArrayList;

import pw.adithya.SPLT.adapters.ExtrasAdapter;
import pw.adithya.SPLT.adapters.SummaryAdapter;
import pw.adithya.SPLT.objects.Participant;
import pw.adithya.splitandpay.R;

public class SummaryActivity extends AppCompatActivity {
    public static double extras;
    public static ArrayList<Participant> participantsArrayList;
    public static String billName;
    SummaryAdapter summaryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_summary);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_summary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        summaryAdapter = new SummaryAdapter(participantsArrayList, this, extras);
        recyclerView.setAdapter(summaryAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(billName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            startActivity(new Intent(SummaryActivity.this, CreateActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
