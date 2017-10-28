package com.example.hp.trcarbsvisiting;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import in.quagga.sdk.activity.ConsentActivity;
import static in.quagga.sdk.utils.ConstantsUtil.DENY_REQUEST;
import static in.quagga.sdk.utils.ConstantsUtil.GATEWAY_TRANSACTION_ID;
import static in.quagga.sdk.utils.ConstantsUtil.KEY_ACTIVITY_RESULT;
import static in.quagga.sdk.utils.ConstantsUtil.KEY_REQUEST_TYPE;
import static in.quagga.sdk.utils.ConstantsUtil.OTP_IDENTIFICATION;
import static in.quagga.sdk.utils.ConstantsUtil.REQUEST_FOR_OTP;

import static in.quagga.sdk.utils.ConstantsUtil.RESULT_ERROR;
import static in.quagga.sdk.utils.EnumRequestType.OTP_AUTH;

import static in.quagga.sdk.utils.EnumRequestType.OTP_EKYC;

public class farmvisiting extends AppCompatActivity {
TextView date,v_date,v_enddate;
    public static final String REQUEST_METHOD = "POST";
    public static final int READ_TIMEOUT = 30000;
    public static final int CONNECTION_TIMEOUT = 30000;
    TextView response;
    String requestType;
    String id;
Boolean vtype=false,etype=false;
    JSONObject obj,visit;
    String trc;
    Button b;
    Spinner state,occupation;
    EditText adh,name,phone,email,pan,street,city,zip,country;
    Intent gatewayIntent;
    RadioGroup rg;
    RadioButton vt,st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmvisiting);
        visit=new JSONObject();
        rg=(RadioGroup) findViewById(R.id.visit_type);

        vt=(RadioButton) findViewById(R.id.visit);
        st=(RadioButton) findViewById(R.id.stay);
        date=(TextView)findViewById(R.id.txtdate);
        v_date=(TextView)findViewById(R.id.v_visit);
        v_enddate=(TextView) findViewById(R.id.v_end);
        v_date.setVisibility(TextView.INVISIBLE);
        v_enddate.setVisibility(TextView.INVISIBLE);

        v_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog=new DateDialog(v);
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });
        v_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog=new DateDialog(v);
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.visit:
                    //  System.out.println("visit");
                        v_date.setHint("Enter Visit Date");
                        v_date.setVisibility(TextView.VISIBLE);
                        v_enddate.setVisibility(TextView.INVISIBLE);
                        etype=false;
                        vtype=true;
                        break;
                    case R.id.stay:
                        vtype=false;
                        etype=true;
                        v_date.setHint("Enter Start Date");
                        v_date.setVisibility(TextView.VISIBLE);
                        v_enddate.setHint("Enter End Date");
                        v_enddate.setVisibility(TextView.VISIBLE);
                      //  System.out.println("stay");

                        break;
