package com.example.eggii.rps;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Class for getting user info
 * */
public class UserInfo extends AppCompatActivity implements View.OnClickListener {

    private Button btnNext;
    private static EditText edit_username;
    private static String username = "";

    /**
     * Method for getting username for app
     * */
    public static String getUsername() {
        return username;
    }

    /**
     * Method for setting username
     * */
    private void setUsername() {
        edit_username = findViewById(R.id.edit_username);
        username = edit_username.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(UserInfo.this);
    }

    /**
     * Method for saving username and redirecting to game
     * */
    @Override
    public void onClick(View view) {
        setUsername();

        Intent intent = new Intent(this,PlayGame.class);
        startActivity(intent);
    }
}
