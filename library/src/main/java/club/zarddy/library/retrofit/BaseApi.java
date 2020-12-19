package club.zarddy.library.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class BaseApi {

    /**
     * 初始化 Retrofit
     * @param domain 基础url
     * @return
     */
    public Retrofit initRetrofit(String domain) {
        Retrofit.Builder builder = new Retrofit.Builder();
        // 支持返回Call<String>
        builder.addConverterFactory(ScalarsConverterFactory.create());
        // 支持直接格式化json返回对象
        builder.addConverterFactory(FastJsonConverterFactory.create());
        // 支持RxJava
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.baseUrl(domain);

        OkHttpClient client = setClient();
        if (client != null) {
            builder.client(client);
        }
        return builder.build();
    }

    /**
     * 设置OkHttpClient，添加拦截器等
     * @return 可以返回null
     */
    protected abstract OkHttpClient setClient();
}
