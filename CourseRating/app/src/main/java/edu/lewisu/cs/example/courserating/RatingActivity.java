package edu.lewisu.cs.example.courserating;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingActivity extends AppCompatActivity {

    private final String TAG = RatingActivity.class.getSimpleName();
    private CourseRating courseRating;
    private EditText courseNameEditText;
    private EditText instructorNameEditText;
    private Spinner courseTypeSpinner;
    private RatingBar ratingBar;
    private Button submitButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Settings.DEFAULT_THEME);
        setContentView(R.layout.activity_rating);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("course_rating");

        courseRating = new CourseRating();

        courseNameEditText = findViewById(R.id.courseNameEditText);
        instructorNameEditText = findViewById(R.id.instructorNameEditText);
        courseNameEditText.addTextChangedListener(new NameTextListener(courseNameEditText));
        instructorNameEditText.addTextChangedListener(new NameTextListener(instructorNameEditText));

        courseTypeSpinner = findViewById(R.id.courseTypeSpinner);
        courseTypeSpinner.setOnItemSelectedListener(new CourseTypeSelectedListener());

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingChangedListener());
        ratingBar.setRating(Settings.DEFAULT_RATING);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new SubmitClickListener());



    }

    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener

    private class NameTextListener implements TextWatcher {
        private View editText;

        public NameTextListener(View v){
            editText = v;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(editText == courseNameEditText) {
                courseRating.setCourseName(charSequence.toString());
                Log.d(TAG, "updated course name to " + courseRating.getCourseName());
            }else if(editText == instructorNameEditText) {
                courseRating.setInstructorName(charSequence.toString());
                Log.d(TAG, "updated instructor name to " + courseRating.getInstructorName());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class CourseTypeSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String courseType = (String)adapterView.getItemAtPosition(i);
            if (i!=0) {
                courseRating.setCourseType(courseType);
                Log.d(TAG, "course type: " + courseType);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class RatingChangedListener implements RatingBar.OnRatingBarChangeListener{
        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            courseRating.setRating((int)v);
            Log.d(TAG, "rating " + v);
        }
    }

    private class SubmitClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            databaseReference.push().setValue(courseRating);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("courseName", courseRating.getCourseName());
            returnIntent.putExtra("rating", courseRating.getRating());
            setResult(RESULT_OK, returnIntent);
            finish();

        }
    }
}
