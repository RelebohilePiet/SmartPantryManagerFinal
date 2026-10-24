package za.co.smartpantry;
import java.util.Arrays;
import java.util.List;

public class Recipe {
    public String name, ingredients, steps;
    public Recipe(String name, String ingredients, String steps) {
        this.name=name; this.ingredients=ingredients; this.steps=steps;
    }
    public List<String> ingredientList() { return Arrays.asList(ingredients.split(",")); }
}
