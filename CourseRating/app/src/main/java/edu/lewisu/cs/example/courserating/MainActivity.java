package edu.lewisu.cs.example.courserating;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int RC_RATING_ACTIVITY = 100;
    private int currentTheme;
    private CourseRatingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Settings.setPreferences(this);
        setTheme(Settings.DEFAULT_THEME);
        currentTheme =Settings.DEFAULT_THEME;
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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
        if(Settings.DEFAULT_THEME != currentTheme){
            currentTheme = Settings.DEFAULT_THEME;
            setTheme(currentTheme);
            recreate();
        }

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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RC_RATING_ACTIVITY) {
            int rating = data.getIntExtra("rating", 0);
            String course = data.getStringExtra("courseName");

            //the first rating selects the string, the second is the value inserted into the string
            String ratingString = getResources().getQuantityString(R.plurals.star_rating, rating, rating);
            String toastString = "Rating entered\n";
            toastString += "Course name: " + course + "\n";
            toastString += ratingString;

            Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
        }
    }


}
