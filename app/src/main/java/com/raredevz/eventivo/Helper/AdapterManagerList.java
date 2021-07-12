package com.raredevz.eventivo.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raredevz.eventivo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterManagerList extends ArrayAdapter {
    ArrayList<Manager> userList;
    Context context;
    public AdapterManagerList(@NonNull Context context, ArrayList<Manager> userList) {
        super(context, android.R.layout.simple_selectable_list_item);
        this.context=context;
        this.userList=userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CircleImageView img=null;
        TextView tvName=null,tvContact=null,tvCity=null,tvEmail=null,tvUserStatus=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.li_account,parent,false);
        }

        tvName=convertView.findViewById(R.id.tvUserName);
        tvCity=convertView.findViewById(R.id.tvUserCity);
        tvContact=convertView.findViewById(R.id.tvUserContact);
        tvEmail=convertView.findViewById(R.id.tvUserEmail);
        tvUserStatus=convertView.findViewById(R.id.tvUserStatus);
        img=convertView.findViewById(R.id.imgUser);

        Manager user=userList.get(position);
        if (user.getImageUrl()!=null){
            Picasso.get().load(user.getImageUrl()).into(img);
        }
        tvName.setText("Name: "+user.getName());
        tvCity.setVisibility(View.GONE);
        tvContact.setText("Contact: "+user.getContact());
        tvEmail.setText("Email: "+user.getEmail());
        tvUserStatus.setText("Status: "+((user.getStatus()==UserStatus.Enabled)?"Enabled":"Disabled"));

        return convertView;
    }
}
