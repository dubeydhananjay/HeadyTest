package com.heady.headytest;

public class ProductTax {
    private int prod_taxId;
    private int tax_value;

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    private int prod_id;
    private String tax_name;
    public int getProd_taxId() {
        return prod_taxId;
    }

    public void setProd_taxId(int prod_taxId) {
        this.prod_taxId = prod_taxId;
    }

    public int getTax_value() {
        return tax_value;
    }

    public void setTax_value(int tax_value) {
        this.tax_value = tax_value;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }


}
