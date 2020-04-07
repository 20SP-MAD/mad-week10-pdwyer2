package edu.lewisu.cs.example.courserating;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    private static final int RC_RATING_ACTIVITY = 100;
    private int currentTheme;
    private CourseRatingAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int SIGN_IN = 1;
    private FirebaseDatabase firebaseDatabase;
    private Query query;
    private FirebaseRecyclerAdapter<CourseRating, CourseRatingAdapter.RatingHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Settings.setPreferences(this);
        setTheme(Settings.DEFAULT_THEME);
        currentTheme =Settings.DEFAULT_THEME;
        setContentView(R.layout.activity_main);
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
                            .build(),SIGN_IN);
                }
            }
        };


        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        query = firebaseDatabase.getReference().child("course_rating");
        FirebaseRecyclerOptions<CourseRating> options =
                new FirebaseRecyclerOptions.Builder<CourseRating>().setQuery(query, CourseRating.class).build();
        firebaseRecyclerAdapter = new CourseRatingAdapter(options);
        recyclerView.setAdapter(firebaseRecyclerAdapter);


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RatingActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        if(Settings.DEFAULT_THEME != currentTheme){
            currentTheme = Settings.DEFAULT_THEME;
            setTheme(currentTheme);
            recreate();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.sign_out:
                AuthUI.getInstance().signOut(this);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RC_RATING_ACTIVITY) {
            int rating = data.getIntExtra("rating", 0);
            String course = data.getStringExtra("course_Name");

            //the first rating selects the string, the second is the value inserted into the string
            String ratingString = getResources().getQuantityString(R.plurals.star_rating, rating, rating);
            String toastString = "Rating entered\n";
            toastString += "Course name: " + course + "\n";
            toastString += ratingString;

            Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
        }

        if(requestCode == RESULT_CANCELED && requestCode == SIGN_IN) {
            finish();
        }
    }


}
