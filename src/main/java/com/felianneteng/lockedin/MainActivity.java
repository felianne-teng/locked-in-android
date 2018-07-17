package com.felianneteng.lockedin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(com.felianneteng.lockedin.R.id.editorButton) Button editorButton;
    @BindView(com.felianneteng.lockedin.R.id.listButton) Button listButton;
    @BindView(com.felianneteng.lockedin.R.id.settingsButton) Button settingsButton;
    //@BindView(R.id.connectButton) Button connectButton;
    int wordLimit;
    int numWords;
    String lastScreen;
    boolean locked;
    boolean showProgress;
    String typed;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.felianneteng.lockedin.R.layout.activity_main);

        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString("username", null) == null)
        {
            username = UUID.randomUUID().toString().substring(0, 6);
            editor.putString("username", username);
            editor.apply();
        }
        else
            username = sharedPreferences.getString("username", null);

        if (lastScreen==null)
        {
            locked = false;
            wordLimit = 500;
            numWords = 0;
            lastScreen = "Home";
            showProgress = true;
            typed = "";
        }
        else {
            Bundle bundle = getIntent().getExtras();
            wordLimit = bundle.getInt("wordlim");
            numWords = bundle.getInt("numwrds");
            lastScreen = bundle.getString("lastscrn");
            locked = bundle.getBoolean("lock");
            showProgress = bundle.getBoolean("progcount");
            typed = bundle.getString("typed");
            username = bundle.getString("username");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dref = database.getReference(username);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("wordLimit").getValue()!=null)
                    wordLimit = ((Long)dataSnapshot.child("wordLimit").getValue()).intValue();
                if (dataSnapshot.child("numWords").getValue()!=null)
                    numWords = ((Long)dataSnapshot.child("numWords").getValue()).intValue();
                if (dataSnapshot.child("showProgress").getValue()!=null)
                    showProgress = (Boolean)dataSnapshot.child("showProgress").getValue();
                if (dataSnapshot.child("typed").getValue()!=null)
                    typed = (String)dataSnapshot.child("typed").getValue();
                if (dataSnapshot.child("locked").getValue()!=null)
                    locked = (Boolean)dataSnapshot.child("locked").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("lastscrn", "Home");
                intent.putExtra("wordlim", wordLimit);
                intent.putExtra("numwrds", numWords);
                intent.putExtra("lock", locked);
                intent.putExtra("progcount", showProgress);
                intent.putExtra("typed", typed);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TasksActivity.class);
                intent.putExtra("lastscrn", "Home");
                intent.putExtra("wordlim", wordLimit);
                intent.putExtra("numwrds", numWords);
                intent.putExtra("lock", locked);
                intent.putExtra("progcount", showProgress);
                intent.putExtra("typed", typed);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("lastscrn", "Home");
                intent.putExtra("wordlim", wordLimit);
                intent.putExtra("numwrds", numWords);
                intent.putExtra("lock", locked);
                intent.putExtra("progcount", showProgress);
                intent.putExtra("typed", typed);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        /**
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                intent.putExtra("lastscrn", "Home");
                intent.putExtra("wordlim", wordLimit);
                intent.putExtra("numwrds", numWords);
                intent.putExtra("lock", locked);
                intent.putExtra("progcount", showProgress);
                intent.putExtra("typed", typed);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
         */
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dref = database.getReference(username);
        dref.child("wordLimit").setValue(wordLimit);
        dref.child("numWords").setValue(numWords);
        dref.child("showProgress").setValue(showProgress);
        dref.child("typed").setValue(typed);
        dref.child("locked").setValue(locked);
    }

}
