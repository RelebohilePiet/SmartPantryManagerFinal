package za.co.smartpantry;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatch {
    public final Recipe recipe;
    public final List<String> presentIngredients = new ArrayList<>();
    public final List<String> missingIngredients = new ArrayList<>();

    public RecipeMatch(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getTotalCount() {
        return presentIngredients.size() + missingIngredients.size();
    }

    public int getMatchedCount() {
        return presentIngredients.size();
    }

    public boolean isFullMatch() {
        return missingIngredients.isEmpty();
    }

    public double getMatchRatio() {
        int total = getTotalCount();
        return total == 0 ? 0 : (double) getMatchedCount() / total;
    }

    public String getMissingString() {
        if (missingIngredients.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < missingIngredients.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(missingIngredients.get(i));
        }
        return sb.toString();
    }
}
