package hcmute.edu.vn.chaydiemoi.About;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import hcmute.edu.vn.chaydiemoi.Adapter.AboutUs_ViewPageAdapter;
import hcmute.edu.vn.chaydiemoi.MainActivity;
import hcmute.edu.vn.chaydiemoi.R;

public class AboutMemberActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout mDotLayout;
    TextView[] dots;
    AboutUs_ViewPageAdapter aboutUs_viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_member);
        getSupportActionBar().hide();

        // Lấy reference tới các View trong Fragment
        viewPager = findViewById(R.id.view_pager);
        mDotLayout =findViewById(R.id.indicator_layout);



        // Thực hiện các thao tác khác với các View này
        //aboutUs_viewPageAdapter = new AboutUs_ViewPageAdapter(getActivity() );
        aboutUs_viewPageAdapter = new AboutUs_ViewPageAdapter(this );
        viewPager.setAdapter(aboutUs_viewPageAdapter);

        setUpindicator(0);
        viewPager.addOnPageChangeListener(viewListener);

    }


    public void setUpindicator(int position){

        dots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);


            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(36);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }

    private int getitem(int i){

        return viewPager.getCurrentItem() + i;
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

            }
            else {
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };




    // Quay lại màn hình chính - Khi nhấn nút Back
    // nếu không set thì sẽ tự động out app
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}