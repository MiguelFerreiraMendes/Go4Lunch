package com.miguel.go4lunch_p6;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String URL = getIntent().getStringExtra("url");
        Log.i("webview", "url webview" + URL);
        WebView webView = findViewById(R.id.webview);
        webView.loadUrl(URL);
    }
}
