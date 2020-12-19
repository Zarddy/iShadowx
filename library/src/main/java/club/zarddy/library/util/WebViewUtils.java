package club.zarddy.library.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 浏览器功能
 */
public class WebViewUtils {

    public static final String MIME_TYPE = "text/html; charset=utf-8";
    public static final String ENCODING = "utf-8";

    // 初始化浏览器配置
    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebViewSettings(final Activity activity, final WebView webView) {
        if (webView == null) {
            return;
        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
            }
        });

        WebSettings webSettings = webView.getSettings();
        if (webSettings == null) {
            return;
        }

        webSettings.setJavaScriptEnabled(true);
        // 开启DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启database storage API功能
        webSettings.setDatabaseEnabled(true);

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUserAgentString(getDefaultUserAgent(activity) + " PDMobileHD/" + PackageUtils.getVersionName(activity));

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不使用缓存
    }

    /**
     * 浏览器加载网页内容（针对只有页面内容，没有头尾部的页面代码）
     * @param webView 显示内容的webView
     * @param html 静态页面内容
     */
    public static void loadHtmlData(WebView webView, String html) {
        loadHtmlData(webView, html, "", "", "");
    }

    public static void loadHtmlData(WebView webView, String html, String jsFunction) {
        loadHtmlData(webView, html, jsFunction, "", "");
    }

    /**
     * 浏览器加载网页内容（针对只有页面内容，没有头尾部的页面代码）
     * @param webView 显示内容的webView
     * @param html 静态页面内容
     * @param jsFunction 需要追加到底部的js方法
     */
    public static void loadHtmlData(WebView webView, String html, String jsFunction, String cssLink, String style) {

        jsFunction = TextUtils.isEmpty(jsFunction) ? "" : jsFunction;
        cssLink = TextUtils.isEmpty(cssLink) ? "" : cssLink;
        style = TextUtils.isEmpty(style) ? "" : style;

        String body = "<html><head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + cssLink
                + "<style>"
                + "img{max-width: 100%; width: auto; height: auto;}"
                + style
                + "</style>"
                + "</head><body>"
                + html
                + "<script type=\"text/javascript\">" + jsFunction + "</script>"
                + "</body>"
                + "</html>";

        webView.loadData(body, MIME_TYPE, ENCODING);
    }

    /**
     * 获取默认UserAgent，并添加当前app版号
     */
    public static String getDefaultUserAgent(final Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }

        if (TextUtils.isEmpty(userAgent)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userAgent.length(); i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 打开设备默认浏览器
     */
    public static void openBrowser(final Context context, final String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
