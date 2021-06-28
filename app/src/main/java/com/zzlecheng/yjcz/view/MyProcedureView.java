package com.zzlecheng.yjcz.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.zzlecheng.yjcz.R;
import com.zzlecheng.yjcz.activity.ProcedureOperationActivity;
import com.zzlecheng.yjcz.activity.ProcedureSeeActivity;
import com.zzlecheng.yjcz.base.BaseObserver;
import com.zzlecheng.yjcz.bean.CommonBean;
import com.zzlecheng.yjcz.bean.LcBean;
import com.zzlecheng.yjcz.bean.ProcedureBean;
import com.zzlecheng.yjcz.eventbus.PushEventBean;
import com.zzlecheng.yjcz.net.HttpMethods;
import com.zzlecheng.yjcz.utils.AlertDialogManager;
import com.zzlecheng.yjcz.utils.CommonUtil;
import com.zzlecheng.yjcz.utils.LogUtils;
import com.zzlecheng.yjcz.utils.SharedPreferenceUtils;
import com.zzlecheng.yjcz.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @类名: MyProcedureView
 * @描述:
 * @作者: huangchao
 * @时间: 2018/12/6 2:26 PM
 * @版本: 1.0.0
 */
public class MyProcedureView extends View {

    //限制滑动宽度
    private List<Integer> listX = new ArrayList<>();
    private List<Integer> listY = new ArrayList<>();
    private int maxX;//宽度最多有几块
    private int maxY;//高度最多有几块
    private int maxWidth;//屏幕最宽值
    private int maxHeight;//屏幕最高值

    //屏幕宽度
    private int width;
    //屏幕高度
    private int height;
    //模块宽度为屏幕的1/6
    private int widthChild;
    //模块高度为模块宽度的1/2
    private int heightChild;
    //模块间的横向间隔为屏幕的1/6/4
    private int withSpace;
    //模块间的纵向间隔为模块高度
    private int heightSpace;
    //触摸偏移量x
    private float offsetX = 0;
    //触摸偏移量y
    private float offsetY = 0;
    //流程ID
    private String lcId = SharedPreferenceUtils.getInstance().getString("lcId");
    //流程历史ID
    private String lclsId = SharedPreferenceUtils.getInstance().getString("lclsId");
    //组织机构
    private String zzjgbm = SharedPreferenceUtils.getInstance().getString("zzjgbm");
    //流程名称
    private String lcmc = SharedPreferenceUtils.getInstance().getString("lcmc");
    //流程数据
    private List<ProcedureBean> beanList = new ArrayList<>();
    //背景画笔
    //已完成
    private Paint backPaintYWc;
    //未完成
    private Paint backPaintWwc;
    //无权限
    private Paint backPaintWqx;
    //文字画笔
    private TextPaint charPaint;
    //线画笔
    private Paint linePaint;
    //缩放画笔
    private Paint enPaint;

    private Canvas mCanvas = null;

    private float scale = 1.2f;

    private boolean flag = false;
    //true绘制
    public static boolean isHz = true;

    private String userId = "";

    private AlertDialogManager mDialog;

    private Map<String, ProcedureBean> map = new HashMap<>();

    private List<LcBean> lcBeans = new ArrayList<>();

    public MyProcedureView(Context context) {
        super(context);
        //注册订阅者
        EventBus.getDefault().register(this);
    }

    public MyProcedureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProcedureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        //设置绘布的大小
////        setMeasuredDimension(500,500);
//    }


    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveEventBus(PushEventBean eventBean) {
        drawWindow();
    }


    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //测量屏幕及模块宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        height = dm.heightPixels;
        width = dm.widthPixels;

        //初始化线画笔
        enPaint = new Paint();
        enPaint.setColor(getResources().getColor(R.color.white));
        enPaint.setStrokeWidth(5f);

