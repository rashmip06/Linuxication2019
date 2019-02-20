package com.example.rashmi.linuxication2k19;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String year;
    EditText nm,clg,fees,mode,phno,email;
    String id;
    Button register;
    DatabaseReference mRef,mRef2;
    Student stu;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirebaseUser curUser;
        curUser= FirebaseAuth.getInstance().getCurrentUser();
        uid=curUser.getUid();
        Toast.makeText(Registration.this,uid,Toast.LENGTH_LONG).show();

        Spinner spinner=findViewById(R.id.year);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(Registration.this,R.array.Year,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(Registration.this);
        register=(Button)findViewById(R.id.register);

        nm=(EditText)findViewById(R.id.name);
        clg=(EditText)findViewById(R.id.College);
        fees=(EditText)findViewById(R.id.Fees);
        mode=(EditText)findViewById(R.id.mode);
        phno=(EditText)findViewById(R.id.number);
        email=(EditText)findViewById(R.id.email);

        stu=new Student();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nm.getText().toString()))
                {
                    nm.setError("This item is required");
                }
                else if(TextUtils.isEmpty(clg.getText().toString()))
                {
                    clg.setError("This item is required");
                }
                else if(TextUtils.isEmpty(fees.getText().toString()))
                {
                    fees.setError("This item is required");
                }
                else if(TextUtils.isEmpty(mode.getText().toString()))
                {
                    mode.setError("This item is required");
                }
                else
                {
                    if(Integer.parseInt(fees.getText().toString())<=250)
                    {
                        stu.setCollege(clg.getText().toString());
                        stu.setMode(mode.getText().toString());
                        stu.setName(nm.getText().toString());
                        stu.setPaid(Integer.parseInt(fees.getText().toString()));
                        stu.setRemaining((250-Integer.parseInt(fees.getText().toString())));
                        stu.setPhone(phno.getText().toString());
                        stu.setEmail(email.getText().toString());
                        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                        mRef=FirebaseDatabase.getInstance().getReference();
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int id=Integer.parseInt(dataSnapshot.child("Count").getValue().toString());
                                ++id;
                                Log.d("Inside ondata change","inside on data change");
                                mRef.child("Count").setValue(id);
                                mRef2=FirebaseDatabase.getInstance().getReference().child(uid).child(String.valueOf(id));

                                mRef2.setValue(stu);
                                nm.setText("");
                                clg.setText("");
                                fees.setText("");
                                mode.setText("");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        fees.setError("Please Reenter value less than 250");
                    }


                  /*  mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int id=Integer.parseInt(dataSnapshot.child("Count").getValue().toString());
                            ++id;
                            Log.d("Inside ondata change","inside on data change");
                            mRef.child("Count").setValue(id);
                            mRef2=FirebaseDatabase.getInstance().getReference().child(uid).child(dataSnapshot.child("Count").getValue().toString());
                            mRef2.setValue(stu);
                            nm.setText("");
                            clg.setText("");
                            fees.setText("");
                            mode.setText("");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/


                }

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         year=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
