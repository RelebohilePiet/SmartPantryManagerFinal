package za.co.smartpantry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.List;

public class PantryAdapter extends BaseAdapter {
    private final Context context;
    private final List<PantryItem> items;

    public PantryAdapter(Context c, List<PantryItem> data) {
        context = c;
        items = data;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int p) {
        return items.get(p);
    }

    @Override
    public long getItemId(int p) {
        return items.get(p).id;
    }

    @Override
    public View getView(int p, View convert, ViewGroup parent) {
        PantryItem item = items.get(p);

        LinearLayout card = UiUtils.createCard(context);
        card.setOrientation(LinearLayout.VERTICAL);

        // Top row: Emoji + Info + Actions
        LinearLayout topRow = new LinearLayout(context);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView icon = new TextView(context);
        icon.setText(UiUtils.getIngredientEmoji(item.name));
        icon.setTextSize(28);
        icon.setPadding(0, 0, 20, 0);

        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.VERTICAL);

        TextView name = new TextView(context);
        name.setText(item.name);
        name.setTextSize(17);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextColor(Color.parseColor("#1A1C1E"));

        TextView details = new TextView(context);
        details.setText(item.quantity + " " + item.unit);
        details.setTextSize(14);
        details.setTextColor(Color.parseColor("#5F6368"));

        infoLayout.addView(name);
        infoLayout.addView(details);

        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        infoLayout.setLayoutParams(infoParams);

        // Buttons
        Button edit = new Button(context);
        edit.setText("✏️ Edit");
        UiUtils.styleEditButton(edit);
        edit.setOnClickListener(v -> {
            Intent i = new Intent(context, IngredientActivity.class);
            i.putExtra("id", item.id);
            context.startActivity(i);
        });

        Button delete = new Button(context);
        delete.setText("🗑️");
        UiUtils.styleDeleteButton(delete);
        LinearLayout.LayoutParams delParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        delParams.setMargins(12, 0, 0, 0);
        delete.setLayoutParams(delParams);
        delete.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Delete ingredient?")
                .setMessage("Remove " + item.name + " from your pantry?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> {
                    new DatabaseHelper(context).deletePantryItem(item.id);
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).refreshPantry();
                    }
                }).show());

        topRow.addView(icon);
        topRow.addView(infoLayout);
        topRow.addView(edit);
        topRow.addView(delete);

        card.addView(topRow);

        // Expiry Status Badge if available
        UiUtils.ExpiryStatus status = UiUtils.getExpiryStatus(item.expiry);
        if (status != null) {
            TextView badge = new TextView(context);
            badge.setText(status.text);
            badge.setTextSize(12);
            badge.setTypeface(null, Typeface.BOLD);
            badge.setTextColor(status.textColor);
            badge.setPadding(16, 6, 16, 6);

            // Shape background
            android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
            shape.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
            shape.setColor(status.backgroundColor);
            shape.setCornerRadius(16);
            badge.setBackground(shape);

            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            badgeParams.setMargins(0, 14, 0, 0);
            badge.setLayoutParams(badgeParams);

            card.addView(badge);
        }

        return card;
    }
}
