package com.example.bakeart.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bakeart.R;
import com.example.bakeart.api.RecipeApi;
import com.example.bakeart.models.Recipe;
import com.example.bakeart.utils.RetrofitClient;
import java.util.*;
import retrofit2.*;

public class UploadActivity extends AppCompatActivity {

    EditText titleInput, imageInput, ingredientsInput, stepsInput, servingsInput;
    Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        titleInput = findViewById(R.id.titleInput);
        imageInput = findViewById(R.id.imageInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        stepsInput = findViewById(R.id.stepsInput);
        servingsInput = findViewById(R.id.servingsInput);
        uploadBtn = findViewById(R.id.uploadBtn);

        uploadBtn.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", titleInput.getText().toString());
            map.put("imageUrl", imageInput.getText().toString());

            List<String> ingredients = Arrays.asList(ingredientsInput.getText().toString().split(","));
            List<String> steps = Arrays.asList(stepsInput.getText().toString().split(","));

            map.put("ingredients", ingredients);
            map.put("steps", steps);
            map.put("servings", Integer.parseInt(servingsInput.getText().toString()));

            // Optional fields
            map.put("funFact", "Optional Fun Fact");
            map.put("ingredientExplanation", "Optional Ingredient Explanation");

            RecipeApi api = RetrofitClient.getInstance().create(RecipeApi.class);
            api.createRecipe(map).enqueue(new Callback<Recipe>() {
                @Override
                public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(UploadActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UploadActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Recipe> call, Throwable t) {
                    Toast.makeText(UploadActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
