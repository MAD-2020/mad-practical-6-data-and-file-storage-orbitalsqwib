package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page
     */

    // Data Store
    public ArrayList<Integer> levels;
    public ArrayList<Integer> scores;
    public UserData userData;

    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public CustomScoreAdaptor(UserData userdata){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        this.levels = userdata.getLevels();
        this.scores = userdata.getScores();
        this.userData = userdata;
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layout is to be once the viewholder is created.
         */

        CustomScoreViewHolder viewHolder;

        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.level_select,
                parent,
                false
        );

        viewHolder = new CustomScoreViewHolder(item);
        return viewHolder;
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */

        String descString = "Level " + levels.get(position).toString();
        holder.LevelDescTextView.setText(descString);
        String scoreString = "High Score: " + scores.get(position).toString();
        holder.LevelScoreTextView.setText(scoreString);

        Log.v(TAG, FILENAME + " Showing level " + levels.get(position) + " with highest score: " + scores.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + userData.getMyUserName());
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return scores.size();
    }
}