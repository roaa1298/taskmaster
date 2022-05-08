package com.roaa.mytasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roaa.mytasks.data.Task;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {

    List<Task> dataList;
    TaskClickListener listener;

    public TaskRecyclerViewAdapter(List<Task> dataList, TaskClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(listItemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.body.setText(dataList.get(position).getBody());
        holder.state.setText(dataList.get(position).getState());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView body;
        TextView state;

        TaskClickListener listener;
        public TaskViewHolder(@NonNull View itemView, TaskClickListener listener) {
            super(itemView);

            this.listener = listener;

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            state = itemView.findViewById(R.id.state);

            itemView.setOnClickListener(view -> listener.onTaskItemClicked(getAdapterPosition()));
        }
    }
    public interface TaskClickListener {
        void onTaskItemClicked(int position);
    }
}
