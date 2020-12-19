package club.zarddy.ishadowx.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import club.zarddy.ishadowx.Config;
import club.zarddy.ishadowx.model.IShadowAccount;
import club.zarddy.ishadowx.parser.JsoupParser;
import club.zarddy.library.lifecycle.BaseViewModel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class MainViewModel extends BaseViewModel {

    private MutableLiveData<List<IShadowAccount>> mIShadowAccountList = new MutableLiveData<>();

    public MutableLiveData<List<IShadowAccount>> getIShadowAccountList() {
        return mIShadowAccountList;
    }

    private ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build();

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectionSpecs(Collections.singletonList(spec))
            .build();

    private List<IShadowAccount> runUrl(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                return new ArrayList<>();
            }
            return JsoupParser.parse(response.body().string());
        }
    }

    /**
     * 加载本地存储的iShadow账号信息
     */
    public void loadAccountListCache() {
        // 加载本地缓存
        mIShadowAccountList.setValue(Hawk.get(Config.KEY_iShadowx_List, new ArrayList<IShadowAccount>()));
    }

    /**
     * 从网络上获取iShadowx账号信息
     */
    public void fetchAccountList() {
        Observable<List<IShadowAccount>> observable = Observable.create(new ObservableOnSubscribe<List<IShadowAccount>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<IShadowAccount>> emitter) throws Exception {
                try {
                    emitter.onNext(runUrl(Config.url));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Observer<List<IShadowAccount>> observer = new Observer<List<IShadowAccount>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mLoadingDialog.setValue(true);
            }
            @Override
            public void onNext(List<IShadowAccount> list) {
                mIShadowAccountList.setValue(list);
                Hawk.put(Config.KEY_iShadowx_List, list);
            }
            @Override
            public void onError(Throwable e) {
                mLoadingDialog.setValue(false);
                showErrorMessage("加载出错！！！");
            }
            @Override
            public void onComplete() {
                mLoadingDialog.setValue(false);
            }
        };
        observable.subscribe(observer);
    }
}
