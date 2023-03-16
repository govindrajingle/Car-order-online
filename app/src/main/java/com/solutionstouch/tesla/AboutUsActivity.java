package com.solutionstouch.tesla;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutUsActivity extends AppCompatActivity implements PaymentResultListener {

    EditText name,number,mail,country,state,city;
    Button button;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    EmployeeInfo employeeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setTitle();

        name = findViewById(R.id.idEdtEmployeeName);
        mail = findViewById(R.id.idEdtEmployeeEmail);
        number = findViewById(R.id.idEdtEmployeePhoneNumber);
        country = findViewById(R.id.idEdtEmployeeCountry);
        state = findViewById(R.id.idEdtEmployeeState);
        city = findViewById(R.id.idEdtEmployeeCity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("EmployeeInfo");
        employeeInfo = new EmployeeInfo();
        button = findViewById(R.id.idBtnSendData);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                String mail1 = mail.getText().toString();
                String number1 = number.getText().toString();
                String country1 = country.getText().toString();
                String state1 = state.getText().toString();
                String city1 = city.getText().toString();

                if (TextUtils.isEmpty(name1) && TextUtils.isEmpty(mail1) && TextUtils.isEmpty(number1) && TextUtils.isEmpty(country1)
                && TextUtils.isEmpty(state1) && TextUtils.isEmpty(city1)){
                    Toast.makeText(AboutUsActivity.this,"Please enter all fields",Toast.LENGTH_LONG).show();
                }else{
                    addDatatoFirebase(name1, mail1, number1, country1, state1, city1);
                }
            }
        });
    }

    private void addDatatoFirebase(String name1, String mail1, String number1, String country1, String state1, String city1) {
        employeeInfo.setName(name1);
        employeeInfo.setMailid(mail1);
        employeeInfo.setNumber(number1);
        employeeInfo.setCountry(country1);
        employeeInfo.setState(state1);
        employeeInfo.setCity(city1);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(employeeInfo);
                Toast.makeText(AboutUsActivity.this, "Redirecting to payment gateway", Toast.LENGTH_SHORT).show();
                Log.d("succes","success");
                paymentCall();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AboutUsActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                Log.d("fail","dd"+error);

            }
        });
    }

    public void paymentCall(){
        Checkout.preload(getApplicationContext());
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_K2hC7Urbu9P8vz");
     //   checkout.setImage(R.drawable.payment);

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name",name.getText().toString());
            jsonObject.put("description","Purchase Payment");
            jsonObject.put("currency","INR");
            jsonObject.put("amount", "100");
            jsonObject.put("prefill.contact",number.getText().toString());
            jsonObject.put("prefill.email", mail.getText().toString());
            checkout.open(AboutUsActivity.this,jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
        }

    }
    private void setTitle() {
        setTitle("Customer Details");
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),"Success...Happy Driving..!!!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AboutUsActivity.this,MainActivity.class);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),s.toString(),Toast.LENGTH_SHORT).show();

    }
}
