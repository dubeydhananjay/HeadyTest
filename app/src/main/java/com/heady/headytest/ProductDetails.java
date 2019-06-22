package com.heady.headytest;

public class ProductDetails {
   private int category_id,prod_id,prod_taxId;
   private String prod_name,prod_date,trimmed_products;

    public int getCategory_id() {
        return category_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public int getProd_taxId() {
        return prod_taxId;
    }

    public String getProd_name() {
        return prod_name;
    }

    public String getTrimmed_products() {
        return trimmed_products;
    }

    public void setTrimmed_products(String trimmed_products) {
        this.trimmed_products = trimmed_products;
    }

    public String getProd_date() {
        return prod_date;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public void setProd_taxId(int prod_taxId) {
        this.prod_taxId = prod_taxId;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public void setProd_date(String prod_date) {
        this.prod_date = prod_date;
    }
}
