package com.example.bakeart;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

            // Fetch all recipes
            ApiClient.getAllRecipes(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    resultList.clear();
                    if (response.isSuccessful() && response.body() != null) {
                        for (Recipe r : response.body()) {
                            if (r.getIngredients() != null) {
                                String ingredients = r.getIngredients().toLowerCase();
                                for (String word : queryWords) {
                                    if (ingredients.contains(word.trim())) {
                                        resultList.add(r);
                                        break;
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No results found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    Toast.makeText(getContext(), "Search failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return v;
    }
}
