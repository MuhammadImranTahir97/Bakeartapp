package com.example.bakeart.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakeart.R;
import com.example.bakeart.Recipe;
import com.example.bakeart.api.RecipeApi;
import com.example.bakeart.api.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public void updateList(List<Recipe> newList) {
    this.recipeList = newList;
    notifyDataSetChanged();
}

