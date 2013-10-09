package com.mobileproto.lab5;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lego6245 on 10/9/13.
 */
public class SettingsActivity extends Activity {
    SharedPreferences prefs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String username = prefs.getString("username", "lego6245");

        final EditText usernameField = (EditText)findViewById(R.id.usernameField);
        usernameField.setText(username);
        Button sendName = (Button)findViewById(R.id.usernameSubmit);
        sendName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String tweetText = usernameField.getText().toString();
                prefs.edit().putString("username", tweetText).commit();
                finish();
            }
        });
    }

}
