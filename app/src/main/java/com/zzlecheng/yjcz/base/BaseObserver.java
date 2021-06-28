package com.zzlecheng.yjcz.base;

import android.app.ProgressDialog;
import android.content.Context;

import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 对返回结果的封装
 *
 * @param <T>
 */
public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private Context mContext;
    private ProgressDialog progressDialog;
    private Disposable disposable;

    //默认无效果的请求
    protected BaseObserver(Context context) {
        this.mContext = context.getApplicationContext();
    }

    //带进度条的请求
    protected BaseObserver(Context context, boolean showProgress) {
        this.mContext = context.getApplicationContext();
        if (showProgress) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setOnCancelListener(dialogInterface -> disposable.dispose());
            progressDialog.show();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(BaseBean<T> value) {
        //这里对数据bean的封装,这里看个人的网络回掉的结果来操作，我是随便写的
        if (value.getCode().equals(Commons.DATA_SUCCESS_CODE)) {// 请求成功
            onHandleSuccess(value.getData());
        } else {
            onHandleError(value.getMsg());
        }

    }

    @Override
    public void onError(Throwable e) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onComplete() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        ToastUtils.showLongToast(mContext, msg);
    }
}