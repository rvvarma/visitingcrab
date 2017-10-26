package com.example.hp.trcarbsvisiting;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
TextView date;
    public static final String REQUEST_METHOD = "POST";
    public static final int READ_TIMEOUT = 30000;
    public static final int CONNECTION_TIMEOUT = 30000;
    TextView response;
    String requestType;
    String id;

    JSONObject obj,visit;
    String trc;
    Button b;
    Spinner state,occupation;
    EditText adh,name,phone,email,pan,street,city,zip,country;
    Intent gatewayIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmvisiting);
        visit=new JSONObject();
        date=(TextView)findViewById(R.id.txtdate);
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
                try {
                    visit.put("DOB",date.getText().toString());
                    visit.put("name",name.getText().toString());
                    visit.put("phone",phone.getText().toString());
                    visit.put("email",email.getText().toString());
                    visit.put("pan",pan.getText().toString());
                    visit.put("street",street.getText().toString());
                    visit.put("city",city.getText().toString());
                    visit.put("zipcode",zip.getText().toString());
                    visit.put("state",state.getSelectedItem().toString());
                    visit.put("country",country.getText().toString());
                    visit.put("occupation",occupation.getSelectedItem().toString());
                    visit.put("Adhaar",adh.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String, String> postData = new HashMap<>();
                postData.put("purpose", "demo");
                postData.put("ver", "2.1");
                postData.put("storeCode", "QUASUS00036");
                HttpPostRequest Generate_id=new HttpPostRequest(postData);
                String num=adh.getText().toString();
                Generate_id.execute(num);
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
}
