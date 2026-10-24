package za.co.smartpantry;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends BaseAdapter {
    private final Context context;
    private final List<RecipeMatch> matches;

    public RecipeAdapter(Context c, List<RecipeMatch> m) {
        context = c;
        matches = m;
    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public Object getItem(int p) {
        return matches.get(p);
    }

    @Override
    public long getItemId(int p) {
        return p;
    }

    @Override
    public View getView(int p, View convert, ViewGroup parent) {
        RecipeMatch match = matches.get(p);
        Recipe recipe = match.recipe;

        LinearLayout card = UiUtils.createCard(context);
        card.setOrientation(LinearLayout.VERTICAL);

        // Top Row: Icon + Title + Arrow
        LinearLayout topRow = new LinearLayout(context);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView icon = new TextView(context);
        icon.setText(UiUtils.getRecipeEmoji(recipe.name));
        icon.setTextSize(32);
        icon.setPadding(0, 0, 20, 0);

        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.VERTICAL);

        TextView name = new TextView(context);
        name.setText(recipe.name);
        name.setTextSize(17);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextColor(Color.parseColor("#1A1C1E"));

        infoLayout.addView(name);

        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        infoLayout.setLayoutParams(infoParams);

        TextView arrow = new TextView(context);
        arrow.setText("➔");
        arrow.setTextSize(18);
        arrow.setTextColor(Color.parseColor("#2E7D32"));

        topRow.addView(icon);
        topRow.addView(infoLayout);
        topRow.addView(arrow);

        card.addView(topRow);

        // Match Status Chip
        TextView matchBadge = new TextView(context);
        matchBadge.setTextSize(12);
        matchBadge.setTypeface(null, Typeface.BOLD);

        GradientDrawable badgeShape = new GradientDrawable();
        badgeShape.setShape(GradientDrawable.RECTANGLE);
        badgeShape.setCornerRadius(16);

        if (match.isFullMatch()) {
            matchBadge.setText("🟢 Ready to Cook! (" + match.getMatchedCount() + "/" + match.getTotalCount() + " ingredients)");
            matchBadge.setTextColor(Color.parseColor("#2E7D32"));
            badgeShape.setColor(Color.parseColor("#E8F5E9"));
        } else {
            matchBadge.setText("🟠 " + match.getMatchedCount() + "/" + match.getTotalCount() + " ingredients in pantry (Missing: " + match.getMissingString() + ")");
            matchBadge.setTextColor(Color.parseColor("#E65100"));
            badgeShape.setColor(Color.parseColor("#FFF3E0"));
        }

        matchBadge.setBackground(badgeShape);
        matchBadge.setPadding(16, 6, 16, 6);

        LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        badgeParams.setMargins(0, 12, 0, 0);
        matchBadge.setLayoutParams(badgeParams);

        card.addView(matchBadge);

        // Ingredient list preview
        TextView ingredientsText = new TextView(context);
        StringBuilder ingSb = new StringBuilder("🛒 ");
        for (int i = 0; i < match.presentIngredients.size(); i++) {
            if (i > 0) ingSb.append(", ");
            ingSb.append(match.presentIngredients.get(i)).append(" ✓");
        }
        for (int i = 0; i < match.missingIngredients.size(); i++) {
            if (match.presentIngredients.size() > 0 || i > 0) ingSb.append(", ");
            ingSb.append(match.missingIngredients.get(i)).append(" ✗");
        }
        ingredientsText.setText(ingSb.toString());
        ingredientsText.setTextSize(13);
        ingredientsText.setTextColor(Color.parseColor("#5F6368"));
        ingredientsText.setPadding(0, 8, 0, 0);

        card.addView(ingredientsText);

        return card;
    }
}
