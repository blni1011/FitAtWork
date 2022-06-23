package eu.iums.fitwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    /*
    RecyclerAdpater:
    Darstellung des Leaderboards in einem RecyclerView
    Alle User werden als ArrayList übergeben
     */

    Context context;
    ArrayList<User> users;
    private UserDBHelper userDB;

    public RecyclerAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        this.userDB = new UserDBHelper();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Laden des CardView zur Darstellung der einzelnen User
        View v = LayoutInflater.from(context).inflate(R.layout.userslist_leaderboard, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        //Es werden nur User angezeigt, die das Leaderboard aktiviert haben. Auslesen der Darten aus dem Userobjekt
        User user = users.get(position);
        if(user.isLeaderboardActive()) {
            holder.username.setText(user.getUsername());
            holder.fitPoints.setText(String.valueOf(user.getFitPoints()));
            holder.rank.setText(String.valueOf(position + 1));
            userDB.getProfilePicture(user.getUsername(), holder.profilepic);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        //Initialisierung TextViews
        TextView username;
        TextView fitPoints;
        TextView rank;
        ShapeableImageView profilepic;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.cardview_username);
            fitPoints = itemView.findViewById(R.id.cardview_points);
            rank = itemView.findViewById(R.id.cardview_rank);
            profilepic = itemView.findViewById(R.id.cardview_profilepic);
        }
    }
}
