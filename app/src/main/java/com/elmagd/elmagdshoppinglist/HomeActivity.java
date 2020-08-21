package com.elmagd.elmagdshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.RunnableFuture;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private TextView totalAmount;
    private  ProgressDialog mDialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ListView listView;
    FirebaseListAdapter adapter;

    private String type, note;
    private int amount;
    private String post_key;

    public static void show(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        totalAmount = findViewById(R.id.total_amount);

        Context context;
        mDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String uId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);
        mDatabase.keepSynced(true);
        listView=findViewById(R.id.list_view);


        Query query = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);

        FirebaseListOptions<Data> options = new FirebaseListOptions.Builder<Data>()
                .setQuery(query, Data.class)
                .setLayout(R.layout.item_data)
                .build();
        adapter=new FirebaseListAdapter<Data>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull final Data model, final int position) {
                TextView mDate = v.findViewById(R.id.date);
                TextView  mType = v.findViewById(R.id.type);
                TextView mNote = v.findViewById(R.id.note);
                TextView mAmount = v.findViewById(R.id.amount);
                CardView parent= v.findViewById(R.id.rv_card);

                mDate.setText(model.getDate());
                mType.setText(model.getType());
                mNote.setText(model.getNote());
                String value = String.valueOf(model.amount);
                mAmount.setText("$ "+value);

                parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();
                        type = model.getType();
                        note = model.getNote();
                        amount = model.getAmount();

                        updateData();
                    }
                });mDialog.dismiss();
            }
        }; listView.setAdapter(adapter);

        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                //Calculating the total amount
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int amount = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Data data = snapshot.getValue(Data.class);
                            amount+=data.getAmount();
                            String tAmount = String.valueOf(amount);
                            totalAmount.setText("$ "+tAmount);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }});

            }
        });t1.start();




            fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    private void customDialog(){
        AlertDialog.Builder myDialog= new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);
        View view=inflater.inflate(R.layout.input_data_layout,null);
        final AlertDialog dialog=myDialog.create();
        dialog.setView(view);
        dialog.show();

        final EditText type = view.findViewById(R.id.edt_type);
        final EditText amount = view.findViewById(R.id.edt_amount);
        final EditText note = view.findViewById(R.id.edt_note);
        Button save = view.findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mType = type.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String mNote = note.getText().toString().trim();

                if (TextUtils.isEmpty(mType)){
                    type.setError("Required field.");
                    return;
                }
                if (TextUtils.isEmpty(mAmount)) {
                    amount.setError("Required field.");
                    return;
                }
                if (TextUtils.isEmpty(mNote)){
                    note.setError("Required field.");
                    return;
                }

                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                int amount = Integer.parseInt(mAmount);

                Data data = new Data(mType,amount,mNote,date,id);

                mDatabase.child(id).setValue(data);

                Toast.makeText(HomeActivity.this, "Shopping data added", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();


    }
        public void updateData(){
                AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view =  inflater.inflate(R.layout.update_input_data_layout,null);
                final AlertDialog dialog = myDialog.create();
                dialog.setView(view);
                dialog.show();
            final EditText uType = view.findViewById(R.id.edt_type_upd);
            final EditText uAmount = view.findViewById(R.id.edt_amount_upd);
            final EditText uNote = view.findViewById(R.id.edt_note_upd);

            uType.setText(type);
            uType.setSelection(type.length());
            uNote.setText(note);
            uNote.setSelection(note.length());
            uAmount.setText(String.valueOf(amount));
            uAmount.setSelection(String.valueOf(amount).length());

            Button update = view.findViewById(R.id.btn_update_upd);
            Button delete = view.findViewById(R.id.btn_delete_upd);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mType = uType.getText().toString().trim();
                    String mAamount = uAmount.getText().toString().trim();
                    String mNote = uNote.getText().toString().trim();
                    int intAmount = Integer.parseInt(mAamount);
                    String date = DateFormat.getDateInstance().format(new Date());

                    if (TextUtils.isEmpty(mType)){
                        uType.setError("Required field.");
                        return;
                    }

                    if (TextUtils.isEmpty(mNote)){
                        uNote.setError("Required field.");
                        return;
                    }

                    Data  data = new Data(mType,intAmount,mNote,date,post_key);
                    mDatabase.child(post_key).setValue(data);
                    Toast.makeText(HomeActivity.this, "Shopping data updated.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.child(post_key).removeValue();
                    Toast.makeText(HomeActivity.this, "Data deleted.", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            });
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.singout:
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this,RegistrationActivity.class));
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    }



