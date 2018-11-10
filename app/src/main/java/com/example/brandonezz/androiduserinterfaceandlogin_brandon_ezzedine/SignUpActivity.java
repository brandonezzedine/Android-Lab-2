package com.example.brandonezz.androiduserinterfaceandlogin_brandon_ezzedine;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText name, username, birthdate, phone, email, password;
    Button mRegisterbtn;
    String Name, Username, Birthdate, Phone, Email, Password;
    ProgressDialog mDialog;
    SQLiteOpenHelper openHelper;
    SQLiteHelper sqLiteHelper;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteDatabase db;
    Cursor cursor;
    String F_Result = "Not_Found";
    DatabaseReference mdatabase;
    FirebaseAuth mAuth;
    public static final String TAG="LOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name = (EditText) findViewById(R.id.editName);
        username = (EditText) findViewById(R.id.editUsername);
        birthdate = (EditText) findViewById(R.id.editBirthdate);
        phone = (EditText) findViewById(R.id.editPhone);
        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPassword);
        mRegisterbtn = (Button) findViewById(R.id.buttonRegister);
        mDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        sqLiteHelper = new SQLiteHelper(this);



        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SQLiteDataBaseBuild();

                SQLiteTableBuild();

                CheckEditTextStatus();

                CheckingEmailAlreadyExistsOrNot();

                EmptyEditTextAfterDataInsert();


            }
        });

            }


    private void UserRegister() {
        Name = name.getText().toString().trim();
        Username =username.getText().toString().trim();
        Birthdate =birthdate.getText().toString().trim();
        Phone =phone.getText().toString().trim();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Name)){
            Toast.makeText(SignUpActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Email)){
            Toast.makeText(SignUpActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Password)){
            Toast.makeText(SignUpActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (Password.length()<6){
            Toast.makeText(SignUpActivity.this,"Passwor must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){


                    FirebaseUser user = mAuth.getCurrentUser();
                    OnAuth(task.getResult().getUser());
                    Log.d(TAG, "createUserWithEmail:success");
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                }
                else{
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this,"error on creating user",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }



    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }
    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
    }


    private User BuildNewuser(){
        return new User(


                getName(),
                getUsername(),
                getBirthdate(),
                getPhone(),
                getEmail()

        );
    }

    public String getName() {
        return Name;
    }

    public String getUsername() {
        return Username;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {

        return Email;
    }









    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Username + " VARCHAR, " + SQLiteHelper.Table_Column_3_Birthdate + " VARCHAR, " + SQLiteHelper.Table_Column_4_Phone + " VARCHAR, " + SQLiteHelper.Table_Column_5_Email + " VARCHAR, " + SQLiteHelper.Table_Column_6_Password +" VARCHAR)");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase() {
        long a;
        // If editText is not empty then this block will executed.
        if (EditTextEmptyHolder == true) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", Name);
            contentValues.put("username", Username);
            contentValues.put("birthdate", Birthdate);
            contentValues.put("phone", Phone);
            contentValues.put("email", Email);
            contentValues.put("password", Password);
            a = sqLiteHelper.insert(SQLiteHelper.TABLE_NAME, contentValues, SQLiteHelper.Table_Column_ID);
            if (a > 0) {
                Toast.makeText(this, "User Succesfully Created", Toast.LENGTH_SHORT).show();
            }
            sqLiteDatabaseObj.close();

        }
    }



    public void EmptyEditTextAfterDataInsert(){

        name.getText().clear();

        username.getText().clear();

        birthdate.getText().clear();

        phone.getText().clear();

        email.getText().clear();

        password.getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){


        Name = name.getText().toString();
        Username = username.getText().toString();
        Birthdate = birthdate.getText().toString();
        Phone = phone.getText().toString();
        Email = email.getText().toString();
        Password = password.getText().toString();

        if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    // Checking Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot(){

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_5_Email + "=?", new String[]{Email}, null, null, null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Email is already exists then Result variable value set as Email Found.
                F_Result = "Email Found";

                // Closing cursor.
                cursor.close();
            }
        }

        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();

    }


    // Checking result
    public void CheckFinalResult(){

        // Checking whether email is already exists or not.
        if(F_Result.equalsIgnoreCase("Email Found"))
        {

            // If email is exists then toast msg will display.
            Toast.makeText(SignUpActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();

        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();

        }

        F_Result = "Not_Found" ;

    }

}



