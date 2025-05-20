package com.example.bakeart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class UploadActivity extends AppCompatActivity {

    private EditText titleInput, ingredientsInput, stepsInput, funFactInput;
    private Button uploadBtn, selectImageBtn;
    private ImageView imageView;
    private Uri imageUri;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        titleInput = findViewById(R.id.titleInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        stepsInput = findViewById(R.id.stepsInput);
        funFactInput = findViewById(R.id.funFactInput);
        uploadBtn = findViewById(R.id.uploadBtn);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        imageView = findViewById(R.id.imagePreview);

        selectImageBtn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, 101);
        });

        uploadBtn.setOnClickListener(v -> uploadRecipe());
    }

    private void uploadRecipe() {
        String title = titleInput.getText().toString().trim();
        String ingredients = ingredientsInput.getText().toString().trim();
        String steps = stepsInput.getText().toString().trim();
        String funFact = funFactInput.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(steps)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.uploadRecipe(title, ingredients, steps, funFact, imageFile, success -> {
            if (success) {
                Toast.makeText(this, "Recipe uploaded!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == 101 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageFile = FileUtils.getFileFromUri(this, imageUri);
        }
    }
}
