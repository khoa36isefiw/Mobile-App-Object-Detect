package hcmute.edu.vn.chaydiemoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import hcmute.edu.vn.chaydiemoi.Connect.Intro;

public class SplashActivity extends AppCompatActivity {

    /*
     * Splash Screen là một màn hình đặc biệt,
     * màn hình này được hiển thị đầu tiên nhất khi bạn mở một ứng dụng.
     *
     * */
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, IntroAppActivity.class);
                startActivity(intent);
            }
        }, 8000);
    }
}