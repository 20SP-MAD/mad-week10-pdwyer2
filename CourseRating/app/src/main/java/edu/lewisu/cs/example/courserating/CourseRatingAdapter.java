package edu.lewisu.cs.example.courserating;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CourseRatingAdapter {


    class RatingHolder extends RecyclerView.ViewHolder{
        private final TextView courseNameTextView;
        private final TextView courseRatingTextView;
        private final TextView instructorTextView;

        RatingHolder(View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            courseRatingTextView = itemView.findViewById(R.id.courseRatingTextView);
            instructorTextView = itemView.findViewById(R.id.courseInstructorTextView);

        }
    }


    private Context context;


}
