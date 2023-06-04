package hcmute.edu.vn.chaydiemoi.Translate;

public class ModelLanguages {

    // model này dùng để thực hiện translate Language
    String languageCode;        // lưu trưu code của ngôn ngữ ví dụ English- us, VietNamese -vi
    String languageTitle;   // lưu trữ tên ngôn ngũ

    // contructor
    public ModelLanguages(String languageCode, String languageTitle) {
        this.languageCode = languageCode;
        this.languageTitle = languageTitle;
    }

    // getter and setter để lấy và set properties các kiểu
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }


}
