package com.heady.headytest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    private static String DB_NAME = "heady.db";
    private SQLiteDatabase sDB;
    private final Context context;
    public ArrayList<String> category_names;
    File dbFile;
    public boolean product;

    //constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);

        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;
        //DB_PATH = this.context.getDatabasePath(DB_NAME).getAbsolutePath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void createDatabase() throws IOException {
        boolean mDataBaseExist = checkDataBase();

        if (!mDataBaseExist) {
            this.getReadableDatabase();

            try {
                this.close();
                //copy the database from assets
                copyDataBase();
                Log.e("***DB", "Created***");
            } catch (Exception e) {
                throw new Error("Error copying db");
            }

        }
    }

    private void copyDataBase() throws IOException {

        OutputStream dbOutputStream = new FileOutputStream(DB_PATH + DB_NAME);
        InputStream dbInputStream;
        byte[] buffer = new byte[1024];
        int length;

        dbInputStream = context.getAssets().open(DB_NAME);
        try {
            while ((length = dbInputStream.read(buffer)) > 0) {
                dbOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            dbInputStream.close();
            dbOutputStream.flush();
            dbOutputStream.close();

        }


    }

    private boolean checkDataBase() {
        dbFile = new File(DB_PATH + DB_NAME);
        dbFile.delete();
        return dbFile.exists();
    }

    public boolean openDB() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        sDB = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        return sDB != null;
    }

    @Override
    public synchronized void close() {
        if (sDB != null)
            sDB.close();
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {

            try {
                copyDataBase();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Insert & Get Categories
    public boolean insertCategory(Category category) {

        try {
            openDB();
            sDB = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("category_id", category.getId());
            cv.put("name", category.getCategory_Name());
            cv.put("child_category_id", category.getChild_Category());
            cv.put("trimmed_category", category.getTrimmed_category());

            sDB.insert("Category", null, cv);
        } catch (Exception e) {

        } finally {
            close();
        }

        return true;
    }

    public ArrayList<String> getCategoryNames() {
        return getCommon("select name from Category");
    }

    public ArrayList<Integer> getCategoryID() {
        ArrayList<Integer> names = new ArrayList<>();
        Cursor c = null;
        try {
            sDB = this.getReadableDatabase();
            c = sDB.rawQuery("select category_id from Category", null);

            while (c.moveToNext()) {
                names.add(c.getInt(0));
            }
        } catch (Exception e) {

        } finally {
            c.close();
            close();
        }
        return names;
    }

    public ArrayList<String> getChildCategories() {
        return getCommon("select child_category_id from Category");

    }

    //Insert and Get ProductDetails
    public boolean insertProductDetails(ProductDetails productDetails) {
        try {
            openDB();
            sDB = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("prod_id", productDetails.getProd_id());
            cv.put("category_id", productDetails.getCategory_id());
            cv.put("prod_taxId", productDetails.getProd_taxId());
            cv.put("prod_name", productDetails.getProd_name());
            cv.put("prod_date", productDetails.getProd_date());
            cv.put("trimmed_products", productDetails.getTrimmed_products());

            sDB.insert("ProductDetails", null, cv);
        } catch (Exception e) {

        } finally {

            close();
        }
        return true;
    }

    public ArrayList<String> getProductName() {
        return getCommon("select prod_name from ProductDetails");

    }

    //Insert & get product variants

    public boolean insertProductVariants(ProductVariants productVariants) {
        try {
            openDB();
            sDB = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("prod_id", productVariants.getProd_id());
            cv.put("category_id", productVariants.getCategory_id());
            cv.put("variant_id", productVariants.getVariant_id());
            cv.put("variant_price", productVariants.getVariant_price());
            cv.put("variant_color", productVariants.getVariant_color());
            cv.put("variant_size", productVariants.getVariant_size());

            sDB.insert("ProductVariants", null, cv);
        } catch (Exception e) {

        } finally {

            close();
        }
        return true;
    }

    public ArrayList<String> getProductVariantColor() {

        return getCommon("select variant_color from ProductVariants");

    }

    //Insert & get product tax

    public boolean insertProductTax(ProductTax productTax) {
        try {
            openDB();
            sDB = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("tax_value", productTax.getTax_value());
            cv.put("tax_name", productTax.getTax_name());
            cv.put("prod_id", productTax.getProd_id());

            sDB.insert("ProductTax", null, cv);
        } catch (Exception e) {

        } finally {

            close();
        }
        return true;
    }

    public ArrayList<String> getProductTaxName() {

        return getCommon("select tax_name from ProductTax");

    }

    public int getTaxId(String tax_name) {
        int id = 0;
        Cursor c = null;
        try {
            sDB = this.getReadableDatabase();
            c = sDB.rawQuery("select tax_name from ProductTax", null);

            while (c.moveToNext()) {
                id = c.getInt(0);
            }
            c.close();
        } catch (Exception e) {

        } finally {
            c.close();
            close();
        }
        return id;
    }

    public ArrayList<String> getMainCategory() {
        ArrayList<String> names = new ArrayList<>();

        names = getCategoryNames();

        for (int i = 0; i < getChildCategories().size(); i++) {
            String[] str = getChildCategories().get(i).split(",");
            for (int j = 0; j < str.length; j++) {

                if (!str[j].equals("")) {
                    if (names.contains(getCategoryById(Integer.parseInt(str[j])))) {
                        names.remove(getCategoryById(Integer.parseInt(str[j])));

                    }
                }
            }
        }

        return names;
    }

    //
    public String getCategoryById(int id) {
        String name = "";
        Cursor c = null;
        try {
            sDB = this.getReadableDatabase();
            c = sDB.rawQuery("select name from Category where category_id =" + id, null);

            while (c.moveToNext()) {
                name = c.getString(0);
            }
        } catch (Exception e) {

        } finally {
            c.close();
            close();
        }
        return name;

    }

    ArrayList<String> getCommon(String query) {
        ArrayList<String> names = new ArrayList<>();
        Cursor c = null;
        try {
            sDB = this.getReadableDatabase();
            c = sDB.rawQuery(query, null);

            while (c.moveToNext()) {
                names.add(c.getString(0));
            }
        } catch (Exception e) {

        } finally {
            c.close();
            close();
        }
        return names;
    }

    public ArrayList<String> getSubCategoryOrProduct(String name) {
        String str = "";
        ArrayList<String> names = new ArrayList<>();
        String[] strings;
        str = getChildCatByCatName(name);
        if (!str.equals("")) {
            strings = str.split(",");
            for (int i = 0; i < strings.length; i++) {
                names.add(getCategoryById(Integer.parseInt(strings[i])));
            }
        }
        else {

            names = getProductByCatId(getCatIdByName(name));
        }


        return names;
    }

    public int getCatIdByName(String name) {
        int a = 0;
        sDB = this.getReadableDatabase();
        Cursor c = sDB.rawQuery("select category_id from Category where trimmed_category = '" + name + "'", null);
        while (c.moveToNext()) {
            a = c.getInt(0);
        }
        c.close();
        close();
        return a;
    }

    public ArrayList<String> getProductByCatId(int id) {
        ArrayList<String> prod_names = new ArrayList<>();
        sDB = this.getReadableDatabase();
        Cursor c = sDB.rawQuery("select prod_name from ProductDetails where category_id = " + id, null);
        while (c.moveToNext()) {
            prod_names.add(c.getString(0));
        }
        c.close();
        close();
        return prod_names;
    }

    public String getChildCatByCatName(String name) {
        String str = "";
        Cursor c = null;
        try {
            sDB = this.getReadableDatabase();
            c = sDB.rawQuery("select child_category_id from Category where name = '" + name + "'", null);

            while (c.moveToNext()) {
                str = c.getString(0);
            }
        } catch (Exception e) {
        } finally {
            c.close();
            close();
        }
        return str;
    }

    public ProductVariants getProductVariants(int prod_id)
    {
        ProductVariants productVariants = new ProductVariants();
        sDB = this.getReadableDatabase();
        Cursor c = sDB.rawQuery("select variant_color,variant_size,variant_price from ProductVariants where prod_id = "+prod_id,null);
        while (c.moveToNext())
        {
            productVariants.setVariant_color(c.getString(0));
            productVariants.setVariant_size(c.getString(1));
            productVariants.setVariant_price(c.getInt(2));
        }
        Log.e("cursor***",c+"**"+productVariants.getVariant_color());
        return productVariants;

    }

    public int getProdId(String name)
    {
        int s = 0;
        sDB = this.getReadableDatabase();
        Cursor c = sDB.rawQuery("select prod_id from ProductDetails where prod_name = '"+name+"'",null);
        while (c.moveToNext())
        {
            s = c.getInt(0);
        }
        Log.e("prodid***",s+"");
        c.close();
        close();
        return s;

    }

}
