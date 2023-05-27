package hcmute.edu.vn.chaydiemoi.Connect;

public class Student {
    private int image; // set cứng image thông qua drawable nên phải dùng int
    private int studentID;
    private int studentName;

    public Student() {
    }

    public Student(int image, int studentID, int studentName) {
        this.image = image;
        this.studentID = studentID;
        this.studentName = studentName;
    }



}
