package com.example.montour.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.montour.R;
import com.example.montour.callbacks.IOnMonumentData;
import com.example.montour.callbacks.IOnToggleClicked;
import com.example.montour.helpers.MonumentExistsInListException;
import com.example.montour.helpers.MonumentManager;
import com.example.montour.models.MonumentItem;
import com.example.montour.models.MonumentListAdapter;
import com.example.montour.models.MonumentSelection;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements IOnMonumentData, IOnToggleClicked {

    private RecyclerView recyclerView;

    private MonumentListAdapter monumentListAdapter;
    private ArrayList<MonumentItem> monumentItems = new ArrayList<>();
    private MonumentManager manager;
    private LinearLayoutManager layoutManager;
    private Intent outgoingIntent;
    private Button goToMapButton;
    private SQLiteDatabase db;
    private MonumentSelection selection;
    public boolean selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        this.recyclerView = (RecyclerView)findViewById(R.id.monu_list);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.goToMapButton = (Button)findViewById(R.id.go_to_map);
        this.selection = MonumentSelection.getInstance();

        this.manager = MonumentManager.getInstance(this, this);
        this.db = this.manager.getWritableDatabase();
        this.manager.loadMonumentsFromDb(this.db);
        this.monumentItems = this.manager.getmMonumentList();
        this.monumentListAdapter = new MonumentListAdapter(this, this.monumentItems, this);
        this.recyclerView.setAdapter(this.monumentListAdapter);
        this.setOnButtonClick();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.db.close();
    }

    @Override
    public void dataChanged() {
        this.monumentListAdapter.notifyDataSetChanged();
    }

    public void setOnButtonClick(){
        this.goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               outgoingIntent = new Intent(ListActivity.this, MapsActivity.class);
               startActivity(outgoingIntent);
            }
        });
    }

    @Override
    public void handleClickedToggle(Object o) {
        MonumentItem item = (MonumentItem) o;

        try {
            this.selection.addToMonumentList(item);
            Toast.makeText(this, "Monument added to selection.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            if(e instanceof MonumentExistsInListException)
                this.selection.removeFromMonumentList(item);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
