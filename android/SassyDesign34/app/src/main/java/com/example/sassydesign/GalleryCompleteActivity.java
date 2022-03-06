package com.example.sassydesign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GalleryCompleteActivity extends AppCompatActivity {
    ImageView galleryReceipt;
    Intent intent;
    public static Activity galleryCompleteActivity;
    String firebaseurl = "";
    private final int GALLERY_CODE = 10;
    private FirebaseStorage storage;
    Button cropButton;
    Button galleryCompleteButton;
    Uri file;
    int completButtonFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_complete);

        galleryCompleteActivity = GalleryCompleteActivity.this;

        galleryReceipt = findViewById(R.id.galleryReceipt);
        storage = FirebaseStorage.getInstance();

        //갤러리에서 가져오기 버튼 눌렀을 때
        Button galleryGetButton = findViewById(R.id.galleryGetButton);
        galleryGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 10);
            }
        });

        //갤러리를 선택한 후 완료버튼 누르기
        galleryCompleteButton = findViewById(R.id.galleryCompleteButton);
        //FireBase 연결x면 완료버튼 안눌림
        if(completButtonFlag == 0) {
            galleryCompleteButton.setEnabled(false);
            galleryCompleteButton.setBackground(getResources().getDrawable(R.drawable.button_not_enabled));
        }
        galleryCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryCompleteIntent;
                galleryCompleteIntent = new Intent(GalleryCompleteActivity.this, ReceiptAddActivity.class);

                galleryCompleteIntent.putExtra("firebaseurl", firebaseurl);
                startActivity(galleryCompleteIntent);
                GalleryCompleteActivity GA = (GalleryCompleteActivity) GalleryCompleteActivity.galleryCompleteActivity;
                GA.finish();

            }
        });
    }

    public void onSelectImageClick(View view) {
        CropImage.activity(file).setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    //갤러리 연동코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE) {
            file = data.getData();
            CropImage.activity(file).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //크롭 성공시
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.galleryReceipt)).setImageURI(result.getUri());

                StorageReference storageRef = storage.getReference();

                StorageReference riversRef = storageRef.child("photo/" + System.currentTimeMillis() + ".jpg");
                UploadTask uploadTask = riversRef.putFile(result.getUri());
                try {
                    InputStream in = getContentResolver().openInputStream(result.getUri());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    galleryReceipt.setImageBitmap(img);
                    Toast.makeText(GalleryCompleteActivity.this, "사진이 업로드 중입니다 기다려주세요", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(GalleryCompleteActivity.this, "사진이 정상적으로 업로드 되지 않았습니다", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(GalleryCompleteActivity.this, "사진 정상 업로드 성공", Toast.LENGTH_SHORT).show();
                        completButtonFlag = 1;
                        galleryCompleteButton.setEnabled(true);
                        galleryCompleteButton.setBackground(getResources().getDrawable(R.drawable.rectangle_round));
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        firebaseurl = String.valueOf(downloadUrl);
                    }
                });

                //실패시
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }


    }

}