package com.pk.ubulance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginAuth extends AppCompatActivity {

    private final String CLIENT_ID = "npY9QZdQcwcrp4uIazxvKYcnDlvcmXYy";
    private final String AUTH_URL = "https://login.uber.com/oauth/v2/authorize?client_id=" +
            CLIENT_ID+"&response_type=code&scope=request";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_auth);

        WebView webview = new WebView(this);
        setContentView(webview);


        final Activity activity = this;

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%

                activity.setProgress(progress * 1000);
            }
        });

        webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                String summary = "<html><body></body></html>";
                view.loadData(summary, "text/html", null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webview.loadUrl(AUTH_URL);


    }
}
