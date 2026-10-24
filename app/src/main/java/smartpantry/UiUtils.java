package za.co.smartpantry;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UiUtils {

    public static String getIngredientEmoji(String name) {
        if (name == null) return "📦";
        String lower = name.toLowerCase(Locale.ROOT).trim();
        if (lower.contains("tomato") || lower.contains("ketchup")) return "🍅";
        if (lower.contains("apple")) return "🍎";
        if (lower.contains("banana")) return "🍌";
        if (lower.contains("egg")) return "🥚";
        if (lower.contains("milk") || lower.contains("cream") || lower.contains("butter")) return "🥛";
        if (lower.contains("cheese")) return "🧀";
        if (lower.contains("bread") || lower.contains("flour") || lower.contains("toast")) return "🍞";
        if (lower.contains("meat") || lower.contains("beef") || lower.contains("steak") || lower.contains("pork")) return "🥩";
        if (lower.contains("chicken") || lower.contains("poultry")) return "🍗";
        if (lower.contains("fish") || lower.contains("salmon") || lower.contains("tuna")) return "🐟";
        if (lower.contains("carrot")) return "🥕";
        if (lower.contains("onion") || lower.contains("garlic")) return "🧅";
        if (lower.contains("potato")) return "🥔";
        if (lower.contains("rice")) return "🌾";
        if (lower.contains("pasta") || lower.contains("noodle") || lower.contains("spaghetti")) return "🍝";
        if (lower.contains("salt") || lower.contains("pepper") || lower.contains("spice")) return "🧂";
        if (lower.contains("oil") || lower.contains("vinegar")) return "🍾";
        if (lower.contains("lemon") || lower.contains("lime")) return "🍋";
        if (lower.contains("water") || lower.contains("juice")) return "🧃";
        return "📦";
    }

    public static String getRecipeEmoji(String recipeName) {
        if (recipeName == null) return "🍲";
        String lower = recipeName.toLowerCase(Locale.ROOT).trim();
        if (lower.contains("soup")) return "🥣";
        if (lower.contains("salad")) return "🥗";
        if (lower.contains("pizza")) return "🍕";
        if (lower.contains("pasta") || lower.contains("spaghetti") || lower.contains("mac")) return "🍝";
        if (lower.contains("burger")) return "🍔";
        if (lower.contains("sandwich") || lower.contains("toast")) return "🥪";
        if (lower.contains("curry")) return "🍛";
        if (lower.contains("cake") || lower.contains("pie")) return "🍰";
        if (lower.contains("omelet") || lower.contains("egg")) return "🍳";
        return "🍲";
    }

    public static class ExpiryStatus {
        public final String text;
        public final int backgroundColor;
        public final int textColor;

        public ExpiryStatus(String text, int backgroundColor, int textColor) {
            this.text = text;
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
        }
    }

    public static ExpiryStatus getExpiryStatus(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = sdf.parse(expiryDate.trim());
            if (date == null) return null;

            Date today = new Date();
            long diffMs = date.getTime() - today.getTime();
            long days = diffMs / (1000 * 60 * 60 * 24);

            if (diffMs < 0 && Math.abs(days) >= 1) {
                return new ExpiryStatus("Expired (" + expiryDate + ")", Color.parseColor("#FFEBEE"), Color.parseColor("#C62828"));
            } else if (days <= 3) {
                return new ExpiryStatus("Expires soon (" + expiryDate + ")", Color.parseColor("#FFF3E0"), Color.parseColor("#E65100"));
            } else {
                return new ExpiryStatus("Expires: " + expiryDate, Color.parseColor("#E8F5E9"), Color.parseColor("#2E7D32"));
            }
        } catch (Exception e) {
            return new ExpiryStatus("Expires: " + expiryDate, Color.parseColor("#F5F5F5"), Color.parseColor("#616161"));
        }
    }

    public static LinearLayout createCard(Context context) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_card);
        card.setPadding(32, 28, 32, 28);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        return card;
    }

    public static void stylePrimaryButton(Button button) {
        button.setBackgroundResource(R.drawable.bg_button_primary);
        button.setTextColor(Color.WHITE);
        button.setTextSize(15);
        button.setTypeface(null, Typeface.BOLD);
        button.setPadding(32, 20, 32, 20);
    }

    public static void styleSecondaryButton(Button button) {
        button.setBackgroundResource(R.drawable.bg_button_secondary);
        button.setTextColor(Color.parseColor("#1B5E20"));
        button.setTextSize(14);
        button.setTypeface(null, Typeface.BOLD);
        button.setPadding(28, 18, 28, 18);
    }

    public static void styleEditButton(Button button) {
        button.setBackgroundResource(R.drawable.bg_button_edit);
        button.setTextColor(Color.parseColor("#1565C0"));
        button.setTextSize(13);
        button.setTypeface(null, Typeface.BOLD);
        button.setPadding(20, 10, 20, 10);
    }

    public static void styleDeleteButton(Button button) {
        button.setBackgroundResource(R.drawable.bg_button_danger);
        button.setTextColor(Color.parseColor("#C62828"));
        button.setTextSize(13);
        button.setTypeface(null, Typeface.BOLD);
        button.setPadding(20, 10, 20, 10);
    }

    public static void styleEditText(EditText editText) {
        editText.setBackgroundResource(R.drawable.bg_input_field);
        editText.setPadding(28, 24, 28, 24);
        editText.setTextSize(15);
        editText.setTextColor(Color.parseColor("#1A1C1E"));
        editText.setHintTextColor(Color.parseColor("#9E9E9E"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 20);
        editText.setLayoutParams(params);
    }
}
