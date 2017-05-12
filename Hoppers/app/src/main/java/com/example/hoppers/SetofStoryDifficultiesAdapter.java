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

public class SetofStoryDifficultiesAdapter extends BaseAdapter {


    private List<StoryLevelSetClass> list;
    private Context context;

    public SetofStoryDifficultiesAdapter(Context context, List<StoryLevelSetClass> list){
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
        TextView amountoffrogs = (TextView) v.findViewById(R.id.amountoffrogs);
        TextView levelscompleted = (TextView) v.findViewById(R.id.levelscompleted);
        amountoffrogs.setText("Difficulty : "+String.valueOf(list.get(position).getAmountoffrogs()));
        levelscompleted.setText("Completed levels : "+String.valueOf(list.get(position).getLevels_completed())+
                "/"+String.valueOf(list.get(position).getTotallevels()));

        v.setTag(list.get(position));
        return v;
    }
}
