package ie.deliverd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DriverInfo extends AppCompatActivity {
    private Button ReturnBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);





        ReturnBtn = findViewById(R.id.ReturnBtn);


        ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DriverInfo.this, DriverProfile.class));
            }
        });
    }
}
