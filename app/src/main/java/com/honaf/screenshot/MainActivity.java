package com.honaf.screenshot;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 截屏(包括dialog单独截屏)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String CAMERA_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    private Window window;
    private Handler handler = new Handler();
    private Button btn_shot, btn_shot_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    public void initView() {
        btn_shot = (Button) this.findViewById(R.id.btn_shot);
        btn_shot_all = (Button) this.findViewById(R.id.btn_shot_all);
    }

    public void initListener() {
        btn_shot.setOnClickListener(this);
        btn_shot_all.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_shot:
                showDialog();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //单独截屏弹框dialog
                        screenShotView(window.getDecorView());
                        //全屏但不包括弹框dialog
//                        screenShotView(MainActivity.this.getWindow().getDecorView());
                    }
                }, 1000);
                break;

            case R.id.btn_shot_all:
                screenShotAll();
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("等待1秒自动截屏dialog,请在相册目录下("+CAMERA_DIR+")查看");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        window = dialog.getWindow();
    }

    /**
     * 截屏到相册(全屏不包括弹框)
     */
    private void screenShotAll() {
        View view = getWindow().getDecorView().getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap temBitmap = view.getDrawingCache();
        File fileDir = new File(CAMERA_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String path = CAMERA_DIR + "/all_screen.png";
        try {
            FileOutputStream foStream = new FileOutputStream(path);
            temBitmap.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.flush();
            foStream.close();
            Toast.makeText(this,"截屏成功,请在相册目录下("+path+")查看",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.i("Show", e.toString());
        }
    }

    /**
     * 保存指定view的截图到相册
     */
    private void screenShotView(View view) {
        Bitmap temBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temBitmap);
        view.draw(canvas);
        File fileDir = new File(CAMERA_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String path = CAMERA_DIR + "/view_screen.png";
        try {
            FileOutputStream foStream = new FileOutputStream(path);
            temBitmap.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.flush();
            foStream.close();
            Toast.makeText(this,"截屏成功,请在相册目录下("+path+")查看",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.i("Show", e.toString());
        }
    }


}
