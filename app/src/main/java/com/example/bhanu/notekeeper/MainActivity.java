package com.example.bhanu.notekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Create by Megan Reiffer
 */
public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button addButton;
    EditText titleEditText;
    Spinner prioritySpinner;
    RealmResults<Note> notes;
    RecyclerView recyclerView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =(Toolbar)findViewById(R.id.tool_bar);
        addButton =(Button) findViewById(R.id.button);
        titleEditText =(EditText)findViewById(R.id.editText);
        prioritySpinner =(Spinner)findViewById(R.id.spinner);
        recyclerView =(RecyclerView)findViewById(R.id.RecyclerView);
        setSupportActionBar(toolbar);
        setTitle("NoteKeeper");


// Initialize Realm
        Realm.init(this /*context*/);

// Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title= titleEditText.getText().toString();
                titleEditText.setText("");
                String p= prioritySpinner.getSelectedItem().toString();



                String key = UUID.randomUUID().toString();
                Note note=new Note(key,p,"pending",title, Calendar.getInstance().getTime());

                realm.beginTransaction();

                realm.copyToRealm(note);

                realm.commitTransaction();

                recyclerView.setAdapter(new NoteAdapter(getApplicationContext(),notes));

            }
        });

        notes = realm.where(Note.class).findAll().sort("status");

        recyclerView.setAdapter(new NoteAdapter(MainActivity.this,notes));


        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm otherRealm) {

                notes = otherRealm.where(Note.class).findAll().sort("status");

                recyclerView.setAdapter(new NoteAdapter(MainActivity.this,notes));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.all:


                notes = realm.where(Note.class).findAll().sort("status");

                recyclerView.setAdapter(new NoteAdapter(this,notes));

                break;
            case R.id.completed:

                notes = realm.where(Note.class).equalTo("status", "completed").findAll();


                recyclerView.setAdapter(new NoteAdapter(this,notes));

                break;
            case R.id.Sortbytime:

                notes = notes.sort("upDateTime");

                recyclerView.setAdapter(new NoteAdapter(this,notes));


                break;

            case R.id.Sortbyprior:
                notes = notes.sort("priority");

                recyclerView.setAdapter(new NoteAdapter(this,notes));

                break;

            case R.id.pending:
                notes = realm.where(Note.class).equalTo("status", "pending").findAll();

                recyclerView.setAdapter(new NoteAdapter(this,notes));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
