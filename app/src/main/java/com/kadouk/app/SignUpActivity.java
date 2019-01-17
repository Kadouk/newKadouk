package com.kadouk.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.kadouk.app.model.RegisterResponse;
import com.kadouk.app.webService.APIClient;
import com.kadouk.app.webService.APIInterface;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {

    Short EditTextMaxLength = 11;
    FloatingActionButton fab;
    EditText EditTextNumber;
    Intent intent;
    Typeface typeface;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_enter_number);

        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/B Koodak Bold_0.ttf");

        fab = findViewById(R.id.signup_number_fab);
        fab.setEnabled(false);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grandis)));
        EditTextNumber = findViewById(R.id.signup_number_edt);
        EditTextNumber.setTypeface(typeface);
        EditTextNumber.addTextChangedListener(mTextEditorWatcher);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @SuppressLint("WrongViewCast")
    public void sendNumber(View view) {

        if (EditTextNumber.getText().length() < EditTextMaxLength) {
            EditTextNumber.setError("There must be 11 numbers");
        } else {
            Log.i("number", String.valueOf(EditTextNumber.getText()));
            String number = String.valueOf(EditTextNumber.getText());
            Globals.setNumber(number);
            sendPhoneNumber(number);
        }
    }

    private void sendPhoneNumber(String Number){

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RegisterResponse> call =  apiInterface.sendPhoneNumber(Number);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response){
                if(response.isSuccessful()) {

                    if (String.valueOf(response.body().getStatus()).equals("200")){
                        Log.i("LOGIN", String.valueOf(response.body().getStatus()));
                        intent = new Intent(SignUpActivity.this, SignUpEnterCodeActivity.class).
                                addFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                       // finish();
                        startActivity(intent);
                    }
                }else {
                    Log.i("LOGIN", "not response");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.i("LOGIN", "connection problem!");
            }
        });
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() == EditTextMaxLength){
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.olivine)));
                fab.setEnabled(true);
            }else {
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grandis)));
                fab.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
}