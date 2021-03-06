package com.willparken.control;

import static android.graphics.Color.parseColor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.willparken.R;
import com.willparken.model.SerializationFactory;
import com.willparken.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText txt_email,txt_password;
    Button btn_login,btn_register;
    ImageView img_loginicon;

    Intent intentRegister;
    Intent intentDashboard;
//    Intent intentTempsmptActivity;

    User iUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        SerializationFactory.getInstance().restore(getApplicationContext());

        txt_email = (EditText) findViewById(R.id.login_txt_email);
        txt_password = (EditText) findViewById(R.id.login_txt_password);
        btn_login = (Button) findViewById(R.id.login_btn_login);
        btn_register = (Button) findViewById(R.id.login_btn_register);
        img_loginicon = (ImageView) findViewById(R.id.login_img_loginicon);

        intentRegister = new Intent(this, RegisterActivity.class);
        intentDashboard = new Intent(this, DashboardActivity.class);
//        intentTempsmptActivity = new Intent(this, TempsmptActivity.class);

        btn_login.setOnClickListener(v -> {
            clickOnLogin();
        });
        btn_register.setOnClickListener(v -> {
            finish();
            startActivity(intentRegister);
        });


    }


    void clickOnLogin(){
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher emailMatcher = emailPattern.matcher(txt_email.getText().toString());
        if (!txt_email.getText().toString().equals("") && !txt_password.getText().toString().equals("")) {
            if (emailMatcher.matches()) {
                iUser = User.selectByEmailPassword(
                        txt_email.getText().toString(), txt_password.getText().toString());
            }else{
                txt_email.getBackground().setColorFilter(parseColor("red"), PorterDuff.Mode.SRC_ATOP);
                Toast.makeText(getApplicationContext(),"Wrong email or password!",Toast.LENGTH_LONG).show();
            }
        }
        if (iUser != null)
        {
            intentDashboard.putExtra("iUser",iUser);
            finish();
//            startActivity(intentTempsmptActivity);
            startActivity(intentDashboard);
        }else{
            txt_email.getBackground().setColorFilter(parseColor("red"), PorterDuff.Mode.SRC_ATOP);
            txt_password.getBackground().setColorFilter(parseColor("red"), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
