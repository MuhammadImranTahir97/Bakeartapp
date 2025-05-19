package com.example.bakeart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.*;

import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    private EditText titleInput, ingredientsInput, stepsInput, funFactInput;
    private Button uploadBtn, selectImageBtn;
    private ImageView imageView;
    private Uri imageUri;

    private DatabaseReference recipeDbRef;
    private StorageReference storageRef;

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
        imageView = findViewById(R.id.imagePreview); // use imagePreview from XML

        recipeDbRef = FirebaseDatabase.getInstance().getReference("recipes");
        storageRef = FirebaseStorage.getInstance().getReference("recipe_images");

        selectImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 101);
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

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child(imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String recipeId = recipeDbRef.push().getKey();
                            if (recipeId != null) {
                                Recipe recipe = new Recipe(recipeId, title, ingredients, steps, funFact, "Try", uri.toString());
                                recipeDbRef.child(recipeId).setValue(recipe);
                                Toast.makeText(this, "Recipe Uploaded!", Toast.LENGTH_SHORT).show();
                                clearForm();
                            }
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearForm() {
        titleInput.setText("");
        ingredientsInput.setText("");
        stepsInput.setText("");
        funFactInput.setText("");
        imageView.setImageResource(android.R.color.transparent);
        imageUri = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
