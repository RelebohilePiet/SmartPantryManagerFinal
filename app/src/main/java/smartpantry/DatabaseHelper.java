package za.co.smartpantry;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, ingredients TEXT NOT NULL, steps TEXT NOT NULL)");
        seedRecipes(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Migration statements when database schema changes.
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
          {"Tomato Egg Scramble","egg,tomato,salt","1. Beat the eggs. 2. Chop tomato. 3. Cook together in a pan and season."},
          {"Banana Pancakes","banana,egg,flour","1. Mash banana. 2. Mix with egg and flour. 3. Fry small pancakes."},
          {"Simple Fried Rice","rice,egg,carrot,oil","1. Dice carrot. 2. Cook carrot in oil. 3. Add cooked rice and egg; stir-fry."},
          {"Garlic Pasta","pasta,garlic,oil,salt","1. Boil pasta. 2. Gently cook chopped garlic in oil. 3. Toss pasta through garlic oil."},
          {"Cheese Omelette","egg,cheese,salt","1. Beat eggs with salt. 2. Cook in a pan. 3. Add cheese and fold."},
          {"Potato Hash","potato,onion,oil,salt","1. Dice potato and onion. 2. Fry until tender and golden. 3. Season."},
          {"Apple Oat Bowl","apple,oats,milk","1. Chop apple. 2. Warm oats with milk. 3. Top with apple."},
          {"Peanut Banana Toast","bread,peanut butter,banana","1. Toast bread. 2. Spread peanut butter. 3. Add sliced banana."},
          {"Tuna Pasta","pasta,tuna,mayonnaise","1. Boil and cool pasta. 2. Drain tuna. 3. Mix pasta, tuna and mayonnaise."},
          {"Bean Salad","beans,tomato,onion","1. Drain cooked beans. 2. Chop tomato and onion. 3. Mix together."},
          {"Carrot Soup","carrot,onion,stock","1. Chop carrot and onion. 2. Simmer in stock until soft. 3. Blend carefully."},
          {"Cheese Sandwich","bread,cheese,butter","1. Butter bread. 2. Add cheese. 3. Toast until melted."},
          {"Chicken Rice Bowl","chicken,rice,carrot,oil","1. Cook chicken thoroughly. 2. Cook diced carrot. 3. Serve with cooked rice."},
          {"Spinach Omelette","egg,spinach,cheese","1. Wash and chop spinach. 2. Cook spinach briefly. 3. Add beaten egg and cheese."},
          {"Tomato Pasta","pasta,tomato,garlic,oil","1. Boil pasta. 2. Cook tomato and garlic in oil. 3. Combine and serve."},
          {"Yoghurt Fruit Bowl","yoghurt,banana,apple","1. Slice fruit. 2. Add to yoghurt. 3. Stir and serve chilled."},
          {"Avocado Toast","bread,avocado,salt","1. Toast bread. 2. Mash avocado with salt. 3. Spread over toast."},
          {"Corn Fritters","corn,egg,flour,oil","1. Mix corn, egg and flour. 2. Spoon into a lightly oiled pan. 3. Cook both sides."},
          {"Lentil Stew","lentils,carrot,onion,stock","1. Rinse lentils. 2. Simmer with chopped vegetables and stock until tender."},
          {"Honey Oats","oats,milk,honey","1. Warm oats and milk. 2. Stir until creamy. 3. Add honey before serving."}
        };
        for (String[] r : data) {
            ContentValues cv = new ContentValues();
            cv.put("name",r[0]); cv.put("ingredients",r[1]); cv.put("steps",r[2]);
            db.insert("recipes",null,cv);
        }
    }

    public long addPantryItem(String name, double qty, String unit, String expiry) {
        ContentValues v = new ContentValues();
        v.put("name", normalize(name)); v.put("quantity",qty); v.put("unit",unit); v.put("expiry",expiry);
        return getWritableDatabase().insert("pantry",null,v);
    }

    public int updatePantryItem(long id, String name, double qty, String unit, String expiry) {
        ContentValues v = new ContentValues();
        v.put("name",normalize(name)); v.put("quantity",qty); v.put("unit",unit); v.put("expiry",expiry);
        return getWritableDatabase().update("pantry",v,"id=?",new String[]{String.valueOf(id)});
    }

    public int deletePantryItem(long id) {
        return getWritableDatabase().delete("pantry","id=?",new String[]{String.valueOf(id)});
    }

    public List<PantryItem> getPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        try (Cursor c=getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry FROM pantry ORDER BY name",null)) {
            while(c.moveToNext()) list.add(new PantryItem(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4)));
        }
        return list;
    }

    public List<Recipe> getRecipes() {
        List<Recipe> list = new ArrayList<>();
        try (Cursor c=getReadableDatabase().rawQuery("SELECT name,ingredients,steps FROM recipes ORDER BY name",null)) {
            while(c.moveToNext()) list.add(new Recipe(c.getString(0),c.getString(1),c.getString(2)));
        }
        return list;
    }

    public List<RecipeMatch> getSmartRecipeMatches() {
        List<PantryItem> pantry = getPantryItems();
        List<Recipe> recipes = getRecipes();
        List<RecipeMatch> matches = new ArrayList<>();

        for (Recipe recipe : recipes) {
            RecipeMatch match = new RecipeMatch(recipe);
            for (String required : recipe.ingredientList()) {
                boolean foundInPantry = false;
                for (PantryItem item : pantry) {
                    if (item.quantity > 0 && matchesIngredient(item.name, required)) {
                        foundInPantry = true;
                        break;
                    }
                }
                if (foundInPantry) {
                    match.presentIngredients.add(required.trim());
                } else {
                    match.missingIngredients.add(required.trim());
                }
            }

            if (match.getMatchedCount() > 0) {
                matches.add(match);
            }
        }

        Collections.sort(matches, (a, b) -> {
            if (a.isFullMatch() != b.isFullMatch()) {
                return a.isFullMatch() ? -1 : 1;
            }
            int cmpRatio = Double.compare(b.getMatchRatio(), a.getMatchRatio());
            if (cmpRatio != 0) return cmpRatio;

            int cmpCount = Integer.compare(b.getMatchedCount(), a.getMatchedCount());
            if (cmpCount != 0) return cmpCount;

            return a.recipe.name.compareToIgnoreCase(b.recipe.name);
        });

        return matches;
    }

    public List<Recipe> getStrictMatches() {
        List<Recipe> strict = new ArrayList<>();
        for (RecipeMatch m : getSmartRecipeMatches()) {
            if (m.isFullMatch()) {
                strict.add(m.recipe);
            }
        }
        return strict;
    }

    public static boolean matchesIngredient(String pantryName, String requiredName) {
        if (pantryName == null || requiredName == null) return false;
        String p = normalize(pantryName);
        String r = normalize(requiredName);
        if (p.isEmpty() || r.isEmpty()) return false;

        if (p.equals(r)) return true;
        if (p.contains(r) || r.contains(p)) return true;

        String[] pWords = p.split("\\s+");
        String[] rWords = r.split("\\s+");
        for (String pw : pWords) {
            for (String rw : rWords) {
                String pwNorm = normalize(pw);
                String rwNorm = normalize(rw);
                if (pwNorm.length() >= 3 && rwNorm.length() >= 3 && (pwNorm.equals(rwNorm) || pwNorm.contains(rwNorm) || rwNorm.contains(pwNorm))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String normalize(String input) {
        String s = input == null ? "" : input.trim().toLowerCase(Locale.ROOT);
        if (s.endsWith("ies") && s.length()>3) s=s.substring(0,s.length()-3)+"y";
        else if (s.endsWith("s") && !s.endsWith("ss") && s.length()>1) s=s.substring(0,s.length()-1);
        return s;
    }
}
