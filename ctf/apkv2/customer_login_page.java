package com.v3.onlinebanking;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.UByte;

public class customer_login_page extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.v3.onlineBanking.EXTRA_TEXT";
    DBHelper DB;
    EditText accountNumber;
    Button customerLoginBTN;
    EditText deployment;
    Dialog dialog;
    EditText pin;

    public void alert(String str) {
        AlertDialog create = new AlertDialog.Builder(this).create();
        create.setTitle(str);
        create.setMessage("App will close now");
        create.setButton(-3, "OK", new customer_login_page$$ExternalSyntheticLambda0());
        create.setCancelable(false);
        create.show();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_customer_login_page);
        this.dialog = new Dialog(this);
        this.accountNumber = (EditText) findViewById(R.id.customer_acc_no);
        this.pin = (EditText) findViewById(R.id.customer_pin_no);
        this.deployment = (EditText) findViewById(R.id.deployment_url);
        this.customerLoginBTN = (Button) findViewById(R.id.customer_login_btn);
        if (!isNetworkAvailable(getApplication()).booleanValue()) {
            alert("Please connect to the internet");
        }
        DBHelper dBHelper = new DBHelper(getApplicationContext());
        this.DB = dBHelper;
        dBHelper.createDB();
        this.customerLoginBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String obj = customer_login_page.this.accountNumber.getText().toString();
                String obj2 = customer_login_page.this.pin.getText().toString();
                String obj3 = customer_login_page.this.deployment.getText().toString();
                if (obj.equals("") || obj2.equals("") || obj3.equals("")) {
                    Toast.makeText(customer_login_page.this, "Please Enter all the fields !", 0).show();
                    return;
                }
                try {
                    if (!customer_login_page.this.isValidURL(obj3)) {
                        Toast.makeText(customer_login_page.this, "Invalid URL", 0).show();
                    } else if (customer_login_page.this.DB.authenticateUser(obj, customer_login_page.hash(Long.valueOf(Long.parseLong(obj2)))).booleanValue()) {
                        customer_login_page.this.customerLogin(obj2);
                    } else {
                        customer_login_page.this.invalidCredentialsDialog();
                    }
                } catch (MalformedURLException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService("connectivity");
        Network activeNetwork = connectivityManager.getActiveNetwork();
        boolean z = false;
        if (activeNetwork == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        if (networkCapabilities != null && (networkCapabilities.hasTransport(1) || networkCapabilities.hasTransport(0) || networkCapabilities.hasTransport(3) || networkCapabilities.hasTransport(2))) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public static Long hash(Long l) {
        Long valueOf = Long.valueOf(((l.longValue() & 2863311530L) >>> 1) | ((l.longValue() & 1431655765) << 1));
        Long valueOf2 = Long.valueOf(((valueOf.longValue() & 3435973836L) >>> 2) | ((valueOf.longValue() & 858993459) << 2));
        Long valueOf3 = Long.valueOf(((valueOf2.longValue() & 4042322160L) >>> 4) | ((valueOf2.longValue() & 252645135) << 4));
        Long valueOf4 = Long.valueOf(((valueOf3.longValue() & 4278255360L) >>> 8) | ((valueOf3.longValue() & 16711935) << 8));
        return Long.valueOf((valueOf4.longValue() >>> 16) | (valueOf4.longValue() << 16));
    }

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isValidURL(String str) throws MalformedURLException, URISyntaxException {
        try {
            new URL(str).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException unused) {
            return false;
        }
    }

    public void customerLogin(String str) {
        String obj = this.accountNumber.getText().toString();
        String obj2 = this.deployment.getText().toString();
        Intent intent = new Intent(this, customer_workspace_activity.class);
        intent.putExtra("com.v3.onlineBanking.EXTRA_TEXT", obj);
        intent.putExtra("depl_URL", obj2);
        intent.putExtra("pin", str);
        startActivity(intent);
    }

    public void invalidCredentialsDialog() {
        this.dialog.setContentView(R.layout.invalid_credentials);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ((ImageView) this.dialog.findViewById(R.id.imageViewClose)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                customer_login_page.this.dialog.dismiss();
            }
        });
        ((Button) this.dialog.findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                customer_login_page.this.dialog.dismiss();
            }
        });
        this.dialog.show();
    }
}
