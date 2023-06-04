package hcmute.edu.vn.chaydiemoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hcmute.edu.vn.chaydiemoi.Adapter.ViewPagerAdapter;

public class IntroAppActivity extends AppCompatActivity {

    //ViewPager và LinearLayout được khởi tạo
    // để hiển thị các trang/slide thông tin và chỉ báo.

    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    Button backbtn, nextbtn, skipbtn;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_app);
        getSupportActionBar().hide();

        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        skipbtn = findViewById(R.id.skipButton);

        // set events for some buttons
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (getitem(0) > 0){

                    mSLideViewPager.setCurrentItem(getitem(-1),true);

                }

            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // nếu item này trong khoảng 0 < 2 thì tiếp tục chuyển sang slide khác

                if (getitem(0) < 2)
                    mSLideViewPager.setCurrentItem(getitem(1),true);
                else {

                    // nếu > 2 thì chuyển sang giao diện Main App

                    // Main Activity --> Main Home App
                    Intent i = new Intent(IntroAppActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }

            }
        });

        // Bay tới Main APp
        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroAppActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        mSLideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);


        viewPagerAdapter = new ViewPagerAdapter(this);

        mSLideViewPager.setAdapter(viewPagerAdapter);

        // Phương thức setUpindicator() được gọi để thiết lập index, position của image
        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);

    }



    // có thể hiểu một image như là một trang
    // khi chuyển từ hình ảnh này sang hình ảnh khác thò nó chuyển trang

    public void setUpindicator(int position){

        // Một mảng TextView dùng để hiển thị các chấm trang và
        dots = new TextView[3];
        mDotLayout.removeAllViews();

        // Mỗi TextView được thiết lập để hiển thị một chấm chỉ báo và màu sắc được đặt là màu chưa hoạt động.
        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            // chuyển sang hình khác thì "position i" trở thành inactive

            dots[i].setTextColor(getResources().getColor(R.color._inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);

        }

        //vị trí được chỉ định bởi "position" được thiết lập để hiển thị màu sắc hoạt động. đã được định nghĩa
        dots[position].setTextColor(getResources().getColor(R.color._active,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                backbtn.setVisibility(View.VISIBLE);

            }else {

                backbtn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    // hàm này dùng để  để lấy trang hiện tại của ViewPager

    //xác định trang kế tiếp/trước khi người dùng nhấp vào các nút back/next.
    private int getitem(int i){

        // chuyển sang một slidee image mới
        return mSLideViewPager.getCurrentItem() + i;
    }
}