package ie.deliverd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseAnalytics;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    public void signUp(View view){
        intent = new Intent(this, Signup_driver.class);
        startActivity(intent);
    }

    public void login(View view){
        intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}