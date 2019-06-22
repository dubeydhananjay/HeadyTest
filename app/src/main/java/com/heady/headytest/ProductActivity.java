package com.heady.headytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {
    TextView prod_name,color,size,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prod_layout);
        prod_name = findViewById(R.id.prod_name);
        color = findViewById(R.id.color);
        size = findViewById(R.id.size);
        price = findViewById(R.id.price);


        Bundle extras = getIntent().getExtras();
        String color1 = extras.getString("color");
        String size1 = extras.getString("size");
        int price1 = extras.getInt("price");
        String prodname = extras.getString("prodname");

        prod_name.setText(prodname);
        color.setText(color1);
        size.setText(size1);
        price.setText(price1+"");
    }
}
