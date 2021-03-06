package com.willparken.control;

import static android.graphics.Color.parseColor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.willparken.R;
import com.willparken.model.EmailUtility;
import com.willparken.model.SerializationFactory;
import com.willparken.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText txt_email,txt_password,txt_confirmpassword,
            txt_firstname,txt_lastname,txt_address,txt_tel;
    Button btn_login,btn_register;

    Intent intentLogin;
    Intent intentDashboard;
    Intent intentTempsmptActivity;

    User iUser;
    EmailUtility emailUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_email = (EditText) findViewById(R.id.register_txt_email);
        txt_password = (EditText) findViewById(R.id.register_txt_password);
        txt_confirmpassword = (EditText) findViewById(R.id.register_txt_confirmpassword);
        txt_firstname = (EditText) findViewById(R.id.register_txt_firstname);
        txt_lastname = (EditText) findViewById(R.id.register_txt_lastname);
        txt_address = (EditText) findViewById(R.id.register_txt_address);
        txt_tel = (EditText) findViewById(R.id.register_txt_tel);

        btn_login = (Button) findViewById(R.id.register_btn_login);
        btn_register = (Button) findViewById(R.id.register_btn_register);
        intentTempsmptActivity = new Intent(this, TempsmptActivity.class);

        intentLogin = new Intent(this, LoginActivity.class);
        intentDashboard = new Intent(this, DashboardActivity.class);

        btn_login.setOnClickListener(v -> {
            finish();
            startActivity(intentLogin);
        });
        btn_register.setOnClickListener(v ->{
            clickOnRegister();
        });

        emailUtility = new EmailUtility();

        getSupportActionBar().hide();
    }

    void clickOnRegister(){
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Pattern phonePattern = Pattern.compile("\\d{7,}");
        Matcher emailMatcher = emailPattern.matcher(txt_email.getText().toString());
        Matcher phoneMatcher = phonePattern.matcher(txt_tel.getText().toString());
        txt_email.getBackground().setColorFilter(parseColor("grey"), PorterDuff.Mode.SRC_ATOP);
        txt_confirmpassword.getBackground().setColorFilter(parseColor("grey"), PorterDuff.Mode.SRC_ATOP);
        txt_tel.getBackground().setColorFilter(parseColor("grey"), PorterDuff.Mode.SRC_ATOP);


        if (!txt_email.getText().toString().equals("") &&
            !txt_password.getText().toString().equals("") &&
            !txt_confirmpassword.getText().toString().equals("") &&
            !txt_firstname.getText().toString().equals("") &&
            !txt_lastname.getText().toString().equals("") &&
            !txt_address.getText().toString().equals("") &&
            !txt_tel.getText().toString().equals(""))
        {
            if(emailMatcher.matches() && txt_password.getText().toString().equals(txt_confirmpassword.getText().toString()) && phoneMatcher.matches()){
                iUser = new User(txt_firstname.getText().toString(),
                        txt_lastname.getText().toString(),
                        txt_address.getText().toString(),
                        txt_tel.getText().toString(),
                        txt_email.getText().toString(),
                        User.encryptPassword(txt_password.getText().toString())
                );
                iUser.save();
                SerializationFactory.getInstance().persist(getApplicationContext());
                Toast.makeText(getApplicationContext(),"Account created successfully!",Toast.LENGTH_LONG).show();
                new Thread(){
                    @Override
                    public void run() {
                        emailUtility.sendMail(iUser.getEmail(), "WillParken > Welcome!","Welcome to WillParken!\nWe hope you like our product!");
                    }
                }.start();
                finish();
                intentDashboard.putExtra("iUser",iUser);
                startActivity(intentDashboard);
                return;
            }
            else {
                if (!emailMatcher.matches()) {
                    txt_email.getBackground().setColorFilter(parseColor("red"), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_LONG).show();
                }
                if (!txt_password.getText().toString().equals(txt_confirmpassword.getText().toString())) {
                    txt_confirmpassword.getBackground().setColorFilter(parseColor("red"), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                }
                if (!phoneMatcher.matches()) {
                    txt_tel.getBackground().setColorFilter(parseColor("red"), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(getApplicationContext(), "Phone number is not valid!", Toast.LENGTH_LONG).show();
                }
            }
            if (SerializationFactory.getInstance().exists(iUser)){
                    Toast.makeText(getApplicationContext(),"Email already in use!",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please fill in all Fields.",Toast.LENGTH_LONG).show();
        }
    }
}
