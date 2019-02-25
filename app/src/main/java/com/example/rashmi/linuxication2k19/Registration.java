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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String year;
    EditText nm,clg,fees,mode,phno,email;
    String id;
    Button register;
    DatabaseReference mRef,mRef2;
    Student stu;
    String uid;
    String mobiles,authkey,senderId,message,route;
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
        //Your authentication key
        authkey = "199192AN8u9SKkpL5a8d8d97";
    //Multiple mobiles numbers separated by comma

    //Sender ID,While using route4 sender id should be 6 characters long.
        senderId = "LXMCUG";
    //Your message to send, Add URL encoding here.

    //define route
        route="4";


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
                    if(fees.getText().toString().matches("[0-9]+")) {
                        if (Integer.parseInt(fees.getText().toString()) <= 250 ) {

                            if(phno.length()==10 ) {
                                if(phno.getText().toString().matches("[0-9]+")) {
                                    stu.setCollege(clg.getText().toString());
                                    stu.setMode(mode.getText().toString());
                                    stu.setName(nm.getText().toString());
                                    stu.setPaid(Integer.parseInt(fees.getText().toString()));
                                    stu.setRemaining((250 - Integer.parseInt(fees.getText().toString())));
                                    stu.setPhone(phno.getText().toString());
                                    stu.setEmail(email.getText().toString());

                                    mRef = FirebaseDatabase.getInstance().getReference();
                                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            int id = Integer.parseInt(dataSnapshot.child("Count").getValue().toString());
                                            ++id;
                                            Log.d("Inside ondata change", "inside on data change");
                                            mRef.child("Count").setValue(id);
                                            mRef2 = FirebaseDatabase.getInstance().getReference().child(uid).child(String.valueOf(id));

                                            mRef2.setValue(stu);
                                            Toast.makeText(Registration.this, "REGISTRATION DONE!!", Toast.LENGTH_LONG).show();
                                            mobiles=phno.getText().toString();
                                            id=id+100;
                                            if(stu.getRemaining()==0)
                                            {
                                                message="Thank You for registering for Linuxication 2K19.\nYour registration Id : LX-"+id+"\nTEAM MCUG";
                                            }
                                            else
                                            {
                                                message="Thank You for registering for Linuxication 2K19.\nYour registration Id : LX-"+id+"\nRemaining fees: "+stu.getRemaining()+"\nTEAM MCUG";
                                            }


                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    URLConnection myURLConnection=null;
                                                    URL myURL=null;
                                                    BufferedReader reader=null;

//encoding message
                                                    String encoded_message= URLEncoder.encode(message);


                                                    String mainUrl="http://api.msg91.com/api/sendhttp.php?";

                                                    StringBuilder sbPostData= new StringBuilder(mainUrl);
                                                    sbPostData.append("authkey="+authkey);
                                                    sbPostData.append("&mobiles="+mobiles);
                                                    sbPostData.append("&message="+encoded_message);
                                                    sbPostData.append("&route="+route);
                                                    sbPostData.append("&sender="+senderId);


                                                    mainUrl = sbPostData.toString();
                                                    try
                                                    {
                                                        //prepare connection
                                                        myURL = new URL(mainUrl);
                                                        myURLConnection = myURL.openConnection();
                                                        myURLConnection.connect();
                                                        reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

                                                        //reading response
                                                        String response;
                                                        while ((response = reader.readLine()) != null)
                                                            //print response
                                                            Log.d("RESPONSE", ""+response);

                                                        //finally close connection
                                                        reader.close();
                                                    }
                                                    catch (IOException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();

                                            nm.setText("");
                                            clg.setText("");
                                            fees.setText("");
                                            mode.setText("");
                                            phno.setText("");
                                            email.setText("");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else
                                {
                                    phno.setError("Phone number should contain only digits");
                                }
                            }
                            else
                            {
                                phno.setError("Phone number should contain 10 digits");
                            }
                        }
                        else
                        {
                            fees.setError("Please Reenter value less than 250");
                        }
                    }
                    else
                    {
                        fees.setError("Please write numbers");
                    }





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
