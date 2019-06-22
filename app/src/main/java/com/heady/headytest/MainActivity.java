package com.heady.headytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String URL_DATA = "https://stark-spire-93433.herokuapp.com/json";
    DBHelper dbHelper;
    ArrayList<String> category_names, child_categories, cacheName;
    GridView mainGridView;
    MainGridAdapter mainGridAdapter;
    boolean flag;
    int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        category_names = new ArrayList<>();
        cacheName = new ArrayList<>();
        mainGridView = findViewById(R.id.mainGridView);
        flag = false;

        try {
            dbHelper = new DBHelper(this);
            dbHelper.createDatabase();
        } catch (Exception e) {
            e.getStackTrace();
        }
        loadRequest();

        mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                c++;
                flag = false;
                TextView text = v.findViewById(R.id.text);
                child_categories = dbHelper.getSubCategoryOrProduct(text.getText().toString());
                //  Log.e("childcat--",dbHelper.getCatIdByName(text.getText().toString().replace(" ","").trim())+"");
                if(c < 4) {
                    mainGridAdapter = new MainGridAdapter(MainActivity.this, child_categories);
                    mainGridView.setAdapter(mainGridAdapter);
                }
                else {
                    c = 0;
                    ProductVariants  pv = dbHelper.getProductVariants(dbHelper.getProdId(text.getText().toString()));
                    Log.e("pv**",pv+" "+pv.getVariant_color());
                    Intent i = new Intent(MainActivity.this,ProductActivity.class);
                    i.putExtra("color",pv.getVariant_color());
                    i.putExtra("size",pv.getVariant_size());
                    i.putExtra("price",pv.getVariant_price());
                    i.putExtra("prodname",text.getText().toString());
                    startActivity(i);

                }
                cacheName = child_categories;
            }
        });
    }

    void loadRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int cat_id = 0, prod_id = 0, variant_id = 0;
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("categories");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                cat_id = jsonArray.getJSONObject(i).getInt("id");
                                insertCategory(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("child_categories"), cat_id);

                                JSONArray productArray = jsonArray.getJSONObject(i).getJSONArray("products");
                                for (int j = 0; j < productArray.length(); j++) {
                                    prod_id = productArray.getJSONObject(j).getInt("id");
                                    insertProduct(cat_id,
                                            prod_id,
                                            productArray.getJSONObject(j).getString("name"),
                                            productArray.getJSONObject(j).getString("date_added"));

                                    JSONArray variantArray = productArray.getJSONObject(j).getJSONArray("variants");
                                    for (int k = 0; k < variantArray.length(); k++) {
                                        variant_id = variantArray.getJSONObject(k).getInt("id");
                                        insertProductVariants(cat_id, prod_id, variant_id,
                                                variantArray.getJSONObject(k).getInt("price"),
                                                variantArray.getJSONObject(k).getString("color"),
                                                variantArray.getJSONObject(k).getString("size"));
                                    }

                                    JSONObject taxArray = productArray.getJSONObject(j).getJSONObject("tax");
                                    insertProductTax(taxArray.getInt("value"), prod_id, taxArray.getString("name"));

                                }
                            }

                            Log.e("products***", dbHelper.getProductName() + "#########");
                            Log.e("category***", dbHelper.getCategoryNames() + "#######");
                            Log.e("prod_variants***", dbHelper.getProductVariantColor() + "#######");
                            Log.e("prod_tax***", dbHelper.getProductTaxName() + "#######");

                            mainGridAdapter = new MainGridAdapter(MainActivity.this, dbHelper.getMainCategory());
                            mainGridView.setAdapter(mainGridAdapter);
                            cacheName = dbHelper.getMainCategory();
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getStackTrace();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void insertCategory(String name, String child_cat, int id) {
        Category category = new Category();
        category.setId(id);
        String str = child_cat.substring(1, child_cat.length() - 1);
        category.setChild_Category(str);
        category.setCategory_Name(name);
        category.setTrimmed_category(name.replace(" ", "").trim());
        dbHelper.insertCategory(category);

    }

    void insertProduct(int category_id, int prod_id, String prod_name, String prod_date) {
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProd_name(prod_name);
        productDetails.setProd_id(prod_id);
        productDetails.setProd_date(prod_date);
        productDetails.setCategory_id(category_id);
        productDetails.setTrimmed_products(prod_name.replace(" ", "").trim());
        dbHelper.insertProductDetails(productDetails);
    }

    void insertProductVariants(int category_id, int prod_id, int variant_id, int variant_price, String variant_color, String variant_size) {
        ProductVariants productVariants = new ProductVariants();
        productVariants.setCategory_id(category_id);
        productVariants.setProd_id(prod_id);
        productVariants.setVariant_color(variant_color);
        productVariants.setVariant_id(variant_id);
        productVariants.setVariant_price(variant_price);
        productVariants.setVariant_size(variant_size);
        dbHelper.insertProductVariants(productVariants);
    }

    void insertProductTax(int tax_value, int prod_id, String tax_name) {
        ProductTax productTax = new ProductTax();
        productTax.setTax_name(tax_name);
        productTax.setTax_value(tax_value);
        productTax.setProd_id(prod_id);
        dbHelper.insertProductTax(productTax);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (!flag) {
            flag = true;
            dbHelper.product = false;
            c = 0;
            mainGridAdapter = new MainGridAdapter(MainActivity.this, dbHelper.getMainCategory());
            mainGridView.setAdapter(mainGridAdapter);
        } else
            super.onBackPressed();
    }
}
