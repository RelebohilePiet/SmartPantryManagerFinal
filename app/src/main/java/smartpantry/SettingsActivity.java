package za.co.smartpantry;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.*;

public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.parseColor("#F4F6F8"));

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Header Banner
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setBackgroundResource(R.drawable.bg_header);
        header.setPadding(32, 48, 32, 40);

        TextView title = new TextView(this);
        title.setText("⚙️ Settings & Info");
        title.setTextSize(24);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.WHITE);

        header.addView(title);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(24, 24, 24, 24);

        // About Card
        LinearLayout aboutCard = UiUtils.createCard(this);

        TextView aboutTitle = new TextView(this);
        aboutTitle.setText("ℹ️ About Smart Pantry Manager");
        aboutTitle.setTextSize(17);
        aboutTitle.setTypeface(null, Typeface.BOLD);
        aboutTitle.setTextColor(Color.parseColor("#1B5E20"));

        TextView aboutText = new TextView(this);
        aboutText.setText("Smart Pantry Manager helps reduce household food waste by matching ingredients in your pantry with recipe ideas you can cook immediately.\n\n• Track quantities & units\n• Set optional expiration dates\n• Get instant recipe recommendations");
        aboutText.setTextSize(14);
        aboutText.setTextColor(Color.parseColor("#37474F"));
        aboutText.setLineSpacing(4, 1.2f);
        aboutText.setPadding(0, 12, 0, 0);

        aboutCard.addView(aboutTitle);
        aboutCard.addView(aboutText);

        // Preferences Card
        LinearLayout prefCard = UiUtils.createCard(this);

        TextView prefTitle = new TextView(this);
        prefTitle.setText("🔔 Preferences");
        prefTitle.setTextSize(17);
        prefTitle.setTypeface(null, Typeface.BOLD);
        prefTitle.setTextColor(Color.parseColor("#1B5E20"));

        CheckBox alerts = new CheckBox(this);
        alerts.setText("Show expiring-soon alerts on startup");
        alerts.setTextSize(15);
        alerts.setPadding(12, 12, 0, 12);
        alerts.setTextColor(Color.parseColor("#1A1C1E"));
        alerts.setChecked(true);

        prefCard.addView(prefTitle);
        prefCard.addView(alerts);

        // Database Info Card
        LinearLayout dbCard = UiUtils.createCard(this);

        TextView dbTitle = new TextView(this);
        dbTitle.setText("💾 Local Storage");
        dbTitle.setTextSize(17);
        dbTitle.setTypeface(null, Typeface.BOLD);
        dbTitle.setTextColor(Color.parseColor("#1B5E20"));

        TextView dbText = new TextView(this);
        dbText.setText("Database: SQLite (Stored offline locally on this device).");
        dbText.setTextSize(14);
        dbText.setTextColor(Color.parseColor("#5F6368"));
        dbText.setPadding(0, 8, 0, 0);

        dbCard.addView(dbTitle);
        dbCard.addView(dbText);

        // Back Button
        Button back = new Button(this);
        back.setText("← Back");
        UiUtils.styleSecondaryButton(back);
        back.setOnClickListener(v -> finish());

        content.addView(aboutCard);
        content.addView(prefCard);
        content.addView(dbCard);
        content.addView(back);

        mainLayout.addView(header);
        mainLayout.addView(content);

        scrollView.addView(mainLayout);
        setContentView(scrollView);
    }
}
