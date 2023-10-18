package com.ispc.gymapp.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ispc.gymapp.R;
import com.ispc.gymapp.model.Exercise;
import com.ispc.gymapp.model.Routine;
import com.ispc.gymapp.model.User;
import com.ispc.gymapp.views.adapter.RoutineAdapter;

import java.util.ArrayList;

public class RoutineActivity extends AppCompatActivity {

    RoutineAdapter routineAdapter;

    ArrayList<Exercise> exercises;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        setUpRecyclerView();
    }

    private void getRoutines() {
        db.collection("routines").whereEqualTo("user", user.getMail())
                .get().addOnCompleteListener(task -> {

                    for (DocumentChange documentChange : task.getResult().getDocumentChanges()) {
                        Routine currentRoutine = new Routine();
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            currentRoutine = documentChange.getDocument().toObject(Routine.class);
                            exercises.addAll(currentRoutine.getExercises());
                            routineAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void setUpRecyclerView() {
        exercises = new ArrayList<>();
        // Adapter
        routineAdapter = new RoutineAdapter(this, exercises);
        getUser();
        // Recycler
        RecyclerView recyclerView = findViewById(R.id.recyclerRoutine);
        recyclerView.setAdapter(routineAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void returnToDescription(View view) {
        this.finish();
    }

    public void returnToExerciseList(View view) {
        Intent intent = new Intent(this, ExerciseList.class);
        startActivity(intent);
    }

    private void getUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            DocumentReference usernameRef = db.collection("users").document(firebaseUser.getUid());
            usernameRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {

                    user = documentSnapshot.toObject(User.class);
                    getRoutines();
                }
            });

        }
    }
}