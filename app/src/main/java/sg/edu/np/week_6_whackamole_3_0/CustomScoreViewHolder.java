package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreViewHolder extends RecyclerView.ViewHolder {
    /* Hint:
        1. This is a customised view holder for the recyclerView list @ levels selection page
     */

    // View objects
    ConstraintLayout ItemBaseConstraintLayout;
    CardView ItemBaseCardView;
    ConstraintLayout ItemCardConstraintLayout;
    TextView LevelDescTextView;
    TextView LevelScoreTextView;

    private static final String FILENAME = "CustomScoreViewHolder.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public CustomScoreViewHolder(final View itemView){
        super(itemView);

        /* Hint:
        This method dictates the viewholder contents and links the widget to the objects for manipulation.
         */

        ItemBaseConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.itemBaseConstraintLayout);
        ItemBaseCardView = (CardView) itemView.findViewById(R.id.itemBaseCardView);
        ItemCardConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.itemCardConstraintLayout);
        LevelDescTextView = (TextView) itemView.findViewById(R.id.levelDescTextView);
        LevelScoreTextView = (TextView) itemView.findViewById(R.id.levelScoreTextView);
    }
}
