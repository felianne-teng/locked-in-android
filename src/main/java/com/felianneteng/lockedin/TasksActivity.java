package com.felianneteng.lockedin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksActivity extends AppCompatActivity implements InputDescriptionDialogFragment.NoticeDialogListener {

    @BindView(com.felianneteng.lockedin.R.id.taskListView) ListView taskListView;
    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
    int wordLimit;
    int numWords;
    String lastScreen;
    @BindView(com.felianneteng.lockedin.R.id.backButton) Button backButton;
    boolean locked;
    boolean showProgress;
    String typed;
    @BindView(com.felianneteng.lockedin.R.id.settingsButton) Button settingsButton;
    @BindView(com.felianneteng.lockedin.R.id.addTaskButton) Button addTaskButton;
    @BindView(com.felianneteng.lockedin.R.id.removeTaskButton) Button removeTaskButton;
    int selectedTask = -1;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.felianneteng.lockedin.R.layout.activity_tasks);
        Bundle bundle = getIntent().getExtras();
        wordLimit = bundle.getInt("wordlim");
        numWords = bundle.getInt("numwrds");
        lastScreen = bundle.getString("lastscrn");
        locked = bundle.getBoolean("lock");
        showProgress = bundle.getBoolean("progcount");
        typed = bundle.getString("typed");
        username = bundle.getString("username");
        ButterKnife.bind(this);

        taskAdapter = new TaskAdapter(this, com.felianneteng.lockedin.R.layout.activity_task_view, tasks);
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
                taskAdapter.clear();
                for (DataSnapshot ds : dataSnapshot.child("tasks").getChildren())
                {
                    taskAdapter.add(ds.getValue(Task.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasksActivity.this, MainActivity.class);
                intent.putExtra("lastscrn", "TaskList");
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
                Intent intent = new Intent(TasksActivity.this, SettingsActivity.class);
                intent.putExtra("lastscrn", "TaskList");
                intent.putExtra("wordlim", wordLimit);
                intent.putExtra("numwrds", numWords);
                intent.putExtra("lock", locked);
                intent.putExtra("progcount", showProgress);
                intent.putExtra("typed", typed);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoticeDialog();
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTask = i;
            }
        });

        removeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTask>=0) {
                    taskAdapter.remove(taskAdapter.getItem(selectedTask));
                    selectedTask = -1;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dref = database.getReference(username);
                dref.child("tasks").setValue(tasks);
            }
        });
        taskListView.setAdapter(taskAdapter);
    }

    public void showNoticeDialog() {
        DialogFragment dialog = new InputDescriptionDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dref = database.getReference(username);
        taskAdapter.add(new Task(((InputDescriptionDialogFragment)dialog).getDescription(), false));
        dref.child("tasks").setValue(tasks);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                taskAdapter.clear();
                for (DataSnapshot ds : dataSnapshot.child("tasks").getChildren())
                {
                    taskAdapter.add(ds.getValue(Task.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        dref.child("tasks").setValue(tasks);
        dref.child("locked").setValue(locked);

    }
}
