package com.example.montour.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.montour.R;
import com.example.montour.callbacks.IOnMonumentData;
import com.example.montour.helpers.MonumentManager;
import com.example.montour.models.MonumentSelection;

public class MainActivity extends AppCompatActivity implements IOnMonumentData {
    private MonumentManager manager;
    private Intent outgoingIntent;
    private Button goToMonumentListBtn;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.manager = MonumentManager.getInstance(this, this);
        this.db = this.manager.getWritableDatabase();
        this.manager.loadMonuments(this.db);

        outgoingIntent = new Intent(this, ListActivity.class);
        this.goToMonumentListBtn = (Button)findViewById(R.id.goToMonumentList);

        this.goToMonumentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(outgoingIntent);

            }
        });

    }

    @Override
    public void dataChanged() {

        Log.v("dataChanged","All monument data has been loaded: "+this.manager.getmMonumentList().size());



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MonumentSelection.clearSelection();
        this.db.close();
    }
}
