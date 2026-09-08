package com.example.autocosmos

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity

class AuthWebViewActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면에 보여줄 WebView 생성
        val webView = WebView(this)
        setContentView(webView)

        val targetUrl = intent.getStringExtra("URL") ?: return

        webView.settings.javaScriptEnabled = true // 자바스크립트 허용
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                // 페이지 로딩 완료 시 찾으신 ID를 바탕으로 자동 클릭 스크립트 실행
                val js = """
                    javascript:(function() {
                        var checkBox = document.getElementById('checkbox-not-robot');
                        if(checkBox) checkBox.click();
                        
                        // 체크박스 클릭 후 0.5초(500ms) 대기 후 인증 버튼 클릭
                        setTimeout(function() {
                            var authBtn = document.getElementById('btn-tryAuth');
                            if(authBtn) authBtn.click();
                        }, 500);
                    })();
                """.trimIndent()

                view?.evaluateJavascript(js, null)
                Toast.makeText(this@AuthWebViewActivity, "코스모스 자동 인증 완료!", Toast.LENGTH_SHORT).show()

                // 3초 뒤에 앱 화면 자동 종료 (원치 않으면 아래 줄 삭제)
                webView.postDelayed({ finish() }, 3000)
            }
        }
        webView.loadUrl(targetUrl)
    }
}