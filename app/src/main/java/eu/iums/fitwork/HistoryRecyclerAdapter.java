package eu.iums.fitwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.RecyclerViewHolder>{

    /*
    HistoryRecyclerAdapter:
    Adapter RecyclerView zur Darstellung der History
     */

    Context context;
    ArrayList<String> exercises;
    ArrayList<String> times;

    //Übungen und der Zeitpunkt, an dem diese ausgeführt werden, werden als ArrayList übergeben
    public HistoryRecyclerAdapter(Context context, ArrayList<String> exercises, ArrayList<String> times) {
        this.context = context;
        this.exercises = exercises;
        this.times = times;
    }

    @NonNull
    @Override
    public HistoryRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //CardView zur Darstellung der einzelnen abgschlossenen Übungen
        View v = LayoutInflater.from(context).inflate(R.layout.exercise_cardview, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerAdapter.RecyclerViewHolder holder, int position) {
        //Auslesen der Übungen und Zeiten und setzen der TextViews im CardView
        String exerciseName = exercises.get(position);
        String time = times.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date(Long.parseLong(time));

        holder.titleView.setText(exerciseName);
        holder.categoryView.setText(dateFormat.format(date));

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        //Definition TextViews
        TextView titleView;
        TextView fitpointsView;
        TextView categoryView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.exerciseCardView_title);
            fitpointsView = itemView.findViewById(R.id.exerciseCardView_points);
            fitpointsView.setVisibility(View.INVISIBLE);
            categoryView = itemView.findViewById(R.id.exerciseCardView_category);
        }
    }
}
