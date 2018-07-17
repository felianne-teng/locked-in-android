package com.felianneteng.lockedin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class TaskView extends AppCompatActivity {

    CheckBox taskCompletedCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.felianneteng.lockedin.R.layout.activity_task_view);

        taskCompletedCheckBox = findViewById(com.felianneteng.lockedin.R.id.taskCompletedCheckBox);
        taskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskCompletedCheckBox.setSelected(!taskCompletedCheckBox.isSelected());
            }
        });
    }
}
