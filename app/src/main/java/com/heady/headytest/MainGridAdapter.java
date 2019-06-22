package com.heady.headytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainGridAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> cat;
    LayoutInflater inflter;
    public MainGridAdapter(Context applicationContext, ArrayList<String> cat) {
        this.context = applicationContext;
        this.cat = cat;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return cat.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.maingrid_layout, null); // inflate the layout
        TextView textView = view.findViewById(R.id.text);
        textView.setText(cat.get(i));
        return view;
    }
}
