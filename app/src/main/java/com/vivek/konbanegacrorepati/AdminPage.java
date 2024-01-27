package com.vivek.konbanegacrorepati;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminPage extends AppCompatActivity {

    TextView reset;
    EditText question;
    RadioGroup option;
    RadioButton optionA,optionB,optionC,optionD,optionE;

    EditText optionA_txt,optionB_txt,optionC_txt,optionD_txt,optionE_txt;
    RadioGroup complexity;
    RadioButton c01,c02,c03,c04,c05;
    EditText time;
    Button submit;

    String t_question,t_optionA,t_optionB,t_optionC,t_optionD,t_optionE;
    char t_correctAns;
    int t_time,t_complexity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        findId();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetField();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gettext();

                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("http://192.168.0.103/php%20api/KBC/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService=retrofit.create(ApiService.class);

                Call<ResponseAdmin> admin=apiService.Admin
                        (t_question,t_optionA,t_optionB
                                ,t_optionC,t_optionD,t_optionE,t_correctAns,t_complexity,t_time);
                admin.enqueue(new Callback<ResponseAdmin>() {
                    @Override
                    public void onResponse(Call<ResponseAdmin> call, Response<ResponseAdmin> response) {
                        ResponseAdmin responseRegistration=response.body();
                        String success=responseRegistration.getSuccess();
                        String message= responseRegistration.getMessage();

                        Toast.makeText(getApplicationContext(),"success : "+success+"message  :"+message
                                ,Toast.LENGTH_SHORT).show();
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

    private void resetField() {
        question.getText().clear();
        optionA_txt.getText().clear();
        optionB_txt.getText().clear();
        optionC_txt.getText().clear();
        optionD_txt.getText().clear();
        optionE_txt.getText().clear();
        time.getText().clear();

        // Uncheck radio buttons in RadioGroups
        option.clearCheck();
        complexity.clearCheck();
    }

    private void gettext() {
        t_question=question.getText().toString();
        t_optionA=optionA_txt.getText().toString();
        t_optionB=optionB_txt.getText().toString();
        t_optionC=optionC_txt.getText().toString();
        t_optionD=optionD_txt.getText().toString();
        t_optionE=optionE_txt.getText().toString();
        option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected radio button from the group using checkedId
                if(checkedId==optionA.getId()){
                    t_correctAns='a';
                }
                if(checkedId==optionB.getId()){
                    t_correctAns='b';
                }
                if(checkedId==optionC.getId()){
                    t_correctAns='c';
                }
                if(checkedId==optionD.getId()){
                    t_correctAns='d';
                }
                if(checkedId==optionE.getId()){
                    t_correctAns='e';
                }

            }
        });

        complexity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected radio button from the group using checkedId
                if(checkedId==c01.getId()){
                    t_complexity=1;
                }
                if(checkedId==c02.getId()){
                    t_complexity=2;
                }
                if(checkedId==c03.getId()){
                    t_complexity=3;
                }
                if(checkedId==c04.getId()){
                    t_complexity=4;
                }
                if(checkedId==c05.getId()){
                    t_complexity=5;
                }

            }
        });
        t_time=Integer.parseInt(time.getText().toString());

    }

    private void findId() {
        reset=findViewById(R.id.reset);
        question=findViewById(R.id.question);
        option=findViewById(R.id.option);
        optionA=findViewById(R.id.optionA);
        optionB=findViewById(R.id.optionB);
        optionC=findViewById(R.id.optionC);
        optionD=findViewById(R.id.optionD);
        optionE=findViewById(R.id.optionE);
        optionA_txt=findViewById(R.id.optionA_txt);
        optionB_txt=findViewById(R.id.optionB_txt);
        optionC_txt=findViewById(R.id.optionC_txt);
        optionD_txt=findViewById(R.id.optionD_txt);
        optionE_txt=findViewById(R.id.optionE_txt);
        complexity=findViewById(R.id.complexity);
        c01=findViewById(R.id.c01);
        c02=findViewById(R.id.c02);
        c03=findViewById(R.id.c03);
        c04=findViewById(R.id.c04);
        c05=findViewById(R.id.c05);
        time=findViewById(R.id.time);
        submit=findViewById(R.id.submit);
    }
}