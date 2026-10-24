package za.co.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.List;

public class MainActivity extends Activity {
    private DatabaseHelper db;
    private ListView list;
    private View emptyCard;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        db = new DatabaseHelper(this);
        buildUi();
    }

    private void buildUi() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.parseColor("#F4F6F8"));

        // Top Header Banner
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setBackgroundResource(R.drawable.bg_header);
        header.setPadding(32, 48, 32, 40);

        TextView title = new TextView(this);
        title.setText("🥦 Smart Pantry");
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.WHITE);

        TextView subtitle = new TextView(this);
        subtitle.setText("Your ingredients • Less waste • More meals");
        subtitle.setTextSize(13);
        subtitle.setTextColor(Color.parseColor("#E8F5E9"));
        subtitle.setPadding(0, 8, 0, 0);

        header.addView(title);
        header.addView(subtitle);

        // Action Row
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setPadding(24, 20, 24, 12);

        Button add = new Button(this);
        add.setText("➕ Add Ingredient");
        UiUtils.stylePrimaryButton(add);
        LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.3f);
        addParams.setMargins(0, 0, 10, 0);
        add.setLayoutParams(addParams);
        add.setOnClickListener(v -> startActivity(new Intent(this, IngredientActivity.class)));

        Button recipes = new Button(this);
        recipes.setText("👨‍🍳 Recipes");
        UiUtils.styleSecondaryButton(recipes);
        LinearLayout.LayoutParams recParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        recParams.setMargins(0, 0, 10, 0);
        recipes.setLayoutParams(recParams);
        recipes.setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));

        Button settings = new Button(this);
        settings.setText("⚙️");
        UiUtils.styleSecondaryButton(settings);
        LinearLayout.LayoutParams setParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settings.setLayoutParams(setParams);
        settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        actions.addView(add);
        actions.addView(recipes);
        actions.addView(settings);

        // List / Empty state container
        FrameLayout contentArea = new FrameLayout(this);
        contentArea.setPadding(24, 0, 24, 24);

        emptyCard = createEmptyCard();
        list = new ListView(this);
        list.setDivider(null);
        list.setDividerHeight(0);
        list.setSelector(android.R.color.transparent);

        contentArea.addView(emptyCard);
        contentArea.addView(list);

        mainLayout.addView(header);
        mainLayout.addView(actions);
        mainLayout.addView(contentArea, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));

        setContentView(mainLayout);
        refreshPantry();
    }

    private View createEmptyCard() {
        LinearLayout card = UiUtils.createCard(this);
        card.setGravity(Gravity.CENTER);
        card.setPadding(40, 60, 40, 60);

        TextView icon = new TextView(this);
        icon.setText("🛒");
        icon.setTextSize(48);
        icon.setGravity(Gravity.CENTER);

        TextView text = new TextView(this);
        text.setText("Your pantry is empty");
        text.setTextSize(18);
        text.setTypeface(null, Typeface.BOLD);
        text.setTextColor(Color.parseColor("#1A1C1E"));
        text.setGravity(Gravity.CENTER);
        text.setPadding(0, 16, 0, 8);

        TextView subtext = new TextView(this);
        subtext.setText("Tap '+ Add Ingredient' above to get started.");
        subtext.setTextSize(14);
        subtext.setTextColor(Color.parseColor("#757575"));
        subtext.setGravity(Gravity.CENTER);

        card.addView(icon);
        card.addView(text);
        card.addView(subtext);
        return card;
    }

    public void refreshPantry() {
        List<PantryItem> items = db.getPantryItems();
        boolean isEmpty = items.isEmpty();
        emptyCard.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        list.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        list.setAdapter(new PantryAdapter(this, items));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null && list != null) refreshPantry();
    }
}
