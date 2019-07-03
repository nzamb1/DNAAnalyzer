package com.dnaanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneLoginActivity extends BaseActivity {
    EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        phoneEditText = findViewById(R.id.editTextPhone);
        findViewById(R.id.btnSendCode).setOnClickListener(__ -> sendCode());
    }

    private void sendCode() {
        String number = phoneEditText.getText().toString();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(this, "Phone must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, PhoneLoginCodeActivity.class);
        intent.putExtra("phone", number);
        startActivity(intent);
    }
}
