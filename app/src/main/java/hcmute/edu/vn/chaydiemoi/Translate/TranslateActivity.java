package hcmute.edu.vn.chaydiemoi.Translate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hcmute.edu.vn.chaydiemoi.MainActivity;
import hcmute.edu.vn.chaydiemoi.R;
import hcmute.edu.vn.chaydiemoi.Scan.ScanActivity;

public class TranslateActivity extends AppCompatActivity {

    EditText editText_toTranslate ;
    TextView textView_DestinationTranslated;
    MaterialButton btn_chooseSourceLanguage, btn_chooseDestinationLanguage, btn_startTranslate;

    // Copy and Paste :3
    ImageView imgView_Copy, imgView_Paste, imgView_Speech;

    // Exchange 2 Languages to each other
    ImageView img_Exchange;


    // translator options to set source and destination languages
    // EX: English --> Japanese
    private TranslatorOptions translatorOptions;

    // translator object, for configuring it with the source and target languages
    private Translator translator;
    private ProgressDialog progressDialog;

    private ArrayList<ModelLanguages> languagesArrayList;

    private static final String TAG = "Luân nèk";
    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "vi";
    private String destinationLanguageTitle = "Vietnamese";

    // Speech
    private TextToSpeech tts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        getSupportActionBar().hide();

        editText_toTranslate = findViewById(R.id.edtTextToTranslate);
        textView_DestinationTranslated =findViewById(R.id.txtDestinationTranslated);
        btn_chooseSourceLanguage = findViewById(R.id.btnChoose_SourceLanguage);
        btn_chooseDestinationLanguage =findViewById(R.id.btnChoose_DestinationLanguage);
        btn_startTranslate =findViewById(R.id.btnTranslate);

        // copy and paste

        imgView_Copy = findViewById(R.id.imgCopy_TextViewTranslated);
        imgView_Paste = findViewById(R.id.imgPaste_inEditText);

        // speech
        imgView_Speech = findViewById(R.id.imgSpeech_inTextView);

        // exchange
        img_Exchange = findViewById(R.id.imgExchange);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please waiting...");
        progressDialog.setCanceledOnTouchOutside(false);


        loadAvailableLanguages();


