package com.vivek.konbanegacrorepati;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class product extends AppCompatActivity {
    EditText productCode,productName,productPrice;
    Button submit,clear;
    String productCodeText,productNameText;
    int productPriceText,productDiscountText;

    Spinner spinner;
    List<String> patternNames = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getid();
        getDiscount();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productCode.setText("");
                productName.setText("");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettext();
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("http://"+IpAddress.ipAddress+"/php%20api/KBC/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService=retrofit.create(ApiService.class);
                Call<ResponseAdmin> productInfo=apiService.ProductInfo(productCodeText,productNameText,productPriceText,productDiscountText);
                productInfo.enqueue(new Callback<ResponseAdmin>() {
                    @Override
                    public void onResponse(Call<ResponseAdmin> call, Response<ResponseAdmin> response) {
                        ResponseAdmin responseRegistration=response.body();
                        String success=responseRegistration.getSuccess();
                        String message= responseRegistration.getMessage();

                        Toast.makeText(getApplicationContext(),"success : "+success+"message  :"+message
                                ,Toast.LENGTH_SHORT).show();
                        Call<ResponseAdmin> productInfo=apiService.ProductInfo(productCodeText,productNameText,productPriceText,productDiscountText);
                    }

                    @Override
                    public void onFailure(Call<ResponseAdmin> call, Throwable t) {
                        String errorMessage = t.getMessage();

                        // If the message is null, display a generic error message
                        if (errorMessage == null) {
                            errorMessage = "Request failed";
                        }

                        // Display the error message in a Toast
                        Toast.makeText(getApplicationContext(), "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void getDiscount() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://"+IpAddress.ipAddress+"/php%20api/KBC/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService=retrofit.create(ApiService.class);

        Call<List<GetPattern>> gettingPattern=apiService.GettingPattern();
        gettingPattern.enqueue(new Callback<List<GetPattern>>() {
            @Override
            public void onResponse(Call<List<GetPattern>> call, Response<List<GetPattern>> response) {
                List<GetPattern> patterns = response.body();

                for (GetPattern pattern : patterns) {
                    patternNames.add(String.valueOf(pattern.getMaxDiscount()));
                }

                adapter = new ArrayAdapter<>(product.this, android.R.layout.simple_spinner_item, patternNames);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<GetPattern>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"failed  "+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("errrrrrrrrrrrrrror", t.getMessage());
            }
        });
    }

    private void gettext() {
        productNameText=productName.getText().toString();
        productCodeText=productCode.getText().toString();
        productDiscountText=Integer.parseInt(productPrice.getText().toString());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                productPriceText = Integer.parseInt(parentView.getItemAtPosition(position).toString());
              }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }


    private void getid() {
        spinner=findViewById(R.id.discount);
        productCode=findViewById(R.id.product_code);
        productName=findViewById(R.id.product_name);
        productPrice=findViewById(R.id.product_price);
        spinner=findViewById(R.id.discounts);
        submit=findViewById(R.id.submit);
        clear=findViewById(R.id.clear);
    }
}