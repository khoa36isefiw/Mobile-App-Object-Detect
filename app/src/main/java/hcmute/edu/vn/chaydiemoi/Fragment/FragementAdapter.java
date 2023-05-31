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

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new  HowToUseAppFragment();
            default:
                return new AboutAppFragment();

        }

    }

    @Override
    public int getItemCount() {
        // nếu không trả về số lượng tab layout thì đừng hòng anh mài hiện lên

        return 2;
    }
}
