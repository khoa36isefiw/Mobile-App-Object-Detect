package hcmute.edu.vn.chaydiemoi.Home;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.Locale;

import hcmute.edu.vn.chaydiemoi.MainActivity;
import hcmute.edu.vn.chaydiemoi.R;
import hcmute.edu.vn.chaydiemoi.Scan.ScanActivity;
import hcmute.edu.vn.chaydiemoi.Translate.TranslateActivity;

public class Tab extends AppCompatActivity {
    //GUI
    private MaterialButton btn_inputImage, btn_recognizeText, btn_LuanSpeech, btn_LuanTranslate, btn_Tab;
    private ShapeableImageView imgInput;
    private EditText editTextRegText;

    //TAG;
    private static final String TAG = "MAIN_TAG";

    private Uri imageUri = null;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    // check permission and required to pick image from Gallery/ Camera
    private  String[] cameraPermissions;
    private  String[] storagePermissions;

    // progress dialog
    private ProgressDialog progressDialog;
    private TextRecognizer textRecognizer;

    // Speech
    private TextToSpeech tts;

    // Copy All Text to Clipboard
    ImageView img_Copy;


    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_tab_layout);
        getSupportActionBar().hide();

        btn_inputImage = findViewById(R.id.btnInputImage);
        btn_recognizeText = findViewById(R.id.btnRecognizeText);
        imgInput = findViewById(R.id.imgView);
        editTextRegText = findViewById(R.id.edtRecognizeText);

        // change another color when we click on some tabs

        tabLayout = findViewById(R.id.tabIcon);


        // init array of permissions required for camera and gallery
        cameraPermissions = new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Waiting...");
        progressDialog.setCanceledOnTouchOutside(false);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);


//        editTextRegText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // khi click vào edit Text thì clear đi nội dung có nó
//                //editTextRegText.setText("");
//            }
//        });

        // show image dialog

        // There are 2 options to input IMAGE
            //  -- Camera
            //  -- Gallery

        btn_inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputImageDialog();
            }
        });

        btn_recognizeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    // means we haven't image yet
                    Toast.makeText(Tab.this, "Please, Pick Image...",Toast.LENGTH_SHORT).show();
                }
                else {
                    // picked image --> recognize
                    recognizeTextFromImage();
                }
            }
        });







        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Get the position of the selected tab
                int tabPosition = tab.getPosition();

                // Perform some action based on the selected tab
                switch (tabPosition) {
                    case 0: // speaker
                        // Perform some action for the third tab
                        speechText_OnlyENGLISH();
                        break;
                    case 1: // open transalte GUI
                        Intent intent = new Intent(Tab.this , TranslateActivity.class);
                        startActivity(intent);
                        Log.e("Move to:", "Translate Text");
                        break;
                    case 2:     // Copy all Text in EditText is recognized

                        // Show a Toast message for the first tab
                        copyAllText();

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });


    }


    private void copyAllText() {

        // Kiểm tra nếu trong Edit Text Regcognition có rỗng không?
        if (!TextUtils.isEmpty(editTextRegText.getText().toString())) { // khác rỗng thực hiện copy
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Copy",editTextRegText.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
            Log.e("Text is", "Copied! ");
            Toast.makeText(Tab.this, "Text Copied...",Toast.LENGTH_SHORT).show();
        }

        else {  // thông báo Empty không copy được
            Toast.makeText(Tab.this, "Luân Bảo là EMPTY!, Cannot copy",Toast.LENGTH_SHORT).show();
        }

    }

    private void speechText_OnlyENGLISH() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                    tts.setSpeechRate(1.0f);
                    Log.e("Starting:", "Speech");
                    tts.speak(editTextRegText.getText().toString() ,TextToSpeech.QUEUE_ADD, null);
                }
            }
        });
    }



    private void recognizeTextFromImage() {
        Log.d(TAG, "recognizedTextFromImage: ");
        progressDialog.setMessage("Preparing text...");
        progressDialog.show();


        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);

            progressDialog.setMessage("Recognizing text...");
            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();
                            String recognizedText  = text.getText();
                            Log.d(TAG, "recognizedTextFromImage: " + recognizedText);
                            editTextRegText.setText(recognizedText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.e(TAG, "onFailure: ", e);
                            Toast.makeText(Tab.this, "Failed recognizing text due to" +e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        catch (Exception e) {
            progressDialog.dismiss();
            //e.printStackTrace();
            Log.e(TAG, "recognizeTextFromImage: ", e);
            Toast.makeText(Tab.this, "Failed preparing image due to...",Toast.LENGTH_SHORT).show();
        }


    }




    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(this, btn_inputImage);
        MenuItem cameraItem = popupMenu.getMenu().add(Menu.NONE, 1, 1, "CAMERA");
        MenuItem galleryItem = popupMenu.getMenu().add(Menu.NONE, 2, 2, "GALLERY");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 1) {
                    // cam is clicked, check if cam permission is granted or not?
                    Log.d(TAG, "onMenuItemClick: Camera Clicked...");
                    if(checkCameraPermissions()) {
                        // camera permissions granted, can launch camera intent
                        pickImageFromCamera();
                    }
                    else {
                        // not granted --> request the camera permission
                        requestCameraPermissions();
                    }
                }
                else if (id == 2) {
                    // gallery is clicked, check if gallery permission is granted or not?
                    Log.d(TAG, "onMenuItemClick: Gallery Clicked...");
                    if (checkStoragePermissions()) {
//                       // gallery permissions granted, can launch gallery intent
                        pickImageFromGallery();
                    }
                    else {

                        requestStoragePermissions();
                    }
                }
                return true;
            }
        });
    }

    private void pickImageFromGallery() {
        Log.d(TAG, "pick Image From Gallery...");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }


    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    // we will receive the image if picked
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        // image picked
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: imageUri " + imageUri);

                        // set to imageView
                        imgInput.setImageURI(imageUri);
                    }
                    else {
                        Log.d(TAG, "onActivityResult: Failed");
                        Toast.makeText(Tab.this, "Don't Upload Image...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private void pickImageFromCamera() {
        Log.d(TAG, "pick Image From Camera...");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");


        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    // we will receive the image if took from the camera
                    if(result.getResultCode() == Activity.RESULT_OK) {

                        // image is took from the camera
                        // we already have the image in imageUri through pickImageFromCamera()
                        imgInput.setImageURI(imageUri);

                    }
                    else {
                        Toast.makeText(Tab.this, "Cancelled!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private boolean checkStoragePermissions() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return  result;
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    // camera
    private boolean checkCameraPermissions() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean storageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return cameraResult && storageResult;
    }


    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    // handle permission results

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length >0 ) {
                    boolean cameraAccpeted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccpeted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccpeted && storageAccpeted) {
                        pickImageFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera and Storage permissions are required!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Cancelled!", Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccpeted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccpeted) {
                        pickImageFromGallery();
                    }
                    else {
                        Toast.makeText(this, " Storage permission is required!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

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
