package eu.iums.fitwork;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class SportExerciseRecyclerAdapter extends RecyclerView.Adapter<SportExerciseRecyclerAdapter.RecyclerViewHolder>{
    /*
    SportExerciseRecyclerAdapter:
    Adapter für RecyclerView um Übungen darzustellen
     */

    Context context;
    ArrayList<Exercise> exercises;
    ExerciseDBHelper exHelper;

    //Übungen werden als ArrayList übergeben
    public SportExerciseRecyclerAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
        this.exHelper = new ExerciseDBHelper();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //CardView um einzelne Übungen anzuzeigen
        View v = LayoutInflater.from(context).inflate(R.layout.exercise_cardview, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SportExerciseRecyclerAdapter.RecyclerViewHolder holder, int position) {
        //Auslesen der einzelnen Übungen und setzen der Textviews
        Exercise exercise = exercises.get(position);

        holder.titleView.setText(exercise.getTitle());
        holder.fitpointsView.setText(String.valueOf(exercise.getFitpoints()));
        holder.categoryView.setText(exercise.getCategory() + ", Bereich: " + exercise.getArea());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExercisePlayerActivity.class);
                intent.putExtra(exHelper.DB_EXERCISETITLE, exercise.getTitle());
                intent.putExtra(exHelper.DB_EXERCISECATEGORY, exercise.getCategory());
                intent.putExtra(exHelper.DB_EXERCISEDESCRIPTION, exercise.getDescription());
                intent.putExtra(exHelper.DB_EXERCISEFITPOINTS, exercise.getFitpoints());
                intent.putExtra(exHelper.DB_EXERCISEURL, exercise.getUrl());
                intent.putExtra(exHelper.DB_EXERCISEAREA, exercise.getArea());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        //Initialiseren TextViews

        TextView titleView;
        TextView fitpointsView;
        TextView categoryView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.exerciseCardView_title);
            fitpointsView = itemView.findViewById(R.id.exerciseCardView_points);
            categoryView = itemView.findViewById(R.id.exerciseCardView_category);
        }
    }

}
