package my.assignment.accelerometerex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    boolean check;
    String phone;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        /** Getting the shared preference object that points to preferences resource available in this context */
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        check=sp.getBoolean("check_box_preference_1",false);
        phone=sp.getString("edit_text_preference_1","No Number");

        if(Boolean.toString(check).equals("true")) {
            Intent serviceIntent = new Intent(getApplicationContext(), AccelerometerReadingService.class);
            serviceIntent.putExtra("Phone", phone);
            startService(serviceIntent);
        }

        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPostResume() {
        Log.i("MainActivity","onResume");
        super.onPostResume();
            sp.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        Log.i("key",key);

        if(key.equals("check_box_preference_1")) {
            check = sp.getBoolean("check_box_preference_1", false);
            Toast.makeText(MainActivity.this, Boolean.toString(check), Toast.LENGTH_SHORT).show();

            if(Boolean.toString(check).equals("true")){
                Intent serviceIntent=new Intent(getApplicationContext(),AccelerometerReadingService.class);
                serviceIntent.putExtra("Phone",phone);
                startService(serviceIntent);
                Log.i("check",key);
            }
            if(Boolean.toString(check).equals("false")){
                Intent serviceIntent=new Intent(getApplicationContext(),AccelerometerReadingService.class);
                stopService(serviceIntent);

                Log.i("uncheck",key);
            }
        }

        if(key.equals("edit_text_preference_1")) {
            phone = sp.getString("edit_text_preference_1", "No Number");
            Toast.makeText(MainActivity.this, phone, Toast.LENGTH_SHORT).show();
        }

    }


    public static class MyPreferenceFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }
    }
}
