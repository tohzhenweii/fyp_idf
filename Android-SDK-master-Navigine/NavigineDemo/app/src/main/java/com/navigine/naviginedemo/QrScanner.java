package com.navigine.naviginedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QrScanner extends AppCompatActivity {
CodeScanner mcodeScanner;
CodeScannerView mscannerView;
TextView mResultData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        mscannerView = findViewById(R.id.Scannerview);
        mcodeScanner = new CodeScanner(this, mscannerView);
        mResultData = findViewById(R.id.tvScanResult);
        mcodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mResultData.setText((result.getText()));
                    }

                });
        }

        });
        onResume();
        mcodeScanner.startPreview();
    }

}