package hcmute.edu.vn.chaydiemoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import hcmute.edu.vn.chaydiemoi.About.AboutAppActivity;
import hcmute.edu.vn.chaydiemoi.About.AboutMemberActivity;
import hcmute.edu.vn.chaydiemoi.Home.Tab;
import hcmute.edu.vn.chaydiemoi.Scan.ScanActivity;
import hcmute.edu.vn.chaydiemoi.Translate.TranslateActivity;

public class MainActivity extends AppCompatActivity {

    CardView cr_AboutUs, cr_UseApp, cr_Scan, cr_Translates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ẩn đi thanh App Name
        getSupportActionBar().hide();

        cr_AboutUs = findViewById(R.id.crAboutUs);

        cr_UseApp = findViewById(R.id.crHowToUseApp);

        cr_Scan = findViewById(R.id.crScan);

        cr_Translates = findViewById(R.id.crTranslate);

        cr_AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutMemberActivity.class);
                startActivity(i);
                finish();

            }
        });



        cr_UseApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutAppActivity.class);
                startActivity(i);
                finish();

            }
        });

        cr_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(MainActivity.this, ScanActivity.class);
                Intent i = new Intent(MainActivity.this, Tab.class);
                startActivity(i);
                finish();

            }
        });


        cr_Translates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TranslateActivity.class);
                startActivity(i);
                finish();

            }
        });

    }
}