package za.co.smartpantry;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import java.util.Calendar;
import java.util.Locale;

public class IngredientActivity extends Activity {
    private DatabaseHelper db;
    private EditText name, quantity, unit, expiry;
    private long id = -1;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        db = new DatabaseHelper(this);
        id = getIntent().getLongExtra("id", -1);

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
        title.setText(id == -1 ? "📝 Add Ingredient" : "✏️ Edit Ingredient");
        title.setTextSize(24);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.WHITE);

        header.addView(title);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(24, 24, 24, 24);

        // Form Card
        LinearLayout formCard = UiUtils.createCard(this);

        name = createField(formCard, "🏷️ Ingredient Name", "e.g. Tomato, Milk, Chicken");
        quantity = createField(formCard, "🔢 Quantity", "e.g. 2, 500");
        unit = createField(formCard, "📏 Unit", "e.g. item, g, ml, kg");

        // Expiry Date Field with Calendar Picker
        TextView expiryLabel = new TextView(this);
        expiryLabel.setText("📅 Expiry Date (Optional)");
        expiryLabel.setTextSize(14);
        expiryLabel.setTypeface(null, Typeface.BOLD);
        expiryLabel.setTextColor(Color.parseColor("#37474F"));
        expiryLabel.setPadding(4, 8, 4, 4);

        LinearLayout expiryRow = new LinearLayout(this);
        expiryRow.setOrientation(LinearLayout.HORIZONTAL);
        expiryRow.setGravity(Gravity.CENTER_VERTICAL);

        expiry = new EditText(this);
        expiry.setHint("Tap to pick date...");
        expiry.setFocusable(false);
        expiry.setClickable(true);
        expiry.setCursorVisible(false);
        UiUtils.styleEditText(expiry);
        expiry.setOnClickListener(v -> showDatePicker());

        LinearLayout.LayoutParams expParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        expParams.setMargins(0, 8, 8, 20);
        expiry.setLayoutParams(expParams);

        Button pickDateBtn = new Button(this);
        pickDateBtn.setText("📅 Calendar");
        UiUtils.styleSecondaryButton(pickDateBtn);
        pickDateBtn.setOnClickListener(v -> showDatePicker());
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(0, 0, 8, 20);
        pickDateBtn.setLayoutParams(btnParams);

        Button clearDateBtn = new Button(this);
        clearDateBtn.setText("✖");
        UiUtils.styleDeleteButton(clearDateBtn);
        clearDateBtn.setOnClickListener(v -> expiry.setText(""));
        LinearLayout.LayoutParams clearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        clearParams.setMargins(0, 0, 0, 20);
        clearDateBtn.setLayoutParams(clearParams);

        expiryRow.addView(expiry);
        expiryRow.addView(pickDateBtn);
        expiryRow.addView(clearDateBtn);

        formCard.addView(expiryLabel);
        formCard.addView(expiryRow);

        Button save = new Button(this);
        save.setText("💾 Save Ingredient");
        UiUtils.stylePrimaryButton(save);
        save.setOnClickListener(v -> save());

        Button cancel = new Button(this);
        cancel.setText("Cancel");
        UiUtils.styleSecondaryButton(cancel);
        cancel.setOnClickListener(v -> finish());
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelParams.setMargins(0, 12, 0, 0);
        cancel.setLayoutParams(cancelParams);

        content.addView(formCard);
        content.addView(save);
        content.addView(cancel);

        mainLayout.addView(header);
        mainLayout.addView(content);

        scrollView.addView(mainLayout);
        setContentView(scrollView);

        if (id != -1) loadExisting();
    }

    private EditText createField(LinearLayout container, String labelText, String hintText) {
        TextView label = new TextView(this);
        label.setText(labelText);
        label.setTextSize(14);
        label.setTypeface(null, Typeface.BOLD);
        label.setTextColor(Color.parseColor("#37474F"));
        label.setPadding(4, 8, 4, 4);

        EditText field = new EditText(this);
        field.setHint(hintText);
        UiUtils.styleEditText(field);

        container.addView(label);
        container.addView(field);
        return field;
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        String currentText = expiry.getText().toString().trim();
        if (currentText.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                String[] parts = currentText.split("-");
                int y = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]) - 1;
                int d = Integer.parseInt(parts[2]);
                cal.set(y, m, d);
            } catch (Exception ignored) {
            }
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    expiry.setText(formattedDate);
                    expiry.setError(null);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void loadExisting() {
        for (PantryItem p : db.getPantryItems()) {
            if (p.id == id) {
                name.setText(p.name);
                quantity.setText(String.valueOf(p.quantity));
                unit.setText(p.unit);
                expiry.setText(p.expiry == null ? "" : p.expiry);
                break;
            }
        }
    }

    private void save() {
        String n = name.getText().toString().trim();
        String q = quantity.getText().toString().trim();
        String u = unit.getText().toString().trim();
        String ex = expiry.getText().toString().trim();

        if (n.isEmpty() || q.isEmpty() || u.isEmpty()) {
            Toast.makeText(this, "Name, quantity and unit are required.", Toast.LENGTH_LONG).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(q);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            quantity.setError("Enter a number greater than zero");
            return;
        }

        if (!ex.isEmpty() && !ex.matches("\\d{4}-\\d{2}-\\d{2}")) {
            expiry.setError("Use YYYY-MM-DD format");
            return;
        }

        if (id == -1) {
            db.addPantryItem(n, amount, u, ex);
        } else {
            db.updatePantryItem(id, n, amount, u, ex);
        }

        Toast.makeText(this, "Ingredient saved! 🥦", Toast.LENGTH_SHORT).show();
        finish();
    }
}
