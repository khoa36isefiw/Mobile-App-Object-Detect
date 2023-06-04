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

import java.util.ArrayList;

import hcmute.edu.vn.chaydiemoi.R;

// adapteer này được sử dụng để hiển thị các trang trong ViewPager
public class ViewPagerAdapter extends PagerAdapter {

    Context context;    // để truy cập tài nguyên của ứng dụng.


    // start region

    // region này khai báo cái mảng Images, headings và descripton
        // dùng để lưu trữu thông tin của các slide trong ViewPage
    int images[] = {    // dàng mản int để chứa các hihf ảnh là do ta add trực tieps từ drawable vào
            // thì nó mang kiểu "Integer"

            R.drawable.img_textrecog,
            R.drawable.img_translate,
            R.drawable.img_speech,
    };



    int headings[] = {

            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three,
    };

    int description[] = {

      R.string.desc_one,
      R.string.desc_two,
      R.string.desc_three,
    };
    // end  region




    public ViewPagerAdapter(Context context){


        this.context = context;

    }

    //để trả về số lượng các headings e trong ViewPager.
    @Override
    public int getCount() {
        return  headings.length;
    }

    //   kiểm tra xem một đối tượng view có thuộc về một đối tượng object đã được tạo ra hay không.
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // trả về true nếu view tham chiếu đến object đã được tạo ra
        return view == (LinearLayout) object;

        // false thì object không được tạo ra

    }

    ImageView slidetitleimage;
    TextView slideHeading;
    TextView slideDesciption;
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        slidetitleimage =  view.findViewById(R.id.titleImage);
        slideHeading =  view.findViewById(R.id.texttitle);
        slideDesciption=  view.findViewById(R.id.textdeccription);

        // lấy thông tin hình ảnh tương ứng
        slidetitleimage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDesciption.setText(description[position]);

        container.addView(view);

        return view;

    }

    // nếu như 1 trang slide không còn được sử dụng nữa thì loại bỏ ViewPage, slide được xóa khỏi container
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);

    }
}
