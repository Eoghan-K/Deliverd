package ie.deliverd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Signup_driver extends AppCompatActivity {

    public void signUp(View view){
        EditText email = findViewById(R.id.Email_Txt);
        EditText password = findViewById(R.id.Password_Txt);

        if (email.getText().toString().equals("") || password.getText().toString().equals("")){
            Toast.makeText(this, "A email and password are require", Toast.LENGTH_SHORT).show();
        } else{
            ParseUser user = new ParseUser();
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(Signup_driver.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Signup_driver.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_driver);
    }
}
