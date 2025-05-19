package com.example.bakeart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ModeratorFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Recipe> recipeList;
    private DatabaseReference dbRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_recipe_list, container, false);

        recyclerView = v.findViewById(R.id.recipeRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeList = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference();

        RecipeAdapter adapter = new RecipeAdapter(recipeList, true);  // moderator = true
        recyclerView.setAdapter(adapter);

        dbRef.child("recipes")
                .orderByChild("category")
                .equalTo("Try")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        recipeList.clear();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            Recipe r = s.getValue(Recipe.class);
                            if (r != null) {
                                recipeList.add(r);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Error loading recipes.", Toast.LENGTH_SHORT).show();
                    }
                });

        return v;
    }
}
