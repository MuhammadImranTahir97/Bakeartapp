package com.example.bakeart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/recipes")
    Call<Void> uploadRecipe(@Body Recipe recipe);

    @GET("/recipes")
    Call<List<Recipe>> getAllRecipes();
}
