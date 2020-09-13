package ikbal.mulalic.reflektor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class IntroActivity extends AppCompatActivity {

    SharedPreferences pref = null;
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pref.getBoolean("firstrun", true))
                {
                    //prvi put
                    Intent intent = new Intent (IntroActivity.this,InstructionsActivity.class);
                    startActivity(intent);

                    pref.edit().putBoolean("firstrun",false).apply();
                }
                else
                {
                    Intent intent = new Intent (IntroActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },SPLASH_TIME_OUT);


    }
}