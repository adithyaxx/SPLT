package pw.adithya.SPLT.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import pw.adithya.SPLT.ItemOffsetDecoration;
import pw.adithya.SPLT.R;
import pw.adithya.SPLT.adapters.MainAdapter;
import pw.adithya.SPLT.objects.Combined;
import pw.adithya.SPLT.objects.Participant;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ArrayList<Combined> combinedArrayList;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        combinedArrayList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        gson = new Gson();
        Type combinedArrayListType = new TypeToken<ArrayList<Combined>>(){}.getType();
        combinedArrayList = gson.fromJson(sharedPreferences.getString("combinedArrayList", ""), combinedArrayListType);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SPLT");

        RecyclerView mainRecyclerView = findViewById(R.id.recyclerview_main);
        MainAdapter mainAdapter = new MainAdapter(combinedArrayList, this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mainRecyclerView.addItemDecoration(itemDecoration);
        mainRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setAdapter(mainAdapter);
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
            startActivity(new Intent(MainActivity.this, CreateActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
