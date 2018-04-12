package com.example.zsk.scrollpicture;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.zsk.scrollpicture.sql.SqlHelper;
import com.example.zsk.scrollpicture.util.ToastUtil;
import com.example.zsk.scrollpicture.util.Util;
import com.example.zsk.scrollpicture.view.BannerView;
import com.example.zsk.scrollpicture.view.CommonDialog;
import com.google.gson.Gson;
import com.xw.repo.BubbleSeekBar;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements View.OnClickListener {

    private BannerView bannerView;
    private RequestQueue requestQueue;
    private RelativeLayout setImageView;
    private PopupWindow popupWindow;
    private RelativeLayout exitLayout;
    private Dialog setDialog;
    private String url = "http://doucaiwang.com/app/?url=/home/imgdata";
    private List<String> pictureList;
    private List<BannerItem> bannerItems = new ArrayList<>();
    private List<String> temporaryList = new ArrayList<>();
    private int[] process = new int[]{4, 5};
    private Timer timers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.base_message_bannerview);
        requestQueue = NoHttp.newRequestQueue();
        pictureList = new ArrayList<>();
        setImageView = (RelativeLayout) findViewById(R.id.iv_setview);
        exitLayout = (RelativeLayout) findViewById(R.id.ll_exit);
        setImageView.setOnClickListener(this);
        exitLayout.setOnClickListener(this);
        timers = new Timer();
        initParams();   //初始化参数
        timer();
        hideBottomUIMenu();    //隐藏虚拟按键，并且全屏
    }

    private void initParams() {
        int[] processArray = SqlHelper.getProcess();
        if (processArray[0] != 0 && processArray[1] != 0) {
            process[0] = processArray[0];
            process[1] = processArray[1];
        }
        bannerView.setInterval(process[0] * 1000);
    }


    private void timer() {
        timers = new Timer();
        timers.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestPicture();
            }
        }, 0, 1000 * 60 * process[1]);
    }

    private void requestPicture() {
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        requestQueue.add(0, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Boolean isContains = true;
                String result = response.get().toString().trim();
                pictureList.clear();
                ScrolModel scrolModel = new Gson().fromJson(result, ScrolModel.class);
                List<ScrolModel.DataBean.PlayerBean> playerBeenList = scrolModel.getData().getPlayer();
                for (ScrolModel.DataBean.PlayerBean player : playerBeenList) {
                    pictureList.add(player.getPic());
                }
                if (temporaryList.size() != pictureList.size()) {
                    bannerItems.clear();
                    isContains = false;
                } else {
                    for (String pictureUrl : pictureList) {
                        if (!temporaryList.contains(pictureUrl)) {
                            bannerItems.clear();
                            isContains = false;
                        }
                    }
                }
                if (isContains) {
                    return;
                }
                temporaryList.clear();
                temporaryList.addAll(pictureList);
                for (String picture : pictureList) {
                    BannerItem item = new BannerItem();
                    item.image = picture;
                    item.title = "";
                    bannerItems.add(item);
                }
                bannerView.setViewFactory(new BannerViewFactory());
                bannerView.setDataList(bannerItems);
                bannerView.start();
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    //设置弹出框的样式
    private void setPopuStyle() {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View layoutLeft = getLayoutInflater().inflate(R.layout.dialog_set_view, null);

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow = new PopupWindow(layoutLeft, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //设置动画
            popupWindow.setAnimationStyle(R.style.popup_window_anim);
            //设置背景颜色
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
            popupWindow.setTouchable(true); // 设置popupwindow可点击
            popupWindow.setOutsideTouchable(true); // 设置popupwindow外部可点击
            popupWindow.setFocusable(true); // 获取焦点
            final BubbleSeekBar bsScroll = (BubbleSeekBar) layoutLeft.findViewById(R.id.bs_scroll);
            final BubbleSeekBar bsUpdate = (BubbleSeekBar) layoutLeft.findViewById(R.id.bs_update);
            int[] processArray = SqlHelper.getProcess();
            bsScroll.setProgress(processArray[0]);
            bsUpdate.setProgress(processArray[1]);
            layoutLeft.findViewById(R.id.ll_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            Button btSave = (Button) layoutLeft.findViewById(R.id.bt_save);
            Button btExit = (Button) layoutLeft.findViewById(R.id.bt_exit);
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int bsScrollNumber = bsScroll.getProgress();
                    int bsUpdateNumber = bsUpdate.getProgress();
                    SqlHelper.saveProcess(bsScrollNumber, bsUpdateNumber);
                    ToastUtil.showToast("保存成功！");
                    bannerView.setInterval(bsScrollNumber * 1000);
                    timer();
                    popupWindow.dismiss();
                }
            });
            btExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                   // showMessageDialog();
                }
            });
            popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        }
    }

    @Override
    public void onClick(View view) {
        //  showSetDialog();
        switch (view.getId()) {
            case R.id.iv_setview:
                popuListener();
                break;
        }
    }

    private void showMessageDialog() {
        new CommonDialog.Builder(this, 2)
                .setTitle("提示")
                .setMessage("确定要退出吗？")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void popuListener() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            setPopuStyle();
        }
    }

    private void showSetDialog() {
        if (setDialog == null) {
            setDialog = new Dialog(this);
            setDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setDialog.setContentView(R.layout.dialog_set_view);
            Window dialogWindow = setDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            lp.width = Util.dip2px(350);
            lp.height = Util.dip2px(250);
            lp.alpha = 1f;
            dialogWindow.setAttributes(lp);
            final BubbleSeekBar bsScroll = (BubbleSeekBar) setDialog.findViewById(R.id.bs_scroll);
            final BubbleSeekBar bsUpdate = (BubbleSeekBar) setDialog.findViewById(R.id.bs_update);
            bsScroll.setProgress(process[0]);
            bsUpdate.setProgress(process[1]);
            Button btSave = (Button) setDialog.findViewById(R.id.bt_save);
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int bsScrollNumber = bsScroll.getProgress();
                    int bsUpdateNumber = bsUpdate.getProgress();
                    SqlHelper.saveProcess(bsScrollNumber, bsUpdateNumber);
                    ToastUtil.showToast("保存成功！");
                    bannerView.setInterval(bsScrollNumber * 1000);
                    setDialog.dismiss();
                }
            });
        }
        setDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setDialog.show();

    }

    public static class BannerItem {
        public String image;
        public String title;

        @Override
        public String toString() {
            return title;
        }
    }

    public static class BannerViewFactory implements BannerView.ViewFactory<BannerItem> {
        @Override
        public View create(BannerItem item, int position, ViewGroup container) {
            ImageView iv = new ImageView(container.getContext());
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA);
            Glide.with(container.getContext().getApplicationContext())
                    .load(item.image)
                    .apply(options)
                    .into(iv);
            return iv;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (setDialog != null) {
            setDialog.dismiss();
            setDialog = null;
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
