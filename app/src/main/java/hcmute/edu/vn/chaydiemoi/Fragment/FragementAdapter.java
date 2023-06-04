package hcmute.edu.vn.chaydiemoi.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragementAdapter extends FragmentStateAdapter {

    public FragementAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    //tạo ra một Fragment tại vị trí được chỉ định trong ViewPager2.
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //xác định Fragment cần được tạo ra dựa trên vị trí của nó trong ViewPager2.
        switch (position) {
            //trả về một đối tượng HowToUseAppFragment.
            case 0:
                return new  HowToUseAppFragment();

                //trả về một đối tượng AboutAppFragment.
            default:
                return new AboutAppFragment();

        }

    }

    //trả về số lượng các Fragment trong ViewPager2.
    @Override
    public int getItemCount() {
        // nếu không trả về số lượng tab layout thì đừng hòng anh mài hiện lên

        //trả về 2 vì chúng ta chỉ có 2 Fragment
        // (HowToUseAppFragment và AboutAppFragment) để hiển thị trong ViewPager2.
        return 2;

    }
}
