package com.ispc.gymapp.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ispc.gymapp.R;
import com.ispc.gymapp.helper.Callback;
import com.ispc.gymapp.model.User;
import com.ispc.gymapp.presenters.login.LoginPresenter;
import com.ispc.gymapp.views.adapter.OnboardingStateAdapter;
import com.ispc.gymapp.views.viewmodel.RegisterViewModel;

import java.util.HashMap;
import java.util.Map;

public class OnboardingActivityView extends AppCompatActivity implements View.OnClickListener {

    private OnboardingStateAdapter onboardingStateAdapter;
    private LoginPresenter loginPresenter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public User loggedUser;
    private int currentFragmentIndex =0;
    private ViewPager2 viewPager;
    private RegisterViewModel viewModel;
    public HashMap<String,Object> inputData;
    private Button btnNext;
    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.getCurrentUser(new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                loggedUser = user;
                if (loggedUser != null) {

                }
            }

            @Override
            public void onFailure() {
                startActivity(new Intent(OnboardingActivityView.this, LoginActivity.class));
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_view);
        onboardingStateAdapter = new OnboardingStateAdapter(this.getSupportFragmentManager(),getLifecycle());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(onboardingStateAdapter);
        viewPager.setUserInputEnabled(false);
        loginPresenter = new LoginPresenter(this,mAuth,db);
        btnNext = findViewById(R.id.btnObNext);
        Button btnBack = findViewById(R.id.btnObBack);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentFragmentIndex = position;

            }
        });
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        viewModel.getInputData().observe(this, data -> {
            System.out.println("Data: " + data);

            this.inputData = data;

        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnObNext){

            System.out.println(currentFragmentIndex);
            if (currentFragmentIndex < onboardingStateAdapter.getItemCount() - 1) {
                currentFragmentIndex++;
                viewPager.setCurrentItem(currentFragmentIndex);
            }
            if (currentFragmentIndex == onboardingStateAdapter.getItemCount() - 1) {

                btnNext.setText("Finalizar");
                final User finalLoggedUser = loggedUser;
                final HashMap<String,Object> finalInputData = inputData;
                btnNext.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        loginPresenter.updateUser(finalLoggedUser, finalInputData);
                        startActivity(new Intent(OnboardingActivityView.this,MainActivity.class));
                    }
                });
            }
        }

        if(view.getId() == R.id.btnObBack){
            if (currentFragmentIndex >= 1) {
                currentFragmentIndex--;
                viewPager.setCurrentItem(currentFragmentIndex);
            }
        }

    }
}