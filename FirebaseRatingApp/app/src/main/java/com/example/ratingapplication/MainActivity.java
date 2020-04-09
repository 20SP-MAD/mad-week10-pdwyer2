package com.example.ratingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private GameRating gameRating;
    private EditText gameNameEditText;
    private EditText producerNameEditText;
    private Spinner gameTypeSpinner;
    private RatingBar gameRatingBar;
    private CheckBox checkboxMultiplayer;
    private Button submitButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("game_rating");

        gameRating = new GameRating();

        gameNameEditText = findViewById(R.id.gameNameEditText);
        gameNameEditText.addTextChangedListener(new NameTextListener(gameNameEditText));

        producerNameEditText = findViewById(R.id.producerNameEditText);
        producerNameEditText.addTextChangedListener(new NameTextListener(producerNameEditText));

        gameTypeSpinner = findViewById(R.id.gameTypeSpinner);
        gameTypeSpinner.setOnItemSelectedListener(new GameTypeSelectedListener());

        checkboxMultiplayer = findViewById(R.id.checkboxMultiplayer);
        checkboxMultiplayer.setOnClickListener(new MultiplayerCheckedListener());


        gameRatingBar = findViewById(R.id.ratingBar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ratingString = sharedPreferences.getString(getString(R.string.pref_rating_key),"0");
        int defaultRating = Integer.parseInt(ratingString);
        gameRatingBar.setRating(defaultRating);
        gameRatingBar.setOnRatingBarChangeListener(new RatingChangedListener());

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new SubmitClickListener());
    }

    private class NameTextListener implements TextWatcher{
        private View editText;

        public NameTextListener(View v){
            editText = v;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
            if(editText == gameNameEditText){
                gameRating.setGameName(charSequence.toString());
                Log.d(TAG, "Updated game name to " + gameRating.getGameName());
            }else if (editText == producerNameEditText){
                gameRating.setProducerName(charSequence.toString());
                Log.d(TAG, "updated producer name to "+ gameRating.getProducerName());
            }
        }

        @Override
        public void afterTextChanged(Editable editable){

        }
    }

/*
    public void buttonClick(View v) {
        String gameName = gameRating.getGameName();
        int rating = gameRating.getRating();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("gameName", gameName);
        returnIntent.putExtra("rating", rating);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
 */
    private class GameTypeSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String gameGenre = (String)parent.getItemAtPosition(position);
            if(position != 0) {
                gameRating.setGameGenre(gameGenre);
                Log.d(TAG,"game genre: " + gameRating.getGameGenre());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class MultiplayerCheckedListener implements CheckBox.OnClickListener{
        @Override
        public void onClick(View v) {
            if (checkboxMultiplayer.isChecked()){
                gameRating.setGameMultiplayer(true);
                Log.d(TAG,"truemultiplayer " + v);
            } else
                gameRating.setGameMultiplayer(false);
            Log.d(TAG,"falsemultiplayer " + v);
        }
    }


    private class RatingChangedListener implements RatingBar.OnRatingBarChangeListener{
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
           gameRating.setRating((int)rating);
           Log.d(TAG,"rating " + rating);
        }
    }

    private class SubmitClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){


            databaseReference.push().setValue(gameRating);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("gameName", gameRating.getGameName());
            returnIntent.putExtra("rating", gameRating.getRating());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}