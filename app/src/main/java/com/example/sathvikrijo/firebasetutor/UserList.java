package com.example.sathvikrijo.firebasetutor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<Information> {

    Activity mcontext;
    List<Information> infoList;

    public UserList(Activity mcontext, List<Information> infoList) {
        super(mcontext, R.layout.list_layout, infoList);
        this.infoList = infoList;
        this.mcontext = mcontext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = mcontext.getLayoutInflater();

        View listItemView = layoutInflater.inflate(R.layout.list_layout,null,true);

        TextView textViewName = listItemView.findViewById(R.id.tv_name);
        TextView textViewMsg = listItemView.findViewById(R.id.tv_msg);

        Information userInfo = infoList.get(position);
        textViewName.setText(userInfo.getName());
        textViewMsg.setText(userInfo.getMsg());

        return listItemView;

    }
}
