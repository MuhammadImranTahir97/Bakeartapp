const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const app = express();

app.use(cors());
app.use(express.json());

mongoose.connect('mongodb://localhost:27017/bakeart');

const Recipe = mongoose.model('Recipe', new mongoose.Schema({
  title: String,
  ingredients: String,
  steps: String,
  funFact: String,
  category: String,
  imageUrl: String
}));

app.post('/recipes', async (req, res) => {
  const recipe = new Recipe(req.body);
  await recipe.save();
  res.send(recipe);
});

app.get('/recipes', async (req, res) => {
  const recipes = await Recipe.find();
  res.send(recipes);
});

app.listen(3000, () => console.log('Server started on port 3000'));
