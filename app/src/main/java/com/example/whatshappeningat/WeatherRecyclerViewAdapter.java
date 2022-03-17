package com.example.whatshappeningat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WeatherRecyclerViewAdapter";

    private ArrayList<String[]> weatherDetails;
    private Context context;

    public WeatherRecyclerViewAdapter(Context context,  ArrayList<String[]> weatherDetails ) {
        this.context = context;
        this.weatherDetails = weatherDetails;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "(onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String[] day = weatherDetails.get(position);

        holder.date.setText( day[0] );
        holder.temp.setText( day[1] );
        holder.description.setText( day[2]);
    }

    @Override
    public int getItemCount() {
        return weatherDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date, temp, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.textViewDate);
            temp = itemView.findViewById(R.id.textViewTemp);
            description = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
