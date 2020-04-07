package edu.lewisu.cs.exampletodofirebase;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    private ToDo toDo;
    private EditText titleField;
    private Spinner prioritySpinner;
    private CheckBox completeCheckBox;
    private Button addEditButton;
    private String userId;
    private String ref;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleField = findViewById(R.id.title_field);
        titleField.addTextChangedListener(new TitleListener());

        prioritySpinner = findViewById(R.id.spinner);
        prioritySpinner.setOnItemSelectedListener(new PrioritySelect());

        completeCheckBox = findViewById(R.id.complete_checkbox);
        completeCheckBox.setOnClickListener(new CompleteChangeListener());

        addEditButton = findViewById(R.id.add_edit_button);

        userId = getIntent().getStringExtra("uid");
        toDo = new ToDo(userId);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = getIntent().getStringExtra("ref");


        if (ref != null) {
            databaseReference = firebaseDatabase.getReference().child("to_do").child(ref);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    toDo = dataSnapshot.getValue(ToDo.class);
                    setUi();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            databaseReference.addValueEventListener(eventListener);

        } else {
            addEditButton.setOnClickListener(new OnAddButtonClick());
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("to_do");
        }


    }

    private void setUi(){
        if(toDo != null) {
            //set components to display detail information
            titleField.setText(toDo.getTitle());
            prioritySpinner.setSelection(toDo.getPriority());
            completeCheckBox.setChecked(toDo.isComplete());
            addEditButton.setText(R.string.update);
            addEditButton.setOnClickListener(new OnUpdateButtonClick());
        }
    }


    private class TitleListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            toDo.setTitle(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class CompleteChangeListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(completeCheckBox.isChecked()){
                toDo.setComplete(true);
            }else{
                toDo.setComplete(false);
            }
        }
    }

    private class PrioritySelect implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(toDo != null)
                toDo.setPriority(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class OnAddButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            databaseReference.push().setValue(toDo);
            finish();
        }
    }

    private class OnUpdateButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            databaseReference.setValue(toDo);
            finish();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        switch(id){
            case R.id.delete:
                databaseReference.removeValue();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