//                    case R.id.radio3:
//                        RadioButton value = Integer.parseInt(((RadioButton) findViewById(R.id.radio3).getText()) * 3);
//                        break;
                }
            }
        });
        adh=(EditText) findViewById(R.id.adhaar);
        b=(Button) findViewById(R.id.bt_submitform);
        name=(EditText) findViewById(R.id.name);
        phone=(EditText) findViewById(R.id.phone_1);
        email=(EditText) findViewById(R.id.email);
        pan=(EditText) findViewById(R.id.pan);
        street=(EditText) findViewById(R.id.street_address1);
        city=(EditText) findViewById(R.id.city1);
        zip=(EditText) findViewById(R.id.zipcode);
        state=(Spinner) findViewById(R.id.state1);
        country=(EditText) findViewById(R.id.country);
        occupation=(Spinner) findViewById(R.id.occupation);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog=new DateDialog(v);
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if(name.getText().toString().isEmpty()){
                        name.setError("This Field Required");
                        showToast("Some fields are incomplete");
                 }
                    else if(date.getText().toString().length()==0){

                        showToast("Select DOB");

                    }

                 else if(phone.getText().toString().isEmpty()){
                      phone.setError("This Field Required");
                      showToast("Some fields are incomplete");
                    }
                    else if(email.getText().toString().isEmpty()){
                      email.setError("This Field Required");
                        showToast("Some fields are incomplete");
                    }
                    else if(pan.getText().toString().isEmpty()){
                        pan.setError("This Field Required");
                        showToast("Some fields are incomplete");
                    }
                    else if(street.getText().toString().isEmpty()){
                        street.setError("This Field Required");
                        showToast("Some fields are incomplete");
                    }
                    else if(city.getText().toString().isEmpty()){
                        city.setError("This Field Required");
                        showToast("Some fields are incomplete");
                    }
                    else if(zip.getText().toString().isEmpty()){
                        email.setError("This Field Required");
                        showToast("Some fields are incomplete");
                    }
                    else if(country.getText().toString().isEmpty()){
                        email.setError("This Field Required");
                        showToast("Some fields are incomplete");
                    }
                    else if(email.getText().toString().isEmpty()){
                        email.setError("This Field Required");
                        showToast("Some fields are incomplete");
                    }
                    else if(adh.getText().toString().isEmpty()){
                        email.setError("This Field Required");
                        showToast("Some fields are incomplete");

                    }
                    else if(adh.getText().toString().length()!=12){

                        showToast("Aadhar number is incorect");

                    }

                    else if(state.getSelectedItemPosition()==0) {
                        showToast("Select State");
                    }
                    else if(occupation.getSelectedItemPosition()==0) {
                        showToast("Select Occupation");
                    }

                    else if(!vtype&&!etype){

                        showToast("Please Check the Type");

                    }

                    else {
                        try {
                                if(vtype) {
                                if(v_date.getText().toString().length()==0){
                                    showToast("Select Visit Date");

                                }
                            }
                            else if(etype) {
                                if(v_date.getText().toString().length()==0){
                                    showToast("Select start Date");

                                }
                                if(v_enddate.getText().toString().length()==0){
                                    showToast("Select End Date");

                                }
                            }


                                    visit.put("DOB", date.getText().toString());
                                    visit.put("name", name.getText().toString());
                                    visit.put("phone", phone.getText().toString());
                                    visit.put("email", email.getText().toString());
                                    visit.put("pan", pan.getText().toString());
                                    visit.put("street", street.getText().toString());
                                    visit.put("city", city.getText().toString());
                                    visit.put("zipcode", zip.getText().toString());
                                    visit.put("state", state.getSelectedItem().toString());
                                    visit.put("country", country.getText().toString());
                                    visit.put("occupation", occupation.getSelectedItem().toString());
                                    visit.put("Adhaar", adh.getText().toString());
                                                            if(vtype)
                                visit.put("visit_Date",v_date.getText().toString());
                            if(etype)
                                    visit.put("stay_duration", v_date.getText().toString() + "," + v_enddate.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Map<String, String> postData = new HashMap<>();
                        postData.put("purpose", "demo");
                        postData.put("ver", "2.1");
                        postData.put("storeCode", "QUASUS00036");
                        System.out.println(visit.toString());
                        HttpPostRequest Generate_id = new HttpPostRequest(postData);
                        String num = adh.getText().toString();
                        Generate_id.execute(num);
                    }
            }
        });



        requestType= OTP_EKYC.getServiceName();


        // response.setText(trc);
        System.out.println("checking id"+id);


    }

    public class HttpPostRequest extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;
        JSONObject postData;
        public HttpPostRequest(Map<String, String> postData) {
            dialog = new ProgressDialog(farmvisiting.this);
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }
        @Override
        protected String doInBackground(String... params){
            String stringUrl = "http://34.214.58.80:3000/agents/EKYC/"+params[0];   //904617879924";
            String result = null;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestProperty("Content-Type", "application/json");

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = connection.getResponseCode();
                System.out.println("lkk"+statusCode);
                if (statusCode ==  200) {
                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(connection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                }
                else{

                    return null;
                }
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;

            //return null;
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("please wait.");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                obj=new JSONObject(result);
                id=obj.getString("id");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            gatewayIntent = new Intent(farmvisiting.this, ConsentActivity.class);
            gatewayIntent.putExtra(GATEWAY_TRANSACTION_ID, id);
            gatewayIntent.putExtra(KEY_REQUEST_TYPE, requestType);
            startActivityForResult(gatewayIntent, REQUEST_FOR_OTP);
            System.out.println("goy it"+result);
            super.onPostExecute(result);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == DENY_REQUEST) {
            String errorString = "Consent Denied";
            //handle error for otp_auth
            // response.setText(errorString);

        }
        /*
            Handle result for OTP
            - Remove if OTP is not being used
         */

        if (requestCode == REQUEST_FOR_OTP && null != data) {
            String requestType = data.getStringExtra(KEY_REQUEST_TYPE);

            /*
                Handle result for EKYC
                - Remove if only Auth is used in your App
             */
            if (requestType.equalsIgnoreCase(OTP_EKYC.getServiceName())) {

                if (resultCode == RESULT_OK) {
                    String responseString = data.getStringExtra(KEY_ACTIVITY_RESULT);
                    //handle success for otp_ekyc
//                    response.setText(responseString);
                    Intent io=new Intent(farmvisiting.this,declaration.class);
                    io.putExtra("data",responseString);
                    io.putExtra("visit",visit.toString());
                    startActivity(io);

                    System.out.println("hello output"+responseString);
                }

                if (resultCode == RESULT_ERROR) {
                    JSONObject ki=new JSONObject();
                    String errorString = data.getStringExtra(KEY_ACTIVITY_RESULT);
                    System.out.println("errr"+errorString);
                    try {

                        ki=new JSONObject(errorString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            farmvisiting.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("TR CRABS");
                    try {
                        alertDialog.setMessage(ki.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Setting Icon to Dialog


                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // Write your code here to execute after dialog closed
                            //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    //handle error for otp_ekyc
                    // response.setText(errorString);
                }
            }

        }
    }
    public void showToast(String message) {
        Toast.makeText(farmvisiting.this, message, Toast.LENGTH_LONG).show();
    }
}
