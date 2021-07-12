package com.raredevz.eventivo.Admin.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raredevz.eventivo.Helper.AdapterManagerList;
import com.raredevz.eventivo.Helper.Manager;
import com.raredevz.eventivo.Helper.UserStatus;
import com.raredevz.eventivo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentManagerAccounts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentManagerAccounts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentManagerAccounts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUserAccounts.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentManagerAccounts newInstance(String param1, String param2) {
        FragmentManagerAccounts fragment = new FragmentManagerAccounts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    DatabaseReference dref;
    ArrayList<Manager> userList;
    ListView lvAccounts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_user_accounts, container, false);
        lvAccounts=view.findViewById(R.id.lvAccounts);
        dref= FirebaseDatabase.getInstance().getReference();
       initUi();
        return view;
    }

    private void initUi() {
        dref.child("Manager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList=new ArrayList<>();
                for (DataSnapshot u:snapshot.getChildren()){
                    Manager manager=u.getValue(Manager.class);
                    userList.add(manager);
                }
                AdapterManagerList adapterUserList=new AdapterManagerList(getContext(),userList);
                lvAccounts.setAdapter(adapterUserList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lvAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message="";
                Manager manager=userList.get(position);
                if (manager.getStatus().equals(UserStatus.Enabled)){
                    message="The current status of Manager is Enabled, do you want to ";
                }else {
                    message="The current status of Manager is Disabled, do you want to ";
                }
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.ly_manage_user);
                TextView tvMessga=dialog.findViewById(R.id.tvMessage);
                tvMessga.setText(message);
                Button btn=dialog.findViewById(R.id.btnManageUser);
                if (manager.getStatus().equals(UserStatus.Enabled))
                    btn.setText("Disable");
                else
                    btn.setText("Enable");

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (manager.getStatus().equals(UserStatus.Enabled)){
                            manager.setStatus(UserStatus.Disabled);
                            dref.child("Manager").child(manager.getId()).setValue(manager);
                        }else {
                            manager.setStatus(UserStatus.Enabled);
                            dref.child("Manager").child(manager.getId()).setValue(manager);
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }
}