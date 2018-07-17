package com.felianneteng.lockedin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(com.felianneteng.lockedin.R.id.wordLimitEditText) EditText wordLimitEditText;
    int wordLimit;
    int numWords;
    String lastScreen;
    @BindView(com.felianneteng.lockedin.R.id.backButton) Button backButton;
    @BindView(com.felianneteng.lockedin.R.id.warningTextView) TextView warningTextView;
    @BindView(com.felianneteng.lockedin.R.id.useridTextView) TextView useridTextView;
    boolean locked;
    boolean showProgress;
    @BindView(com.felianneteng.lockedin.R.id.showProgressSwitch) Switch showProgressSwitch;
    String typed;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.felianneteng.lockedin.R.layout.activity_settings);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        wordLimit = bundle.getInt("wordlim");
        numWords = bundle.getInt("numwrds");
        lastScreen = bundle.getString("lastscrn");
        locked = bundle.getBoolean("lock");
        typed = bundle.getString("typed");
        showProgress = bundle.getBoolean("progcount");
        username = bundle.getString("username");
        wordLimitEditText.setText("" + wordLimit);
        showProgressSwitch.setChecked(showProgress);

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

        useridTextView.setText("User ID: " + username);

        if (locked)
        {
            wordLimitEditText.setEnabled(false);
        }
        else
            wordLimitEditText.setEnabled(true);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wordLimitEditText.getText().toString().equals("")) {
                    if (lastScreen.equals("Home")) {
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        intent.putExtra("lastscrn", "Settings");
                        intent.putExtra("wordlim", wordLimit);
                        intent.putExtra("numwrds", numWords);
                        intent.putExtra("lock", locked);
                        intent.putExtra("progcount", showProgress);
                        intent.putExtra("typed", typed);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    else if (lastScreen.equals("Editor"))
                    {
                        Intent intent = new Intent(SettingsActivity.this, EditorActivity.class);
                        intent.putExtra("lastscrn", "Settings");
                        intent.putExtra("wordlim", wordLimit);
                        intent.putExtra("numwrds", numWords);
                        intent.putExtra("lock", locked);
                        intent.putExtra("progcount", showProgress);
                        intent.putExtra("typed", typed);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    else if (lastScreen.equals("TaskList"))
                    {
                        Intent intent = new Intent(SettingsActivity.this, TasksActivity.class);
                        intent.putExtra("lastscrn", "Settings");
                        intent.putExtra("wordlim", wordLimit);
                        intent.putExtra("numwrds", numWords);
                        intent.putExtra("lock", locked);
                        intent.putExtra("progcount", showProgress);
                        intent.putExtra("typed", typed);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    else if (lastScreen.equals("Connect"))
                    {
                        Intent intent = new Intent(SettingsActivity.this, TasksActivity.class);
                        intent.putExtra("lastscrn", "Settings");
                        intent.putExtra("wordlim", wordLimit);
                        intent.putExtra("numwrds", numWords);
                        intent.putExtra("lock", locked);
                        intent.putExtra("progcount", showProgress);
                        intent.putExtra("typed", typed);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                }
                else
                    warningTextView.setVisibility(View.VISIBLE);
            }
        });

        wordLimitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                warningTextView.setVisibility(View.GONE);
                if (!wordLimitEditText.getText().toString().equals(""))
                    wordLimit = Integer.parseInt(wordLimitEditText.getText().toString());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dref = database.getReference(username);
                dref.child("wordLimit").setValue(wordLimit);
                dref.child("numWords").setValue(numWords);
                dref.child("showProgress").setValue(showProgress);
                dref.child("typed").setValue(typed);
                dref.child("locked").setValue(locked);
            }
        });

        showProgressSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showProgress = b;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dref = database.getReference(username);
                dref.child("wordLimit").setValue(wordLimit);
                dref.child("numWords").setValue(numWords);
                dref.child("showProgress").setValue(showProgress);
                dref.child("typed").setValue(typed);
                dref.child("locked").setValue(locked);
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
