package hcmute.edu.vn.chaydiemoi.Connect;

public class Intro {


    private int image; // set cứng image thông qua drawable nên phải dùng int
    private int heading;
    private int description;

    public Intro(int image, int heading, int description) {
        this.image = image;
        this.heading = heading;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public int getHeading() {
        return heading;
    }

    public int getDescription() {
        return description;
    }
}
