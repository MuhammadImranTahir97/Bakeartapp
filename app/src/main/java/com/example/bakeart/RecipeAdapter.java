package com.example.bakeart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private boolean isModerator;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    // Constructor for regular user
    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.isModerator = false;
    }

    // Constructor for moderator view
    public RecipeAdapter(List<Recipe> recipeList, boolean isModerator) {
        this.recipeList = recipeList;
        this.isModerator = isModerator;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, ingredients, steps, likeCount;
        ImageButton likeBtn, favBtn;
        LinearLayout container;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipeImage);
            title = itemView.findViewById(R.id.recipeTitle);
            ingredients = itemView.findViewById(R.id.recipeIngredients);
            steps = itemView.findViewById(R.id.recipeSteps);
            likeCount = itemView.findViewById(R.id.likeCount);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            favBtn = itemView.findViewById(R.id.favBtn);
            container = (LinearLayout) itemView; // Assumes root in recipe_item.xml is LinearLayout
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe r = recipeList.get(position);

        holder.title.setText(r.title);
        holder.ingredients.setText("Ingredients:\n" + r.ingredients);
        holder.steps.setText("Steps:\n" + r.steps);
        holder.likeCount.setText(String.valueOf(r.likeCount));

        Picasso.get().load(r.imageUrl).into(holder.imageView);

        // Like Button
        holder.likeBtn.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) return;
            String uid = auth.getCurrentUser().getUid();
            dbRef.child("likes").child(r.id).child(uid).setValue(true);
            dbRef.child("recipes").child(r.id).child("likeCount").setValue(r.likeCount + 1);
        });

        // Favorite Button
        holder.favBtn.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) return;
            String uid = auth.getCurrentUser().getUid();
            dbRef.child("favorites").child(uid).child(r.id).setValue(r);
        });

        // Moderator "Verify" Button
        if (isModerator) {
            Button verifyBtn = new Button(holder.itemView.getContext());
            verifyBtn.setText("Verify Recipe");

            verifyBtn.setOnClickListener(v -> {
                dbRef.child("recipes").child(r.id).child("category").setValue("Verified");
                dbRef.child("recipes").child(r.id).child("verificationNote")
                        .setValue("Your recipe has been verified!");
                Toast.makeText(holder.itemView.getContext(), "Recipe Verified!", Toast.LENGTH_SHORT).show();
            });

            holder.container.addView(verifyBtn);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
