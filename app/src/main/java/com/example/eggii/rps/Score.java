package com.example.eggii.rps;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.eggii.rps.ExternalStorageInterfaces.SaveExternalData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class for game high score
 */
public class Score extends AppCompatActivity implements SaveExternalData {

    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private ListView lv_score;
    private final String SCORE_FILE = "score.txt";
    private final String SCORE = "score";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        lv_score = findViewById(R.id.lv_score);

        //Load score from shared preferences
        loadScore(lv_score);

        //Uncomment to read score from external storage
        //readData(SCORE_FILE, lv_score);
    }

    /**
     * Method for loading score from shared preferences
     */
    private void loadScore(ListView listView) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SCORE, MODE_PRIVATE);

        list = new ArrayList<>();

        Map<String, ?> allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            list.add(entry.getKey() + " " + entry.getValue().toString());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    //Implementation of ExternalStorageInterfaces.ReadExternalData
    /**
     * Method for checking if external storage is readable
     * Returns boolean
     */
    @Override
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Method for reading data from external storage
     */
    @Override
    public void readData(String fileName, Object output) {

        list = new ArrayList<>();

        if (isExternalStorageReadable()) {

            try {

                File fileScore = new File(Environment.getExternalStorageDirectory(), SCORE_FILE);
                FileInputStream fis = new FileInputStream(fileScore);

                if (fis != null) {
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        list.add(line);
                    }
                    fis.close();
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
                ((ListView) output).setAdapter(adapter);
            } catch (IOException e) {
                Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Cannot read score from External Storage", Toast.LENGTH_SHORT).show();
        }
    }
}
