package com.example.ratingapplication;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class GameRatingListAdapter extends FirebaseRecyclerAdapter<GameRating, GameRatingListAdapter.RatingHolder> {

    class RatingHolder extends RecyclerView.ViewHolder {
        final TextView gameNameTextView;
        final TextView gameProducerTextView;
        final TextView gameTypeTextView;
        final TextView gameMultiplayerTextView;
        final TextView gameRatingTextView;


        RatingHolder(View itemView) {
            super(itemView);
            gameNameTextView = itemView.findViewById(R.id.gameNameTextView);
            gameProducerTextView = itemView.findViewById(R.id.gameProducerTextView);
            gameTypeTextView = itemView.findViewById(R.id.gameTypeTextView);
            gameMultiplayerTextView = itemView.findViewById(R.id.gameMultiplayerTextView);
            gameRatingTextView = itemView.findViewById(R.id.gameRatingTextView);

        }

    }

    private Context context;

    public GameRatingListAdapter(@NonNull FirebaseRecyclerOptions options){
        super(options);
    }

    @NonNull
    @Override
    public RatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_row_listed,parent,false);
        return new GameRatingListAdapter.RatingHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RatingHolder holder, int position, @NonNull GameRating model) {
        holder.gameNameTextView.setText(model.getGameName());
        holder.gameProducerTextView.setText(model.getProducerName());
        holder.gameTypeTextView.setText(model.getGameGenre());
        holder.gameMultiplayerTextView.setText("Multi-player: " + Boolean.toString(model.isGameMultiplayer()));
        int rating = model.getRating();
        String ratingString = context.getResources().getQuantityString(R.plurals.star_rating, rating, rating);
        holder.gameRatingTextView.setText(ratingString);
    }
}
