package pw.adithya.SPLT.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;
import pw.adithya.SPLT.adapters.SummaryAdapter;
import pw.adithya.SPLT.objects.Participant;
import pw.adithya.SPLT.R;

public class SummaryActivity extends AppCompatActivity {
    public static double extras;
    public static ArrayList<Participant> participantsArrayList;
    public static String billName, participants, bills, total;
    SummaryAdapter summaryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_summary);

        TextView participantsTextView, billsTextView, totalTextView, extrasTextView;

        participantsTextView = findViewById(R.id.textview_participants);
        billsTextView = findViewById(R.id.textview_bills);
        totalTextView = findViewById(R.id.textview_total);
        extrasTextView = findViewById(R.id.textview_extras);

        FButton doneButton = findViewById(R.id.cardview_done);

        participantsTextView.setText(participants);
        billsTextView.setText(bills);
        totalTextView.setText(total);
        extrasTextView.setText(String.valueOf(extras));

        RecyclerView recyclerView = findViewById(R.id.recyclerview_summary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        summaryAdapter = new SummaryAdapter(participantsArrayList, this, extras);
        recyclerView.setAdapter(summaryAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(billName);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
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
