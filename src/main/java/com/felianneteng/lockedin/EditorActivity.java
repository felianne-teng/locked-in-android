package com.felianneteng.lockedin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity {

    @BindView(com.felianneteng.lockedin.R.id.backButton) Button backButton;
    @BindView(com.felianneteng.lockedin.R.id.settingsButton) Button settingsButton;
    @BindView(com.felianneteng.lockedin.R.id.textArea) EditText textArea;
    @BindView(com.felianneteng.lockedin.R.id.progress) TextView progress;
    @BindView(com.felianneteng.lockedin.R.id.lockButton) Button lockButton;
    int numWords;
    int wordLimit;
    String lastScreen;
    boolean locked;
    boolean showProgress;
    String typed;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.felianneteng.lockedin.R.layout.activity_editor);

        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        wordLimit = bundle.getInt("wordlim");
        numWords = bundle.getInt("numwrds");
        lastScreen = bundle.getString("lastscrn");
        locked = bundle.getBoolean("lock");
        showProgress = bundle.getBoolean("progcount");
        typed = bundle.getString("typed");
        username = bundle.getString("username");
        progress.setText(numWords + "/" + wordLimit);

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

        textArea.setText(typed);

        if(showProgress)
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);

        if(locked)
            lockButton.setEnabled(false);
        else
            lockButton.setEnabled(true);

        textArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                typed = editable.toString();
                numWords = 0;
                String[] words = typed.split(" ");

                // check if words are words and remove empty strings
                for (int i = 0; i<words.length; i++)
                {
                    if (words[i]!=null)
                    {
                        if (!words[i].equals(" ") && !words[i].equals(""))
                            numWords++;
                    }
                }

                if (words[0]==null || words[0].equals(""))
                    numWords = 0;

                progress.setText(numWords + "/" + wordLimit);
                if (wordLimit!=0) {
                    if (numWords / wordLimit >= 1) {
                        lockButton.setEnabled(true);
                        locked = false;
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dref = database.getReference(username);
                        dref.child("wordLimit").setValue(wordLimit);
                        dref.child("numWords").setValue(numWords);
                        dref.child("showProgress").setValue(showProgress);
                        dref.child("typed").setValue(typed);
                        dref.child("locked").setValue(locked);
                        stopLockTask();
                    }
                }
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockButton.setEnabled(false);
                locked = true;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dref = database.getReference(username);
                dref.child("wordLimit").setValue(wordLimit);
                dref.child("numWords").setValue(numWords);
                dref.child("showProgress").setValue(showProgress);
                dref.child("typed").setValue(typed);
                dref.child("locked").setValue(locked);
                startLockTask();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, com.felianneteng.lockedin.MainActivity.class);
                intent.putExtra("lastscrn", "Editor");
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
                Intent intent = new Intent(EditorActivity.this, com.felianneteng.lockedin.SettingsActivity.class);
                intent.putExtra("lastscrn", "Editor");
                intent.putExtra("wordlim", wordLimit);
                intent.putExtra("numwrds", numWords);
                intent.putExtra("lock", locked);
                intent.putExtra("progcount", showProgress);
                intent.putExtra("typed", typed);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
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
