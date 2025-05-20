const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

// Schema
const Recipe = mongoose.model('Recipe', new mongoose.Schema({
  title: String,
  ingredients: String,
  steps: String,
  funFact: String,
  imageUrl: String,
  category: String,
  likeCount: Number
}));

// GET all recipes
router.get('/', async (req, res) => {
  const recipes = await Recipe.find();
  res.json(recipes);
});

// POST a new recipe
router.post('/', async (req, res) => {
  const newRecipe = new Recipe(req.body);
  await newRecipe.save();
  res.status(201).json({ message: 'Recipe added', data: newRecipe });
});

module.exports = router;
