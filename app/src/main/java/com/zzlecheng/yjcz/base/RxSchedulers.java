package com.zzlecheng.yjcz.base;

import android.content.Context;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zzlecheng.yjcz.utils.NetUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络请求初步拦截  对 是否有网络的 第一层过滤
 */
public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> compose(final Context context) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .compose(((RxAppCompatActivity)context).bindToLifecycle())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (!NetUtils.isNetworkConnected(context)) {
                        ToastUtils.showLongToast(context, "请检查手机网络状态");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}