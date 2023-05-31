package hcmute.edu.vn.chaydiemoi.About;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import hcmute.edu.vn.chaydiemoi.Fragment.FragementAdapter;
import hcmute.edu.vn.chaydiemoi.MainActivity;
import hcmute.edu.vn.chaydiemoi.R;

public class AboutAppActivity extends AppCompatActivity {


    ViewPager2 viewPager2;
    TabLayout tabLayout_aboutApp;

    private FragementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        // hide the Support Acton Bar
        getSupportActionBar().hide();

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager2 = findViewById(R.id.viewPager2);
        adapter = new FragementAdapter(fragmentManager , getLifecycle());
        tabLayout_aboutApp = findViewById(R.id.tablayout_aboutAPP);
        viewPager2.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout_aboutApp, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Usage");
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_baseline_document_scanner_24));
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.black));
                        badgeDrawable.setVisible(true);
                        break;

                    case 1:
                        tab.setText("About APP");
                        tab.setIcon(getResources().getDrawable(R.drawable.ic_about));
                        BadgeDrawable badgeDrawable1 = tab.getOrCreateBadge();
                        badgeDrawable1.setBackgroundColor(getResources().getColor(R.color.black));
                        badgeDrawable1.setVisible(true);
                        break;
                }
            }
        }); tabLayoutMediator.attach();


    }







    // Quay lại màn hình chính - Khi nhấn nút Back
    // nếu không set thì sẽ tự động out app
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}