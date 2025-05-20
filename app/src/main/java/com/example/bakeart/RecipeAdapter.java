package com.example.bakeart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private boolean isModerator;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.isModerator = false;
    }

    public RecipeAdapter(List<Recipe> recipeList, boolean isModerator) {
        this.recipeList = recipeList;
        this.isModerator = isModerator;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, ingredients, steps;
        LinearLayout container;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipeImage);
            title = itemView.findViewById(R.id.recipeTitle);
            ingredients = itemView.findViewById(R.id.recipeIngredients);
            steps = itemView.findViewById(R.id.recipeSteps);
            container = (LinearLayout) itemView; // assume LinearLayout as root
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

        holder.title.setText(r.getTitle());
        holder.ingredients.setText("Ingredients:\n" + r.getIngredients());
        holder.steps.setText("Steps:\n" + r.getSteps());

        Picasso.get().load(r.getImageUrl()).into(holder.imageView);

        // Only for moderators
        if (isModerator) {
            Button verifyBtn = new Button(holder.itemView.getContext());
            verifyBtn.setText("Verify Recipe");
            verifyBtn.setOnClickListener(v -> {
                // You should implement this API call
                Toast.makeText(holder.itemView.getContext(), "Marked for verification", Toast.LENGTH_SHORT).show();
            });
            holder.container.addView(verifyBtn);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
