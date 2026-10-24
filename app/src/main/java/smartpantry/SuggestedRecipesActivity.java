package za.co.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import java.util.List;

public class SuggestedRecipesActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        render();
    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    private void render() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<RecipeMatch> matches = db.getSmartRecipeMatches();

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.parseColor("#F4F6F8"));

        // Header
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setBackgroundResource(R.drawable.bg_header);
        header.setPadding(32, 48, 32, 40);

        TextView title = new TextView(this);
        title.setText("👨‍🍳 Suggested Recipes");
        title.setTextSize(25);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.WHITE);

        TextView info = new TextView(this);
        info.setText("Matched relative to the ingredients in your pantry");
        info.setTextSize(13);
        info.setTextColor(Color.parseColor("#E8F5E9"));
        info.setPadding(0, 6, 0, 0);

        header.addView(title);
        header.addView(info);

        // Content layout
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(24, 20, 24, 20);

        if (matches.isEmpty()) {
            LinearLayout emptyCard = UiUtils.createCard(this);
            emptyCard.setGravity(Gravity.CENTER);
            emptyCard.setPadding(40, 60, 40, 60);

            TextView emptyIcon = new TextView(this);
            emptyIcon.setText("🍲");
            emptyIcon.setTextSize(48);
            emptyIcon.setGravity(Gravity.CENTER);

            TextView emptyText = new TextView(this);
            emptyText.setText("No recipes match your ingredients yet");
            emptyText.setTextSize(18);
            emptyText.setTypeface(null, Typeface.BOLD);
            emptyText.setTextColor(Color.parseColor("#1A1C1E"));
            emptyText.setGravity(Gravity.CENTER);
            emptyText.setPadding(0, 16, 0, 8);

            TextView emptySubtext = new TextView(this);
            emptySubtext.setText("Add ingredients like eggs, rice, flour, or tomatoes to unlock meal ideas!");
            emptySubtext.setTextSize(14);
            emptySubtext.setTextColor(Color.parseColor("#757575"));
            emptySubtext.setGravity(Gravity.CENTER);

            emptyCard.addView(emptyIcon);
            emptyCard.addView(emptyText);
            emptyCard.addView(emptySubtext);

            content.addView(emptyCard);
        } else {
            ListView list = new ListView(this);
            list.setDivider(null);
            list.setDividerHeight(0);
            list.setSelector(android.R.color.transparent);
            list.setAdapter(new RecipeAdapter(this, matches));
            list.setOnItemClickListener((a, v, pos, id) -> {
                Intent i = new Intent(this, RecipeDetailActivity.class);
                i.putExtra("name", matches.get(pos).recipe.name);
                startActivity(i);
            });
            content.addView(list, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
        }

        Button back = new Button(this);
        back.setText("← Back to Pantry");
        UiUtils.styleSecondaryButton(back);
        back.setOnClickListener(v -> finish());

        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        backParams.setMargins(0, 12, 0, 0);
        back.setLayoutParams(backParams);
        content.addView(back);

        mainLayout.addView(header);
        mainLayout.addView(content, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));

        setContentView(mainLayout);
    }
}
