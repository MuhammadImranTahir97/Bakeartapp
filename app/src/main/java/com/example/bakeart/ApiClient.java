package com.example.bakeart;

import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class ApiClient {

    private static final String BASE_URL = "https://your-api-url.com/api/"; // 🔁 Replace with actual base URL

    private static Retrofit retrofit;
    private static RecipeService recipeService;
    private static UserService userService;

    // Recipe API
    public interface RecipeService {
        @GET("recipes")
        Call<List<Recipe>> getAllRecipes();

        @Multipart
        @POST("recipes")
        Call<Void> uploadRecipe(
                @Part("title") RequestBody title,
                @Part("ingredients") RequestBody ingredients,
                @Part("steps") RequestBody steps,
                @Part("funFact") RequestBody funFact,
                @Part MultipartBody.Part image
        );
    }

    // User Auth API
    public interface UserService {
        @POST("login")
        Call<Void> login(@Body User user);

        @POST("register")
        Call<Void> register(@Body User user);
    }

    // Retrofit init
    private static void initRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
    }

    public static RecipeService getRecipeService() {
        if (recipeService == null) {
            initRetrofit();
            recipeService = retrofit.create(RecipeService.class);
        }
        return recipeService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            initRetrofit();
            userService = retrofit.create(UserService.class);
        }
        return userService;
    }

    public static void getAllRecipes(Callback<List<Recipe>> callback) {
        getRecipeService().getAllRecipes().enqueue(callback);
    }

    public static void uploadRecipe(String title, String ingredients, String steps, String funFact, File imageFile, UploadCallback callback) {
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody ingredientsPart = RequestBody.create(MediaType.parse("text/plain"), ingredients);
        RequestBody stepsPart = RequestBody.create(MediaType.parse("text/plain"), steps);
        RequestBody funFactPart = RequestBody.create(MediaType.parse("text/plain"), funFact);

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);

        getRecipeService().uploadRecipe(titlePart, ingredientsPart, stepsPart, funFactPart, imagePart).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onComplete(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Upload", "Upload failed: " + t.getMessage(), t);
                callback.onComplete(false);
            }
        });
    }

    public interface UploadCallback {
        void onComplete(boolean success);
    }
}
