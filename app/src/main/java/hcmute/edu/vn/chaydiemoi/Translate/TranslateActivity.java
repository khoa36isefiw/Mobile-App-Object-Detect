package hcmute.edu.vn.chaydiemoi.Translate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    ImageView imgView_Copy, imgView_Paste;

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

    }

    // swap
    String tmpLanguageCode;
    String tmpLanguageTitle;
    private void exchange_twoLanguages() {
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
        editText_toTranslate.setHint("Enter " + sourceLanguageTitle);
        btn_chooseDestinationLanguage.setText(destinationLanguageTitle);

        invalidateData();
    }
    // Lưu trữ nội dung văn bản đã được dịch từ ngôn ngữ trước đó
    //String translatedText2 = textView_DestinationTranslated.getText().toString().trim();
    private void exchange_twoLanguages2() {


        // Đổi chỗ 2 ngôn ngữ được chọn
        String tmpLanguageCode = sourceLanguageCode;
        String tmpLanguageTitle = sourceLanguageTitle;
        sourceLanguageCode = destinationLanguageCode;
        sourceLanguageTitle = destinationLanguageTitle;
        destinationLanguageCode = tmpLanguageCode;
        destinationLanguageTitle = tmpLanguageTitle;

        // Cập nhật lại các nút và hộp văn bản cho ngôn ngữ đã được đổi chỗ
        btn_chooseSourceLanguage.setText(sourceLanguageTitle);
        editText_toTranslate.setHint("Enter " + sourceLanguageTitle);
        btn_chooseDestinationLanguage.setText(destinationLanguageTitle);

        // Dịch lại nội dung văn bản đã nhập theo ngôn ngữ mới



        //Log.d(TAG, "Validate Data: destinationLanguageText:  " + translatedText2);


        if(sourceLanguageText.isEmpty()) {
            Toast.makeText(this, "Enter text to translate...", Toast.LENGTH_SHORT).show();
        }
        else {
            startTranslation();
        }
    }

    private void paste_TextInClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            if (item != null) {
                String textToPaste = item.getText().toString();
                // Kiểm tra nội dung của EditText
                if (!editText_toTranslate.getText().toString().isEmpty()) {
                    // Nếu EditText không rỗng, xóa
                    editText_toTranslate.setText("");
                }
                // Dán nội dung copy gần nhất vào EditText
                editText_toTranslate.setText(textToPaste);
                Log.e("Text is", "Pasted! ");
                Toast.makeText(TranslateActivity.this, "Text is Pasted...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copy_TextIsTranslated() {
        // Kiểm tra nếu trong Edit Text Regcognition có rỗng không?
        if (!TextUtils.isEmpty(textView_DestinationTranslated.getText().toString())) { // khác rỗng thực hiện copy
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Copy",textView_DestinationTranslated.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
            Log.e("Text is", "Copied! ");
            Toast.makeText(TranslateActivity.this, "Text is Copied...",Toast.LENGTH_SHORT).show();
        }

        else {  // thông báo Empty không copy được
            Toast.makeText(TranslateActivity.this, "Luân Bảo là EMPTY!, Cannot copy",Toast.LENGTH_SHORT).show();
        }

    }


    private void sourceLanguageChoose() {
        // init PopMenu param 1 is context
        // param 2 is the UI View arround which we want to show the Popup Menu
            // to choose source language from list
        PopupMenu popupMenu = new PopupMenu(this, btn_chooseSourceLanguage);


        // from languageArrayList we will display language titles
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // get clicked item id which is position/index from the list
                int position = item.getItemId();

                // get code and title of he language selected
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




    private void loadAvailableLanguages() {

        // init language array list  before staring adding data into it
        languagesArrayList = new ArrayList<>();


        // get list of all language code like this: en, th, zh, vi

        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        // to make list containing both the languages code 'en' and the Language Title is 'English'
        for (String languageCode: languageCodeList) {

            //set language title from language code
            // EX en --> English
            String languageTitle = new Locale(languageCode).getDisplayLanguage();

            Log.d("Luân NÈK","Load Available : languageCode" + languageCode);
            Log.d("Luân NÈK","Load Available : languageTitle" + languageTitle);

            // prepate language model and add in into list
            ModelLanguages modelLanguages = new ModelLanguages(languageCode, languageTitle);
            languagesArrayList.add(modelLanguages);

        }

    }



    private  String sourceLanguageText;
    private void invalidateData() {

        sourceLanguageText = editText_toTranslate.getText().toString().trim();

        Log.d(TAG, "Validate Data: sourceLanguageText:  " + sourceLanguageText);




        if(sourceLanguageText.isEmpty()) {
            Toast.makeText(this, "Enter text to translate...", Toast.LENGTH_SHORT).show();
        }
        else {
            startTranslation();
        }

    }

    private void startTranslation() {
        progressDialog.setMessage("Processing language model...");
        progressDialog.show();


        // init TranslatorOptions with source and target languages
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
        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // translation model ready to be translated, lets translate

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