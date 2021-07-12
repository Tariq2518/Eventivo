package com.raredevz.eventivo.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.Helper.Manager;
import com.raredevz.eventivo.Helper.ManagerRequestAdapter;
import com.raredevz.eventivo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ac_ManagerRequests extends AppCompatActivity implements ManagerRequestAdapter.ItemClickListener {

    DatabaseReference dref;
    ArrayList<Manager> managerArrayList;
    RecyclerView ry_manager_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_requests);

        ry_manager_request=findViewById(R.id.ry_manager_requests);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ry_manager_request.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_ry_venue));
        ry_manager_request.addItemDecoration(dividerItemDecoration);

        dref= FirebaseDatabase.getInstance().getReference();
        dref.child("Manager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                managerArrayList=new ArrayList<>();
                for (DataSnapshot d:snapshot.getChildren()){
                    Manager manager=d.getValue(Manager.class);
                    if (!manager.isApproved()){
                        manager.setId(d.getKey());
                        managerArrayList.add(manager);
                    }
                }

                ManagerRequestAdapter adapter=new ManagerRequestAdapter(Ac_ManagerRequests.this,managerArrayList);
                adapter.setClickListener(Ac_ManagerRequests.this);
                ry_manager_request.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(View view, Manager manager) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Are you really want to approve request of manager "+manager.getName()+" ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        manager.setApproved(true);
                        dref.child("Manager").child(manager.getId()).setValue(manager);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}