        //画加号中心点坐标为（with-170,height-360）
        canvas.drawLine(width - 200, height - 500,
                width - 140, height - 500, enPaint);
        canvas.drawLine(width - 170, height - 530,
                width - 170, height - 470, enPaint);
        //画减号中心点坐标为（width-90,height-360）
        canvas.drawLine(width - 120, height - 500,
                width - 60, height - 500, enPaint);
        mCanvas = canvas;
        mCanvas.scale(scale, scale);
        drawWindow();
    }

    /**
     * 绘制的方法
     */
    private void drawWindow() {

        if (!flag || isHz) {
            LogUtils.e("重新绘制" + lcId + lclsId + zzjgbm);
            HttpMethods.getHttpMethods().getProcedure(getContext(), lcId, lclsId, zzjgbm, "0",
                    new BaseObserver<List<ProcedureBean>>(getContext(), false) {
                        @Override
                        protected void onHandleSuccess(List<ProcedureBean> procedureBeans) {
                            beanList.clear();
                            beanList = procedureBeans;
                            postInvalidate();
                            flag = true;
                            isHz = false;
                        }
                    });
        }
        if (flag) {
            readyProcedure();
        }
    }

    /**
     * 准备绘制
     */
    private void readyProcedure() {
        if (null != beanList && beanList.size() != 0) {

            //初始化模块画笔(已完成)
            backPaintYWc = new Paint();
            //设置模块画笔颜色
            backPaintYWc.setColor(getResources().getColor(R.color.ywc));
            //设置模块画笔粗细
            backPaintYWc.setStrokeWidth(2f);

            //初始化模块画笔（待完成）
            backPaintWwc = new Paint();
            //设置模块画笔颜色
            backPaintWwc.setColor(getResources().getColor(R.color.wwc));
            //设置模块画笔粗细
            backPaintWwc.setStrokeWidth(2f);

            //初始化模块画笔（无需操作）
            backPaintWqx = new Paint();
            //设置模块画笔颜色
            backPaintWqx.setColor(getResources().getColor(R.color.tm));
            //设置模块画笔粗细
            backPaintWqx.setStrokeWidth(2f);

            //初始化文字画笔
            charPaint = new TextPaint();
            //设置文字居中对齐
            charPaint.setTextAlign(Paint.Align.CENTER);
            charPaint.setColor(getResources().getColor(R.color.white));
            charPaint.setStrokeWidth(2f);
            charPaint.setTextSize(26f);

            //初始化线画笔
            linePaint = new Paint();
            linePaint.setColor(getResources().getColor(R.color.black));
            linePaint.setStrokeWidth(2f);

//            widthChild = width / 6;
            widthChild = 180;
            heightChild = widthChild / 2;
            withSpace = widthChild / 4;
            heightSpace = withSpace * 2;

            lcBeans.clear();

            //for循环绘制背景和文字
            for (int i = 0; i < beanList.size(); i++) {
                listX.add(Integer.valueOf(beanList.get(i).getNodex()));
                listY.add(Integer.valueOf(beanList.get(i).getNodey()));
                int mLeft = (widthChild + withSpace) * Integer.parseInt(beanList.get(i).getNodey()) + baseMoveX;
                int mTop = (heightChild + heightSpace) * Integer.parseInt(beanList.get(i).getNodex()) + baseMoveY;
                int mRight = widthChild + (withSpace + widthChild) * Integer.parseInt(beanList.get(i).getNodey()) + baseMoveX;
                int mBottom = heightChild + (heightSpace + heightChild) * Integer.parseInt(beanList.get(i).getNodex()) + baseMoveY;
                String text = beanList.get(i).getNodename();
                int charWith = mLeft + widthChild / 2;
                int charHeight = mTop + heightChild / 12;
//               已完成0，未完成有权限1，未完成无权限2
                String backColor = "0";
                if ("1".equals(beanList.get(i).getJdzt())) {//已完成
                    backColor = "0";
                } else if ("0".equals(beanList.get(i).getJdzt())) {//未完成
                    if ("1".equals(beanList.get(i).getCzqx())) {//未完成有权限
                        backColor = "1";
                    } else if ("0".equals(beanList.get(i).getCzqx())) {//未完成无权限
                        backColor = "2";
                    }
                }
                drawMain(mLeft, mTop, mRight, mBottom, backColor);
                if (CommonUtil.getWordCount(text) > 24) {
                    text = text.substring(0, 11);
                }
//                drawChar(text, charWith, charHeight);
                StaticLayout layout = new StaticLayout(text, charPaint, 160, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
                mCanvas.save();
                mCanvas.translate(charWith, charHeight);//从100，100开始画
                layout.draw(mCanvas);
                mCanvas.restore();//别忘了restore
                //组装线所需要的数据格式map
                map.put(beanList.get(i).getId(), beanList.get(i));
                //保存该点的坐标及文字等信息
                LcBean lcBean = new LcBean();
                lcBean.setContent(beanList.get(i).getNodename());
                lcBean.setQx(backColor);
                lcBean.setLeft((int) (mLeft * scale));
                lcBean.setTop((int) (mTop * scale));
                lcBean.setRight((int) (mRight * scale));
                lcBean.setBottom((int) (mBottom * scale));
                lcBean.setNodetype(beanList.get(i).getNodetype());
                lcBean.setLclsid(lclsId);
                lcBean.setJdid(beanList.get(i).getId());
                lcBean.setJdname(beanList.get(i).getNodename());
                lcBeans.add(lcBean);
            }
//            画线
            List<Map<String, ProcedureBean>> lineDate = new ArrayList<>();
            for (int i = 0; i < beanList.size(); i++) {
                ProcedureBean rec = beanList.get(i);
                String parentKeys = rec.getNodesupe();
                if (null != parentKeys && !"".equals(parentKeys)) {
                    String[] parentKey = parentKeys.split(",");
                    for (int j = 0; j < parentKey.length; j++) {
                        ProcedureBean parent = map.get(parentKey[j]);
                        if (null != parent && null != parent.getId() && !"".equals(parent.getId())) {
                            Map<String, ProcedureBean> m = new HashMap<>();
                            m.put("sunNode", rec);
                            m.put("parentNode", parent);
                            lineDate.add(m);
                        }
                    }
                }
            }
            for (int i = 0; i < lineDate.size(); i++) {
//                父节点的x
                String parentNodeX = lineDate.get(i).get("parentNode").getNodex();
//                父节点的y
                String parentNodeY = lineDate.get(i).get("parentNode").getNodey();
//                子节点的x
                String sunNodeX = lineDate.get(i).get("sunNode").getNodex();
//                子节点的y
                String sunNodeY = lineDate.get(i).get("sunNode").getNodey();
//                一共需要四个点四个坐标
                int parentX, parentY, rightX, rightY, leftX, leftY, sunX, sunY;
                parentX = (widthChild + withSpace) * Integer.parseInt(parentNodeY) + widthChild / 2 + baseMoveX;
                parentY = (heightChild + heightSpace) * Integer.parseInt(parentNodeX) + heightChild + baseMoveY;
                sunX = (widthChild + withSpace) * Integer.parseInt(sunNodeY) + widthChild / 2 + baseMoveX;
                sunY = (heightChild + heightSpace) * Integer.parseInt(sunNodeX) + baseMoveY;
                rightX = parentX;
                rightY = parentY + heightChild / 2;
                leftX = sunX;
                leftY = sunY - heightChild / 2;
                //父节点到父中线
                drawLine(parentX, parentY, rightX, rightY);
                //子节点到子中线
                drawLine(sunX, sunY, leftX, leftY);
                //子中线到父中线
                drawLine(leftX, leftY, rightX, rightY);
            }
        }

        maxX = Collections.max(listX);
        maxY = Collections.max(listY);
        maxWidth = (maxX + 1) * widthChild + maxX * withSpace;
        maxHeight = (maxY + 1) * heightChild + maxY * heightSpace * 2;
    }

    /**
     * 绘制模块背景
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void drawMain(int left, int top, int right, int bottom, String backColor) {
        if ("0".equals(backColor)) {
            mCanvas.drawRect(left, top, right, bottom, backPaintYWc);
        } else if ("1".equals(backColor)) {
            mCanvas.drawRect(left, top, right, bottom, backPaintWwc);
        } else if ("2".equals(backColor)) {
            mCanvas.drawRect(left, top, right, bottom, backPaintWqx);
        }
    }

    /**
     * 绘制文字
     *
     * @param text       文字
     * @param charWidth  文字的起点x
     * @param charHeight 文字的起点y
     */
    private void drawChar(String text, int charWidth, int charHeight) {
        mCanvas.drawText(text, charWidth, charHeight, charPaint);
    }

    /**
     * 绘制线
     *
     * @param beginX
     * @param beginY
     * @param endX
     * @param endY
     */
    private void drawLine(int beginX, int beginY, int endX, int endY) {
        mCanvas.drawLine(beginX, beginY,
                endX, endY, linePaint);
    }

    //记录触点的坐标
    float lastX = 0;
    float lastY = 0;
    int baseMoveX = 0;
    int baseMoveY = 0;
    float _x = 0;
    float _y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当前点的坐标（用于平移）
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://点击
                lastX = x;
                lastY = y;
                _x = x;
                _y = y;
                break;
            case MotionEvent.ACTION_UP://抬起
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                //加号
                if (upX > (width - 200) && upX < (width - 140) && upY > (height - 530) && upY < (height - 470)) {
                    scale = scale + 0.2f;
                    postInvalidate();
                }
                //减号
                if (upX > (width - 120) && upX < (width - 60) && upY > (height - 530) && upY < (height - 470)) {
                    scale = scale - 0.2f;
                    postInvalidate();
                }
                //确定点击的是哪个按钮并弹出信息
                for (int i = 0; i < lcBeans.size(); i++) {
                    if (upX > lcBeans.get(i).getLeft() && upX < lcBeans.get(i).getRight() &&
                            upY > lcBeans.get(i).getTop() && upY < lcBeans.get(i).getBottom()) {
                        String nodeType = lcBeans.get(i).getNodetype();
                        String lclsid = lcBeans.get(i).getLclsid();
                        String jdid = lcBeans.get(i).getJdid();
                        String jdname = lcBeans.get(i).getJdname();
                        AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
                        builder.setTitle("信息")
                                .setMessage(beanList.get(i).getNodename())
                                .setPositiveButton("查看", (dialog, which) -> {
                                    see(jdid);
                                })
                                .setNegativeButton("取消", (dialog, which) ->
                                        mDialog.dismiss()
                                );
                        mDialog = builder.create();
                        mDialog.show();
//                        //已完成0，未完成有权限1，未完成无权限2
//                        if ("0".equals(lcBeans.get(i).getQx())) {//已完成（查看）
//                            AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
//                            builder.setTitle("信息")
//                                    .setMessage(beanList.get(i).getNodename())
//                                    .setPositiveButton("查看", (dialog, which) -> {
//                                        see(jdid);
//                                    })
//                                    .setNegativeButton("取消", (dialog, which) ->
//                                            mDialog.dismiss()
//                                    );
//                            mDialog = builder.create();
//                            mDialog.show();
//                        } else if ("1".equals(lcBeans.get(i).getQx())) {//待完成（确认、结束和操作）
//
//                            if (Commons.NODE_TYPE_SURE.equals(nodeType)) {//确认
//                                AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
//                                builder.setTitle("确认")
//                                        .setMessage(beanList.get(i).getNodename())
//                                        .setPositiveButton("确认", (dialog, which) -> {
//                                            commitProcedure(lclsid, jdid, jdname, "确认", "node_type_sure");
//                                        })
//                                        .setNegativeButton("取消", (dialog, which) ->
//                                                mDialog.dismiss()
//                                        );
//                                mDialog = builder.create();
//                                mDialog.show();
//                            } else if (Commons.NODE_TYPE_END.equals(nodeType)) {//结束
//                                AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
//                                builder.setTitle("结束流程")
//                                        .setMessage(beanList.get(i).getNodename())
//                                        .setPositiveButton("结束", (dialog, which) -> {
//                                            commitProcedure(lclsid, jdid, jdname, "结束", "node_type_end");
//                                        })
//                                        .setNegativeButton("取消", (dialog, which) ->
//                                                mDialog.dismiss()
//                                        );
//                                mDialog = builder.create();
//                                mDialog.show();
//                            } else if (Commons.NODE_TYPE_MAKE.equals(nodeType)) {//操作
//                                AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
//                                builder.setTitle("操作")
//                                        .setMessage(beanList.get(i).getNodename())
//                                        .setPositiveButton("操作", (dialog, which) -> {
//                                            operation(lclsid, jdid, jdname);
//                                        })
//                                        .setNegativeButton("取消", (dialog, which) ->
//                                                mDialog.dismiss()
//                                        );
//                                mDialog = builder.create();
//                                mDialog.show();
//                            } else {//查看详情
//                                AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
//                                builder.setTitle("详情")
//                                        .setMessage(beanList.get(i).getNodename())
//                                        .setNegativeButton("取消", (dialog, which) ->
//                                                mDialog.dismiss()
//                                        );
//                                mDialog = builder.create();
//                                mDialog.show();
//                            }
//                        } else if ("2".equals(lcBeans.get(i).getQx())) {//不能操作
//                            AlertDialogManager.Builder builder = new AlertDialogManager.Builder(getContext());
//                            builder.setTitle("详情")
//                                    .setMessage(beanList.get(i).getNodename())
//                                    .setPositiveButton("查看", (dialog, which) -> {
//                                        see(jdid);
//                                    })
//                                    .setNegativeButton("取消", (dialog, which) -> {
//                                        mDialog.dismiss();
//                                    });
//                            mDialog = builder.create();
//                            mDialog.show();
//                        }
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE://移动

                //计算偏移量
                offsetX = x - lastX;
                offsetY = y - lastY;
                baseMoveX += (x - _x);
                baseMoveY += (y - _y);
                _x = x;
                _y = y;
                //不可绘制
                if (baseMoveX > 0) {
                    baseMoveX = 0;
                }
//                LogUtils.e(maxWidth+"-"+width);
                if (maxWidth < width) {//最大宽度小于屏幕宽度
                    if (baseMoveX <= -maxWidth * scale) {
                        baseMoveX = (int) (-maxWidth * scale);
                    }
                } else {
                    if (baseMoveX <= -(maxWidth - widthChild - withSpace) * scale) {
                        baseMoveX = (int) (-(maxWidth - widthChild - withSpace) * scale);
                    }
                }
                if (baseMoveY > heightChild) {
                    baseMoveY = heightChild;
                }
                if (maxHeight < height) {//最大高度小于屏幕高度
                    if (baseMoveY <= -maxHeight * scale) {
                        baseMoveY = (int) (-maxHeight * scale);
                    }
                } else {
                    if (baseMoveY <= -(maxHeight - height) * scale) {
                        baseMoveY = (int) (-(maxHeight - height) * scale);
                    }
                }
                //偏移量不为零时重新绘制
                if (offsetX != 0 || offsetY != 0) {
                    postInvalidate();
                }

                break;
        }
        return true;
    }

    /**
     * 确认或结束流程
     *
     * @param lclsid
     * @param jdid
     * @param
     * @param jdname
     */
    private void commitProcedure(String lclsid, String jdid, String jdname, String state, String czlx) {
        userId = SharedPreferenceUtils.getInstance().getString("userid");
//        LogUtils.e(lclsid+"-"+jdid+"-"+state+"-"+jdname+"-"+userId+"-"+czlx+"-"+lcmc);
        HttpMethods.getHttpMethods().commitProcedure(getContext(), lcId, lclsid, jdid, state, jdname,
                userId, "", czlx, lcmc, "", "0", new BaseObserver<CommonBean>(getContext(), false) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showShortToast(getContext(), state + "成功");
                        isHz = true;
                        postInvalidate();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    protected void onHandleError(String msg) {
                        LogUtils.e(msg);
                    }
                });
    }

    /**
     * 查看
     */
    private void see(String id) {
        postInvalidate();
        Intent intent = new Intent(getContext(), ProcedureSeeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    /**
     * 操作
     */
    private void operation(String lclsid, String jdid, String jdname) {
        Intent intent = new Intent(getContext(), ProcedureOperationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("lcid", lcId);
        bundle.putString("lclsId", lclsid);
        bundle.putString("jdId", jdid);
        bundle.putString("jdName", jdname);
        bundle.putString("lcmc", lcmc);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }


}
