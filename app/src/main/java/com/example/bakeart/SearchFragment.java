package com.example.bakeart;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private Button searchBtn;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> resultList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = v.findViewById(R.id.searchInput);
        searchBtn = v.findViewById(R.id.searchBtn);
        recyclerView = v.findViewById(R.id.searchResultsRecycler);

        resultList = new ArrayList<>();
        adapter = new RecipeAdapter(resultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        searchBtn.setOnClickListener(view -> {
            String input = searchInput.getText().toString().toLowerCase().trim();

            if (TextUtils.isEmpty(input)) {
                Toast.makeText(getContext(), "Please enter at least one ingredient.", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] queryWords = input.split(",");

            FirebaseDatabase.getInstance().getReference("recipes")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            resultList.clear();
                            for (DataSnapshot s : snapshot.getChildren()) {
                                Recipe r = s.getValue(Recipe.class);
                                if (r != null && r.ingredients != null) {
                                    String recipeIngredients = r.ingredients.toLowerCase();
                                    for (String word : queryWords) {
                                        if (recipeIngredients.contains(word.trim())) {
                                            resultList.add(r);
                                            break;
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return v;
    }
}
