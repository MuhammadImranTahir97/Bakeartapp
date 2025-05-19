package com.example.bakeart;

public class Recipe {
    public String verificationNote;

    public String id;
    public String title;
    public String ingredients;
    public String steps;
    public String funFact;
    public String category;
    public String imageUrl;
    public int likeCount;

    // Empty constructor required by Firebase
    public Recipe() {}

    // Constructor with default likeCount
    public Recipe(String id, String title, String ingredients, String steps,
                  String funFact, String category, String imageUrl) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.funFact = funFact;
        this.category = category;
        this.imageUrl = imageUrl;
        this.likeCount = 0;
    }

    // Optional: Constructor with all fields including likeCount
    public Recipe(String id, String title, String ingredients, String steps,
                  String funFact, String category, String imageUrl, int likeCount) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.funFact = funFact;
        this.category = category;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
    }
}
