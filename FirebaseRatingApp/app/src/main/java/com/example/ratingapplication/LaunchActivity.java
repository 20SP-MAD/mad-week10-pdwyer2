package com.example.ratingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;

public class LaunchActivity extends AppCompatActivity {
    private static final int RC_RATING_ACTIVITY = 100;
    private GameRatingListAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int SIGN_IN = 1;
    private FirebaseDatabase firebaseDatabase;
    private Query query;
    private FirebaseRecyclerAdapter<GameRating, GameRatingListAdapter.RatingHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    startActivityForResult(
                            AuthUI.getInstance().createSignInIntentBuilder()
                            .setIsSmartLockEnabled(true)
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(), SIGN_IN);
                }

            }
        };

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        query = firebaseDatabase.getReference().child("game_rating");
        FirebaseRecyclerOptions<GameRating> options =
                new FirebaseRecyclerOptions.Builder<GameRating>().setQuery(query, GameRating.class).build();
        firebaseRecyclerAdapter  = new GameRatingListAdapter(options);
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        FloatingActionButton fab = findViewById(R.id.ratingButton);
        fab.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RC_RATING_ACTIVITY) {
            int rating = data.getIntExtra("rating",0);
            String game = data.getStringExtra("gameName");

            //toast string
            String ratingString = getResources().getQuantityString(R.plurals.star_rating, rating, rating);
            String toastString = "Rating entered: \n";
            toastString += "Game Name: " + game + "\n";
            toastString += ratingString;

            Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
        }
        if(resultCode == RESULT_CANCELED && requestCode == SIGN_IN){
            finish();
        }
    }
}
