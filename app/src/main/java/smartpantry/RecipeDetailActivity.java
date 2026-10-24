package za.co.smartpantry;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.*;
import java.util.List;

public class RecipeDetailActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        String selected = getIntent().getStringExtra("name");
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe found = null;
        for (Recipe r : db.getRecipes()) {
            if (r.name.equals(selected)) {
                found = r;
                break;
            }
        }

        List<PantryItem> pantryItems = db.getPantryItems();

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.parseColor("#F4F6F8"));

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Header Banner
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setBackgroundResource(R.drawable.bg_header);
        header.setPadding(32, 48, 32, 40);

        TextView icon = new TextView(this);
        icon.setText(UiUtils.getRecipeEmoji(found == null ? "" : found.name));
        icon.setTextSize(40);

        TextView title = new TextView(this);
        title.setText(found == null ? "Recipe Details" : found.name);
        title.setTextSize(24);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.WHITE);
        title.setPadding(0, 8, 0, 0);

        header.addView(icon);
        header.addView(title);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(24, 24, 24, 24);

        // Ingredients Card
        LinearLayout ingredientsCard = UiUtils.createCard(this);

        TextView ingTitle = new TextView(this);
        ingTitle.setText("🛒 Ingredients Required");
        ingTitle.setTextSize(17);
        ingTitle.setTypeface(null, Typeface.BOLD);
        ingTitle.setTextColor(Color.parseColor("#1B5E20"));

        StringBuilder ingSb = new StringBuilder();
        if (found == null) {
            ingSb.append("Recipe not found");
        } else {
            for (String req : found.ingredientList()) {
                String reqTrimmed = req.trim();
                boolean present = false;
                for (PantryItem item : pantryItems) {
                    if (item.quantity > 0 && DatabaseHelper.matchesIngredient(item.name, reqTrimmed)) {
                        present = true;
                        break;
                    }
                }
                if (ingSb.length() > 0) ingSb.append("\n");
                if (present) {
                    ingSb.append("• ").append(reqTrimmed).append("  (✓ In Pantry)");
                } else {
                    ingSb.append("• ").append(reqTrimmed).append("  (⚠️ Missing)");
                }
            }
        }

        TextView ingList = new TextView(this);
        ingList.setText(ingSb.toString());
        ingList.setTextSize(15);
        ingList.setLineSpacing(6, 1.2f);
        ingList.setTextColor(Color.parseColor("#37474F"));
        ingList.setPadding(0, 12, 0, 0);

        ingredientsCard.addView(ingTitle);
        ingredientsCard.addView(ingList);

        // Preparation Steps Card
        LinearLayout stepsCard = UiUtils.createCard(this);

        TextView stepsTitle = new TextView(this);
        stepsTitle.setText("👩‍🍳 Preparation Steps");
        stepsTitle.setTextSize(17);
        stepsTitle.setTypeface(null, Typeface.BOLD);
        stepsTitle.setTextColor(Color.parseColor("#1B5E20"));

        TextView stepsList = new TextView(this);
        String stepText = (found == null || found.steps == null || found.steps.isEmpty()) ? "No steps available." : found.steps;
        stepsList.setText(stepText);
        stepsList.setTextSize(15);
        stepsList.setLineSpacing(6, 1.2f);
        stepsList.setTextColor(Color.parseColor("#37474F"));
        stepsList.setPadding(0, 12, 0, 0);

        stepsCard.addView(stepsTitle);
        stepsCard.addView(stepsList);

        // Back Button
        Button back = new Button(this);
        back.setText("← Back to Recipes");
        UiUtils.styleSecondaryButton(back);
        back.setOnClickListener(v -> finish());

        content.addView(ingredientsCard);
        content.addView(stepsCard);
        content.addView(back);

        mainLayout.addView(header);
        mainLayout.addView(content);

        scrollView.addView(mainLayout);
        setContentView(scrollView);
    }
}