        // set events
        btn_chooseSourceLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceLanguageChoose();
            }
        });


        btn_chooseDestinationLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationLanguageChoose();
            }
        });



        // handle Translate button click, translate text to desired Languages
        btn_startTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalidateData();
            }
        });

        // Copy
        imgView_Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy_TextIsTranslated();
            }
        });

        imgView_Paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paste_TextInClipboard();
            }
        });

        img_Exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchange_twoLanguages();//
                //exchange_twoLanguages2();
            }
        });

        // speech
        imgView_Speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech_textTranslated();
            }
        });

    }

    //để hệ thống đọc  văn bản đã được dịch sang ngôn ngữ khác.
    private void speech_textTranslated() {

        //lắng nghe sự kiện khởi tạo của TextToSpeech.
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            // phương thức callback được gọi khi TextToSpeech được khởi tạo.

            @Override
            public void onInit(int status) {

                //kiểm tra xem trạng thái khởi tạo của TextToSpeech có thành công hay không
                if (status == TextToSpeech.SUCCESS) {

//                    thiết lập ngôn ngữ và tốc độ phát âm
                    tts.setLanguage(Locale.US); // US
                    tts.setSpeechRate(1.0f);    // 1.0

                    //sử dụng phương thức "speak" của TextToSpeech để
                    // phát giọng nói cho văn bản đã được dịch sang ngôn ngữ khác.
                    tts.speak(textView_DestinationTranslated.getText().toString() ,TextToSpeech.QUEUE_ADD, null);
                }
            }
        });
    }

    // swap
    //swap/ hoán đổi vị trí hai ngôn ngữ được chọn để dịch

    // biến temporary cho 2 biến sau: LanguageCode and LanguageTitle
    //để lưu trữ giá trị của ngôn ngữ trước khi đổi chỗ chúng.
    String tmpLanguageCode;
    String tmpLanguageTitle;
    private void exchange_twoLanguages() {

        //bắt đầuthực hiện hoán đổi/ vị trí giữa hai ngôn ngữ

        // Đổi chỗ 2 ngôn ngữ được chọn
        tmpLanguageCode = sourceLanguageCode;
        tmpLanguageTitle = sourceLanguageTitle;

        sourceLanguageCode = destinationLanguageCode;
        sourceLanguageTitle = destinationLanguageTitle;

        destinationLanguageCode = tmpLanguageCode;
        destinationLanguageTitle = tmpLanguageTitle;

        // Đổi nội dung của EditText và TextView
        String sourceText = editText_toTranslate.getText().toString().trim();
        editText_toTranslate.setText(textView_DestinationTranslated.getText().toString().trim());
        textView_DestinationTranslated.setText(sourceText);

        // Cập nhật lại các nút và hộp văn bản cho ngôn ngữ đã được đổi chỗ

        btn_chooseSourceLanguage.setText(sourceLanguageTitle);
//        editText_toTranslate.setHint("Enter " + sourceLanguageTitle);
        btn_chooseDestinationLanguage.setText(destinationLanguageTitle);

        //cập nhật dữ liệu sau khi đã hoán đổi giữa hai ngôn ngữ.
        invalidateData();
    }



    // Lưu trữ nội dung văn bản đã được dịch từ ngôn ngữ trước đó
    //String translatedText2 = textView_DestinationTranslated.getText().toString().trim();


    // dán nội dung đã được copy vào trong clipboard gần đây nhất vào trong editText_toTranslate
    private void paste_TextInClipboard() {

        //ClipboardManager được sử dụng để quản lý dữ liệu trong clipboard.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // kiểm tra trong clipboard có dữ liệu không?
        if (clipboard.hasPrimaryClip()) {



            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            if (item != null) {
                // nếu có dữ liệu trong clipboard
                // thì lấy dữ liệu đầu tiên trong clipboard "(getItemAt(0))"
                // gán vào biến textToPaste

                String textToPaste = item.getText().toString();

                // Kiểm tra nội dung của EditText
                if (!editText_toTranslate.getText().toString().isEmpty()) {

                    // Nếu EditText không rỗng, xóa - ý là nó có chữ trong đó đó :")
                    editText_toTranslate.setText("");       // xóa đi nội dung trong editText này
                }

                // Dán nội dung copy gần nhất vào EditText
                editText_toTranslate.setText(textToPaste);
                Log.e("Text is", "Pasted! ");
                // Thông báo message cho người dùng đã paste thành công nội dung vừa trong clipboard!
                Toast.makeText(TranslateActivity.this, "Text is Pasted...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // copy nội dung được dịch vào trong clipboard
    private void copy_TextIsTranslated() {
        // Kiểm tra nếu trong Edit Text Regcognition có rỗng không/ có chứa nội dung hay không?

        // khác rỗng thực hiện copy - tức lá chứa nội dung trong đó
        if (!TextUtils.isEmpty(textView_DestinationTranslated.getText().toString())) {

            // ClipboardManager được tạo ra và dữ liệu được lưu trữ trong ClipData
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            //
            ClipData clipData = ClipData.newPlainText("Copy",textView_DestinationTranslated.getText().toString());

            // ClipData được gán cho clipboard bằng cách sử dụng phương thức "setPrimaryClip".

            // setPrimaryClip() là phương thức của lớp ClipboardManager sử dụng để ghi dữ liệu vào clipboard.
            clipboardManager.setPrimaryClip(clipData);      // ghi dữ liệu của clipData vào trong clipboard


            Log.e("Text is", "Copied! ");
            Toast.makeText(TranslateActivity.this, "Text is Copied...",Toast.LENGTH_SHORT).show();
        }

        else {  // thông báo Empty không copy được
            Toast.makeText(TranslateActivity.this, "Luân Bảo là EMPTY!, Cannot copy",Toast.LENGTH_SHORT).show();
        }

    }


    //hiển thị danh sách ngôn ngữ và chọn ngôn ngữ sourceLanguage cho ứng dụng
    private void sourceLanguageChoose() {
        // init PopMenu param 1 is context
        // param 2 is the UI View arround which we want to show the Popup Menu
            // to choose source language from list

        //hiển thị danh sách các ngôn ngữ có thể được chọn.
        PopupMenu popupMenu = new PopupMenu(this, btn_chooseSourceLanguage);


        // from languageArrayList we will display language titles
        // languagesArrayList chứa danh sách các ngôn ngữ có thể được chọn
        for (int i = 0 ; i < languagesArrayList.size(); i++) {

            // keep adding titles in Popup Menu item
                // param 1 is groupID
                // param 2 is itemID
                // param 3 is order
                // param 4 is title
            popupMenu.getMenu().add(Menu.NONE, i, i, languagesArrayList.get(i).languageTitle);
        }

        // show Popup Menu
        popupMenu.show();

        // handle Popup Menu Item Click
        // xử lý sự kiện khi người dùng chọn ngôn ngữ nguồn từ danh sách popup menu.
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // get clicked item id which is position/index from the list
                int position = item.getItemId();

                // get code and title of the language selected
                sourceLanguageCode = languagesArrayList.get(position).languageCode;
                sourceLanguageTitle = languagesArrayList.get(position).languageTitle;

                // set the selected language to btn_chooseSourceLanguage as 'Text'
                    // and editText_toTranslate as 'Hint'
                btn_chooseSourceLanguage.setText(sourceLanguageTitle);
                editText_toTranslate.setHint("Enter" + sourceLanguageTitle);

                // show in logs
                Log.d(TAG, "OnMenuItemClick: sourceLanguageCode: " + sourceLanguageCode );
                Log.d(TAG, "OnMenuItemClick: sourceLanguageTitle: " + sourceLanguageTitle );


                return false;
            }
        });

    }


    // tương tự như source LanguageChoose
    private void destinationLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, btn_chooseDestinationLanguage);


        // from languageArrayList we will display language titles
        for (int i = 0 ; i < languagesArrayList.size(); i++) {

            // keep adding titles in Popup Menu item
            // param 1 is groupID
            // param 2 is itemID
            // param 3 is order
            // param 4 is title
            popupMenu.getMenu().add(Menu.NONE, i, i, languagesArrayList.get(i).getLanguageTitle());
        }

        //hiển thị lên em ơi!
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();


                destinationLanguageCode = languagesArrayList.get(position).languageCode;
                destinationLanguageTitle = languagesArrayList.get(position).languageTitle;


                btn_chooseDestinationLanguage.setText(destinationLanguageTitle);
                Log.d(TAG, "onMenuItemClick: destinationLanguageCode: " + destinationLanguageCode);
                Log.d(TAG, "onMenuItemClick: destinationLanguageTitle: " + destinationLanguageTitle);


                return false;
            }
        });


    }



