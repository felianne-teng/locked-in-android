package com.felianneteng.lockedin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private List<Task> tasks;
    private Context context;

    public TaskAdapter(@NonNull Context context, int resource, @NonNull List<Task> objects) {
        super(context, resource, objects);
        this.context = context;
        this.tasks = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(com.felianneteng.lockedin.R.layout.activity_task_view, parent, false);
        TextView taskNameTextView = view.findViewById(com.felianneteng.lockedin.R.id.taskNameTextView);
        taskNameTextView.setText(tasks.get(position).getDescription());
        final CheckBox taskCompletedCheckBox = view.findViewById(com.felianneteng.lockedin.R.id.taskCompletedCheckBox);
        taskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tasks.get(position).setCompleted(taskCompletedCheckBox.isChecked());
            }
        });
        taskCompletedCheckBox.setChecked(tasks.get(position).isCompleted());
        return view;
    }
}
