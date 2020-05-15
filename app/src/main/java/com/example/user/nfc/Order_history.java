/*
package com.example.user.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Order_history extends Activity implements View.OnClickListener, DialogInterface.OnClickListener {
    private Activity activity;

    TextView tvDiscountPrice, tvFinalPrice;

    String token, myToken;
    int _index = 0;
    int price = 0;

    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity = this;

        findViewById(R.id.point_use).setOnClickListener(this);
        findViewById(R.id.cash_pay).setOnClickListener(this);
        findViewById(R.id.card_pay).setOnClickListener(this);

        Intent data = getIntent();
        myToken = data.getStringExtra("token");
        _index = data.getIntExtra("_index", 0);
        try {
            JSONArray array = new JSONArray(data.getStringExtra("data"));
            String[] menus = new String[array.length()];
            for (int i = 0; i < menus.length; i++) {
                JSONObject object = array.getJSONObject(i);
                menus[i] = object.getString("name");
                price += object.getInt("price");
            }

            ListView lv = (ListView) findViewById(R.id.purchase_list);
            //noinspection unchecked
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, menus);
            lv.setAdapter(adapter);

            TextView tvOrignalPrice = (TextView) findViewById(R.id.orignal_price);
            tvOrignalPrice.setText(String.format(getString(R.string.orignal_price), price));

            tvDiscountPrice = (TextView) findViewById(R.id.Discount_price);

            tvFinalPrice = (TextView) findViewById(R.id.Final_price_won);
            tvFinalPrice.setText(String.format(getString(R.string.final_price), price));

            str = array.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        get();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        activity = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cash_pay:
            case R.id.card_pay:
                try {
                    String[] regIds = new String[1];
                    regIds[0] = token;
                    JSONObject object = new JSONObject();
                    object.put("content_available", true);
                    object.put("priority", "high");
                    object.put("time_to_live", 60);

                    JSONObject object1 = new JSONObject();
                    object1.put("title", "주문 접수");
                    object1.put("body", "새 주문이 접수되었습니다. 빨리 확인해주세요.");
                    object1.put("sound", "default");

                    object.put("notification", object1);
                    object.put("data", str);
                    object.put("token", myToken);
                    Log.i(getPackageName(), object.toString());
                    sendGcm(regIds, getPackageName(), object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("결제 완료");
                builder.setMessage("결제가 완료되었습니다.");
                builder.setPositiveButton("확인", this);
                builder.show();
                break;

            case R.id.point_use:
                tvDiscountPrice.setText(String.format(getString(R.string.discount_price), 1365));
                tvFinalPrice.setText(String.format(getString(R.string.final_price), price - 1365));
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        setResult(RESULT_OK);
        finish();
    }

    private void get() {
        @SuppressLint("StaticFieldLeak")
        class Select extends AsyncTask<String, Void, String> {
            private Dialog dialog;

            @Override
            protected String doInBackground(String... params) {
                try {
                    String link = F.SERVER_IP.concat("get_token.php");
                    String data = URLEncoder.encode("_index", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");

                    URL url = new URL(link);
                    URLConnection connection = url.openConnection();
                    connection.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(data);
                    writer.flush();

                    StringBuilder builder = new StringBuilder();
                    String s;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((s = reader.readLine()) != null) {
                        builder.append(s);
                    }

                    return builder.toString().trim();
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialog = new Dialog(activity, R.style.MyDialog);
                dialog.setCancelable(true);
                dialog.addContentView(new ProgressBar(activity), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dialog.show();
            }

            @SuppressLint("DefaultLocale")
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.i(getPackageName(), s);

                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(s);
                    token = object.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Select select = new Select();
        select.execute(String.valueOf(_index));
    }

    public void sendGcm(final String[] regIds, final String title, final String message) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                //noinspection TryWithIdenticalCatches
                try {
                    Content content = new Content();
                    for (String regId : regIds) {
                        content.addRegId(regId);
                    }
                    content.createData(title, message);

                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "key=AIzaSyDmVLVKw7VEblKyqG5f1YY0ztMTmPwR-ko");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                    DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
                    mapper.writeValue(stream, content);
                    stream.flush();
                    stream.close();

                    int responseCode = connection.getResponseCode();
                    Log.i(getPackageName(), "ResponseCode: " + responseCode);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private class Content implements Serializable {
        private List<String> registration_ids = null;
        private Map<String, String> data;

        void addRegId(String regId) {
            if (registration_ids == null) {
                registration_ids = new LinkedList<>();
            }

            registration_ids.add(regId);
        }

        void createData(String title, String message) {
            if (data == null) {
                data = new HashMap<>();
            }

            data.put("title", title);
            data.put("message", message);
        }

        public Map<String, String> getData() {
            return data;
        }

        public void setData(Map<String, String> data) {
            this.data = data;
        }
    }
}
*/
