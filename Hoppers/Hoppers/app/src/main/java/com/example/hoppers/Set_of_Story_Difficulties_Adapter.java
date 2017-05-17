package com.example.hoppers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Peter on 01.05.2017.
 */

public class Set_of_Story_Difficulties_Adapter extends BaseAdapter {


    private List<Story_Level_Set_Class> list;
    private Context context;
    private TextView amountoffrogs;
    private TextView levelscompleted;
    public int max = 0;

    public Set_of_Story_Difficulties_Adapter(Context context, List<Story_Level_Set_Class> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.item_storylevels_list,null);


        amountoffrogs = (TextView) v.findViewById(R.id.amountoffrogs);
        levelscompleted = (TextView) v.findViewById(R.id.levelscompleted);
        if (max!=0) amountoffrogs.setMinHeight(max);
        amountoffrogs.setText("Difficulty : "+String.valueOf(list.get(position).getAmountoffrogs())+" Frogs");
        levelscompleted.setText("Completed levels : "+String.valueOf(list.get(position).getLevels_completed())+
                "/"+String.valueOf(list.get(position).getTotallevels()));

        v.setTag(list.get(position));
        return v;
    }
}
