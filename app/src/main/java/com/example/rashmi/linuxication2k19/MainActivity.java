package com.example.rashmi.linuxication2k19;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText name,pass;
    Button login;
    ProgressBar progressBar;
     FirebaseAuth mAuth;
     String username,password;
     DatabaseReference mRef;
     FirebaseAuth.AuthStateListener mAuthList;
     String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuthList= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    Intent intent=new Intent(MainActivity.this,Registration.class);
                    startActivity(intent);
                }
            }
        };

        name=(EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.pass);

        login=(Button)findViewById(R.id.login);
        progressBar=(ProgressBar)findViewById(R.id.progressbar) ;



        mRef=FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=name.getText().toString();
                password=pass.getText().toString();
                mAuth=FirebaseAuth.getInstance();
               mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           progressBar.setVisibility(View.GONE);
                           FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                           mRef.child(currentUser.getUid());
                           Intent intent=new Intent(MainActivity.this,Registration.class);
                           startActivity(intent);
                       }
                       else
                       {
                           Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();                       }
                   }
               });

            }


        });



    }

    protected  void onStart() {

        super.onStart();
//        mAuth.addAuthStateListener(mAuthList);
    }
}
