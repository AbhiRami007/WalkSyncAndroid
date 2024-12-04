package com.example.walksyncandroid.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walksyncandroid.Models.ActivityItem;
import com.example.walksyncandroid.R;

import java.util.Collections;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private final List<ActivityItem> activityItems;

    public ActivityAdapter(List<ActivityItem> activityItems) {
        this.activityItems = activityItems;
    }

    public ActivityAdapter() {
        activityItems = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityItem item = activityItems.get(position);
        // Set data to the TextViews based on the item's title
        switch (item.getTitle()) {
            case "Steps":
                holder.stepsValue.setText(item.getValue());
                break;

            case "Walking Distance":
                holder.walkingAverageValue.setText(item.getValue());
                break;

            case "Standing":
                holder.standingValue.setText(item.getValue());
                break;

            case "Avg. Speed":
                holder.avgSpeedValue.setText(item.getValue());
                break;

            case "Time":
                holder.time.setText(item.getValue());
                break;

            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return activityItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepsValue, walkingAverageValue, standingValue, avgSpeedValue, time;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize TextViews
            stepsValue = itemView.findViewById(R.id.steps_value);
            walkingAverageValue = itemView.findViewById(R.id.walking_average_value);
            standingValue = itemView.findViewById(R.id.standing_value);
            avgSpeedValue = itemView.findViewById(R.id.avg_speed_value);
            time = itemView.findViewById(R.id.time);
        }
    }
}
