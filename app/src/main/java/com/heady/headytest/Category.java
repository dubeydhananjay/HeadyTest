package com.heady.headytest;

public class Category {
   private int id;
  private  String category_Name;
    private String child_category;

    public String getTrimmed_category() {
        return trimmed_category;
    }

    public void setTrimmed_category(String trimmed_category) {
        this.trimmed_category = trimmed_category;
    }

    private String trimmed_category;
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCategory_Name()
    {
        return category_Name;
    }

    public void setCategory_Name(String category_Name)
    {
        this.category_Name = category_Name;
    }

    public String getChild_Category()
    {
        return child_category;
    }

    public void setChild_Category(String child_category)
    {
        this.child_category = child_category;
    }
}
