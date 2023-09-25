package com.ispc.gymapp.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ispc.gymapp.R;
import com.ispc.gymapp.views.adapter.ExercisesAdapter;
import com.ispc.gymapp.views.fragments.ExercisesAdvancedFragment;
import com.ispc.gymapp.views.fragments.ExercisesBeginnerFragment;
import com.ispc.gymapp.views.fragments.ExercisesIntermediateFragment;

public class ExerciseList extends AppCompatActivity {

    private ExercisesAdapter exercisesAdapter;
    private ViewPager2 viewPager2;

    public final static String EXTRA_EXERCISE_TYPE = "com.ispc.gymapp.extra.Exercise_Type";

    private String exerciseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        viewPager2 = findViewById(R.id.viewPagerExercises);
        exercisesAdapter = new ExercisesAdapter(getSupportFragmentManager(),getLifecycle());
        // Add fragments
        exercisesAdapter.add(new ExercisesBeginnerFragment());
        exercisesAdapter.add(new ExercisesIntermediateFragment());
        exercisesAdapter.add(new ExercisesAdvancedFragment());

        viewPager2.setAdapter(exercisesAdapter);

        TabLayout exercisesTabLayout = findViewById(R.id.exercises_difficult_tabs);

        // Sync clic with tab
        new TabLayoutMediator(exercisesTabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0 -> tab.setText("Principiante");
                    case 1 -> tab.setText("Intermedio");
                    case 2 -> tab.setText("Avanzado");
                }
            }
        }).attach();
    }

    public void returnToHome(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

    public void getDescription(View view) {
        Log.i("View", String.valueOf(view.getId()==R.id.absBeginnerImage));

        Intent description = new Intent(this, ExercisesDescription.class);

        if (view.getId()==R.id.absBeginnerImage || view.getId()==R.id.absBeginnerText) {
            exerciseType = "abs";
            description.putExtra(EXTRA_EXERCISE_TYPE, exerciseType);
        } else if (view.getId()==R.id.chestBeginnerImage || view.getId()==R.id.chestBeginnerText) {
            exerciseType = "chest";
            description.putExtra(EXTRA_EXERCISE_TYPE, exerciseType);
        } else if (view.getId()==R.id.armBeginnerImage || view.getId()==R.id.armBeginnerText) {
            exerciseType = "arm";
            description.putExtra(EXTRA_EXERCISE_TYPE, exerciseType);
        } else if (view.getId()==R.id.legBeginnerImage || view.getId()==R.id.legBeginnerText) {
            exerciseType = "leg";
            description.putExtra(EXTRA_EXERCISE_TYPE, exerciseType);
        } else {
            exerciseType = "shoulderAndBack";
            description.putExtra(EXTRA_EXERCISE_TYPE, exerciseType);
        }

        startActivity(description);
    }
}