package com.smarttech.doingtogether.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.smarttech.doingtogether.R;
import com.smarttech.doingtogether.data.network.ApiHelper;
import com.smarttech.doingtogether.data.network.AppApiHelper;
import com.smarttech.doingtogether.data.network.model.DomainResponse;
import com.smarttech.doingtogether.data.network.model.LoginRequest;
import com.smarttech.doingtogether.data.network.model.LoginResponse;
import com.smarttech.doingtogether.ui.event.EventActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private ApiHelper mApi;

    private ImageView mCompanyLogo;
    private TextView mEmailView;
    private TextView mPasswordView;

    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Вход");

        // Api
        mApi = AppApiHelper.getRetrofitAdapter();

        // Icon
        mCompanyLogo = findViewById(R.id.main_company_logo);

        // Login
        mEmailView = findViewById(R.id.main_email_text);
        mPasswordView = findViewById(R.id.main_password_text);

        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    String[] domain = s.toString().split("@");
                    if (domain.length > 1) {
                        mApi.getIconByDomain(domain[1])
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<DomainResponse>() {
                                    @Override
                                    public void accept(DomainResponse domainResponse) throws Exception {
                                        Picasso.with(getApplicationContext())
                                                .load(domainResponse.getIconUrl())
                                                .placeholder(R.drawable.company_logo_placeholder)
                                                .error(R.drawable.company_logo_placeholder)
                                                .into(mCompanyLogo);
                                    }
                                });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Login button
        mLoginButton = findViewById(R.id.main_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    mApi.login(new LoginRequest(email, password))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<LoginResponse>() {
                                @Override
                                public void accept(LoginResponse loginResponse) throws Exception {
                                    if (loginResponse.getKey() != null) {
                                        getSharedPreferences("settings", Context.MODE_PRIVATE)
                                                .edit()
                                                .putString("key", loginResponse.getKey())
                                                .apply();
                                        startActivity(new Intent(LoginActivity.this,
                                                EventActivity.class));
                                    }
                                }
                            });
                }
            }
        });
    }
}
