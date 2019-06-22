package com.heady.headytest;

public class ProductVariants {
    private int category_id,prod_id,variant_id,variant_price;
    private String variant_color,variant_size;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public int getVariant_price() {
        return variant_price;
    }

    public void setVariant_price(int variant_price) {
        this.variant_price = variant_price;
    }

    public String getVariant_color() {
        return variant_color;
    }

    public void setVariant_color(String variant_color) {
        this.variant_color = variant_color;
    }

    public String getVariant_size() {
        return variant_size;
    }

    public void setVariant_size(String variant_size) {
        this.variant_size = variant_size;
    }
}