//  tải danh sách các ngôn ngữ có sẵn và thêm chúng vào một ArrayList để sử dụng trong ứng dụng
    private void loadAvailableLanguages() {

        // init language array list  before staring adding data into it

        //lưu trữ danh sách các ngôn ngữ có sẵn.
        languagesArrayList = new ArrayList<>();


        // get list of all language code like this: en, th, zh, vi

        // lấy danh sách tất cả các ngôn ngữ có sẵn trong ứng dụng
        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        // to make list containing both the languages code 'en' and the Language Title is 'English'

        // duyệt qua sách các ngôn ngữ có sẵn được lấy ra từ 'TranslateLanguage'
        for (String languageCode: languageCodeList) {

            //set language title from language code
            // EX en --> English
            // tên ngôn ngữ được lấy từ mã ngôn ngữ thông qua 'Locale'
            String languageTitle = new Locale(languageCode).getDisplayLanguage();

            Log.d("Luân NÈK","Load Available : languageCode" + languageCode);
            Log.d("Luân NÈK","Load Available : languageTitle" + languageTitle);

            // prepate language model and add in into list
            //Lưu trữ mã ngôn ngữ và tên ngôn ngữ.
            ModelLanguages modelLanguages = new ModelLanguages(languageCode, languageTitle);
            languagesArrayList.add(modelLanguages); // add vào danh sách

        }

    }



    private  String sourceLanguageText;     // lưu trữ nội dung văn bản cần dịch
    private void invalidateData() {

        // gán nội dung của editText_toTranslate cho sourceLanguageText
        sourceLanguageText = editText_toTranslate.getText().toString().trim();

        Log.d(TAG, "Validate Data: sourceLanguageText:  " + sourceLanguageText);




        // kiểm tra đầu vào nếu empty thì thông báo
        if(sourceLanguageText.isEmpty()) {
            Toast.makeText(this, "Enter text to translate...", Toast.LENGTH_SHORT).show();
        }
        else {
            // bắt đầu dịch
            startTranslation();
        }

    }

    // bắt đầu dịch ngôn ngữ đã đc chọn
    private void startTranslation() {

        // hiển thị progress bar thông báo cho người dùng biết rằng 'app đang lấy dữ liệu về'
        progressDialog.setMessage("Processing language model...");
        progressDialog.show();


        // init TranslatorOptions with source and target languages
        // đối tượng TranslatorOptions với mã ngôn ngữ nguồn
        // /và đích được thiết lập từ biến sourceLanguageCode và destinationLanguageCode.
        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();

        translator = Translation.getClient(translatorOptions);

        // start downloading with option to requireWifi (Optional)
        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        // start download translation model if require (will download 1st time)
        // kết nối internet để tải model
        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) { // tỉa model xuống thành công
                        // translation model ready to be translated, lets translate

                        // bắt đầu dịch
                        Log.d(TAG, "onSuccess model ready starting translate...");
                        progressDialog.setMessage("Translating.....");

                        // start translation process
                        translator.translate(sourceLanguageText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {

                                        // successfully translated

                                        Log.e("OnSuccess: ", translatedText);
                                        textView_DestinationTranslated.setText(translatedText);
                                        progressDialog.dismiss(); // đóng progressDialog sau khi dịch xong
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        // failed to translate
                                        progressDialog.dismiss();
                                        Log.e("onFailure: ", e.getMessage());

                                        Toast.makeText(TranslateActivity.this, "Failed to translate due to: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                })

                // nếu model chưa tải xuống thì bắt đầu tải
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // failed to ready translation mode, can procced to translation
                        progressDialog.dismiss();
                        Log.e("onFailure: ", e.getMessage());

                        Toast.makeText(TranslateActivity.this, "Failed to translate due to: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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