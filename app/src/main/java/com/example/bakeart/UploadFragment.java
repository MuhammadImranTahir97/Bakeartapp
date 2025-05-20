package com.example.bakeart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

public class UploadFragment extends Fragment {

    private EditText titleInput, ingredientsInput, stepsInput, funFactInput;
    private Button uploadBtn, selectImageBtn;
    private ImageView imageView;
    private Uri imageUri;
    private File imageFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        titleInput = view.findViewById(R.id.titleInput);
        ingredientsInput = view.findViewById(R.id.ingredientsInput);
        stepsInput = view.findViewById(R.id.stepsInput);
        funFactInput = view.findViewById(R.id.funFactInput);
        uploadBtn = view.findViewById(R.id.uploadBtn);
        selectImageBtn = view.findViewById(R.id.selectImageBtn);
        imageView = view.findViewById(R.id.imagePreview);

        selectImageBtn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, 101);
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

        if (imageFile == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.uploadRecipe(title, ingredients, steps, funFact, imageFile, success -> {
            if (success) {
                Toast.makeText(getContext(), "Recipe uploaded!", Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(getContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        titleInput.setText("");
        ingredientsInput.setText("");
        stepsInput.setText("");
        funFactInput.setText("");
        imageView.setImageResource(android.R.color.transparent);
        imageUri = null;
        imageFile = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageFile = FileUtils.getFileFromUri(getContext(), imageUri);
        }
    }
}
