package hcmute.edu.vn.chaydiemoi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import hcmute.edu.vn.chaydiemoi.R;

public class AboutUs_ViewPageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    int images[] = {
            R.drawable.img_luan_about_us,
            R.drawable.img_thoai,
            R.drawable.img_khoa,
    };

    int studentID[] = {
            R.string.studentID_one,
            R.string.studentID_two,
            R.string.studentID_three,
    };

    int studentName[] = {
            R.string.studentName_one,
            R.string.studentName_two,
            R.string.studentName_three,
    };

    public AboutUs_ViewPageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        //return images.length;
        return studentID.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }


    ImageView imageView;
    TextView studentIDTextView;
    TextView studentNameTextView;
    View view;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //layoutInflater = LayoutInflater.from(context);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        view   = layoutInflater.inflate(R.layout.abous_us_slide_image, container, false);

        imageView = view.findViewById(R.id.titleImage);
        studentIDTextView = view.findViewById(R.id.textStudentID);
        studentNameTextView = view.findViewById(R.id.textStudentName);

        imageView.setImageResource(images[position]);
        studentIDTextView.setText(studentID[position]);
        studentNameTextView.setText(studentName[position]);

//        container.addView(view, 0);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

}