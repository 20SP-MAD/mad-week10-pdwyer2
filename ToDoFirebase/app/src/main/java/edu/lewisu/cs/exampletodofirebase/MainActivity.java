package edu.lewisu.cs.exampletodofirebase;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ToDoListAdapter.TodoListAdapterOnClickHandler {

    private final static int RC_SIGN_IN = 1;
    private ToDoListAdapter adapter;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userId;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    userId = user.getUid();
                }else {
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build())).build(), RC_SIGN_IN);
                }
            }
        };


        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(user != null){
            userId = user.getUid();
        }
        setAdapter();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("uid", userId);
                startActivity(intent);
            }
        });

    }

    private void setAdapter(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Query query = firebaseDatabase.getReference().child("to_do").orderByChild("uid").equalTo(userId);

        FirebaseRecyclerOptions<ToDo> options =
                new FirebaseRecyclerOptions.Builder<ToDo>().setQuery(query, ToDo.class).build();

        adapter = new ToDoListAdapter(options,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra("uid",userId);
        DatabaseReference ref = adapter.getRef(position);
        String id = ref.getKey();
        detailIntent.putExtra("ref",id);
        startActivity(detailIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch(id) {
            case R.id.sign_out:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    userId= user.getUid();
                }
                setAdapter();
            }
        } if(resultCode == RESULT_CANCELED){
            finish();
        }
    }
}
