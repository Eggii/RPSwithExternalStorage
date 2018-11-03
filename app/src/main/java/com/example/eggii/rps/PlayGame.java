package com.example.eggii.rps;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.eggii.rps.ExternalStorageInterfaces.WriteExternalData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class for game play
 */
public class PlayGame extends AppCompatActivity implements View.OnClickListener, WriteExternalData {

    SharedPreferences pref;
    ImageView iv_userCard, iv_cpuCard;
    ImageButton ib_rock, ib_paper, ib_scissors;
    Button btn_pause;
    int[] images = new int[]{
            R.drawable.rock,
            R.drawable.paper,
            R.drawable.scissors
    };
    int userInput = 0;

    private static int maxPoints = 0;
    private int userPoints = 0;
    private int cpuPoints = 0;
    private TextView txt_userPoints;
    private TextView txt_cpuPoints;

    private String username = UserInfo.getUsername();

    private final String SCORE = "score";
    private final String SCORE_FILE = "score.txt";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        iv_userCard = findViewById(R.id.iv_user_card);
        iv_cpuCard = findViewById(R.id.iv_cpu_card);
        ib_rock = findViewById(R.id.btn_rock);
        ib_paper = findViewById(R.id.btn_paper);
        ib_scissors = findViewById(R.id.btn_scissors);
        btn_pause = findViewById(R.id.btn_pause);

        //Set event handlers for rock, paper scissors inputs
        ib_rock.setOnClickListener(this);
        ib_paper.setOnClickListener(this);
        ib_scissors.setOnClickListener(this);

        //Set event handler for pause button
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToScore();
            }
        });
    }

    /**
     * Onclick handlers to get user card choice and display respective card
     * */
    @Override
    public void onClick(View v) {
        int id = v.getId(); //Clicked button id
        switch (id) {
            case R.id.btn_rock:
                userInput = 1;
                iv_userCard.setBackgroundResource(R.drawable.rock);
                setUserCard();
                break;
            case R.id.btn_paper:
                userInput = 2;
                iv_userCard.setBackgroundResource(R.drawable.paper);
                setUserCard();
                break;
            case R.id.btn_scissors:
                userInput = 3;
                iv_userCard.setBackgroundResource(R.drawable.scissors);
                setUserCard();
                break;
        }
    }

    /**
     * Calculate random card for cpu
     * */
    private void setUserCard() {
        int imageId = (int) (Math.random() * images.length);
        iv_cpuCard.setBackgroundResource(images[imageId]);
        checkResult(imageId);
    }

    /**
     * Method for checking if user or cpu wins point and show respective result
     * */
    private void checkResult(int imageId) {
        if (userInput == 1 && imageId == 0) {     //User choose Rock,Computer choose Rock
            showResult(2);
        } else if (userInput == 1 && imageId == 1) { //User choose Rock,Computer choose Paper
            showResult(0);
        } else if (userInput == 1 && imageId == 2) { //User choose Rock,Computer choose Scissors
            showResult(1);
        } else if (userInput == 2 && imageId == 0) { //User choose Paper,Computer choose Rock
            showResult(1);
        } else if (userInput == 2 && imageId == 1) { //User choose Paper,Computer choose Paper
            showResult(2);
        } else if (userInput == 2 && imageId == 2) { //User choose Paper,Computer choose Scissors
            showResult(0);
        } else if (userInput == 3 && imageId == 0) {//User choose Scissors,Computer choose Rock
            showResult(0);
        } else if (userInput == 3 && imageId == 1) { //User choose Scissors,Computer choose Paper
            showResult(1);
        } else if (userInput == 3 && imageId == 2) { //User choose Scissors,Computer choose Scissors
            showResult(2);
        }
    }

    /**
     * Method for showing points to user and if game over redirects to Score activity
     * */
    private void showResult(int result) {
        txt_userPoints = findViewById(R.id.txt_userPoints);
        txt_cpuPoints = findViewById(R.id.txt_cpuPoints);


        if (cpuPoints < 3) { //user have 3 lives
            if (result == 0) { //if user lost
                cpuPoints += 1;
                txt_cpuPoints.setText(String.valueOf(cpuPoints));
            } else if (result == 1) { //if user won
                userPoints += 1;
                txt_userPoints.setText(String.valueOf(userPoints));
            } else {//if tie
            }
        } else { //else game over
            maxPoints = userPoints; //Set maximum points that player took from one game

            userPoints = 0;
            txt_userPoints.setText(String.valueOf(userPoints));

            cpuPoints = 0;
            txt_cpuPoints.setText(String.valueOf(cpuPoints));

            if (isHighSore(maxPoints)) { //if new high score, save new result

                //Save via Shared preferences
                setSharedPrefsScore(username, String.valueOf(maxPoints));

                // Uncomment to switch saving type to external storage
                //String externalSaveText = username + ": " + String.valueOf(maxPoints) + "\n";
                //writeData(SCORE_FILE, externalSaveText);
            }

            redirectToScore();
        }
    }

    /**
     *  Method for setting shared preferences with key and value to Score pref
     * */
    private void setSharedPrefsScore(String key, String value) {
        pref = getApplicationContext().getSharedPreferences(SCORE, MODE_PRIVATE);

        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Method for checking if new high score in score shared preferences
     * @param points input param for score to check
     * return true or false
     */
    private boolean isHighSore(int points) {
        pref = getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE);
        int currentHighScore = Integer.parseInt(pref.getString(username, "0"));

        if (currentHighScore < points) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method for redirecting Score activity
     */
    private void redirectToScore() {
        intent = new Intent(this, Score.class);
        startActivity(intent);
    }


    //Implementation of ExternalStorageInterfaces.WriteExternalData
    /**
     * Method for checking if external storage is writable
     * Returns boolean
     * */
    @Override
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean chkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Method for writing data to external storage
     * */
    @Override
    public void writeData(String fileName, String text) {
        if(isExternalStorageWritable() && chkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            File Score = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

            try {
                FileOutputStream fos = new FileOutputStream(Score, true);
                fos.write(text.getBytes());
                fos.close();

            } catch (IOException e) { }
        }else {
            Toast.makeText(this, "Sorry can't save your score. Cannot write to external storage.", Toast.LENGTH_SHORT).show();
        }
    }
}