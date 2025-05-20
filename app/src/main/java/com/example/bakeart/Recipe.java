package com.example.bakeart;

public class Recipe {
    private String _id;  // MongoDB's default ID field
    private String title;
    private String ingredients;
    private String steps;
    private String funFact;
    private String category;
    private String imageUrl;
    private int likeCount;

    // Empty constructor (required by Gson and Firebase)
    public Recipe() {}

    // Constructor with likeCount defaulted to 0
    public Recipe(String title, String ingredients, String steps,
                  String funFact, String category, String imageUrl) {
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.funFact = funFact;
        this.category = category;
        this.imageUrl = imageUrl;
        this.likeCount = 0;
    }

    // Full constructor
    public Recipe(String _id, String title, String ingredients, String steps,
                  String funFact, String category, String imageUrl, int likeCount) {
        this._id = _id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.funFact = funFact;
        this.category = category;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }

    // Getters and setters (important for Retrofit/Gson)

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
