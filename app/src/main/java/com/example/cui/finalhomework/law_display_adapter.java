package com.example.cui.finalhomework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cui on 2018/5/30.
 */

public class law_display_adapter extends ArrayAdapter<law_display> {
    private int resourceId;
    public law_display_adapter(Context context, int textViewResourseId, List<law_display> object)
    {
        super(context,textViewResourseId,object);
        resourceId=textViewResourseId;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        law_display display1=getItem(position);
        View view;
        if(convertView==null)
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        else
            view=convertView;
        TextView text_name=(TextView)view.findViewById(R.id.lawname_display);
        TextView text_num=(TextView)view.findViewById(R.id.lawnum_displau);
        TextView text_content=(TextView)view.findViewById(R.id.lawcontent_display);
        text_name.setText(display1.getLawname());
        text_num.setText(display1.getLawnum());
        text_content.setText(display1.getLawcontent());
        return view;
    }
}
