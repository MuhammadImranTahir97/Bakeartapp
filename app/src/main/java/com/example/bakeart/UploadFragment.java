package com.example.bakeart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;

import java.util.*;

public class UploadFragment extends Fragment {

    private EditText titleInput, ingredientsInput, stepsInput, funFactInput;
    private Button uploadBtn, selectImageBtn;
    private ImageView imageView;
    private Uri imageUri;
    private DatabaseReference recipeDbRef;
    private StorageReference storageRef;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        titleInput = view.findViewById(R.id.titleInput);
        ingredientsInput = view.findViewById(R.id.ingredientsInput);
        stepsInput = view.findViewById(R.id.stepsInput);
        funFactInput = view.findViewById(R.id.funFactInput);
        uploadBtn = view.findViewById(R.id.uploadBtn);
        selectImageBtn = view.findViewById(R.id.selectImageBtn);
        imageView = view.findViewById(R.id.imagePreview); // match your layout

        recipeDbRef = FirebaseDatabase.getInstance().getReference("recipes");
        storageRef = FirebaseStorage.getInstance().getReference("recipe_images");

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imageView.setImageURI(imageUri);
                    }
                }
        );

        selectImageBtn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            imagePickerLauncher.launch(i);
        });

        uploadBtn.setOnClickListener(v -> uploadRecipe());

        return view;
    }

    private void uploadRecipe() {
        String title = titleInput.getText().toString().trim();
        String ingredients = ingredientsInput.getText().toString().trim();
        String steps = stepsInput.getText().toString().trim();
        String funFact = funFactInput.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(steps)) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Recipe Uploaded!", Toast.LENGTH_SHORT).show();
                                clearForm();
                            }
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearForm() {
        titleInput.setText("");
        ingredientsInput.setText("");
        stepsInput.setText("");
        funFactInput.setText("");
        imageView.setImageResource(android.R.color.transparent);
        imageUri = null;
    }
}
