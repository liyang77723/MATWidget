package cn.meiauto.matwidget.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import cn.meiauto.matwidget.image.ZoomImageView;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/3/31
 */
public class ZoomImageViewActivity extends AppCompatActivity {

    private static final String EXTRA_PATH = "path";
    private static final String EXTRA_URL = "url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        //todo

        ZoomImageView zoomImageView = new ZoomImageView(this);
        setContentView(zoomImageView);
    }

    public static void openFilePath(Activity activity, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            throw new NullPointerException("image path is null");
        }
        Intent intent = new Intent(activity, ZoomImageViewActivity.class);
        intent.putExtra(EXTRA_PATH, imagePath);
        activity.startActivity(intent);
    }

    public static void openFile(Activity activity, File imageFile) {
        if (imageFile == null) {
            throw new NullPointerException("image file is null");
        }
        openFilePath(activity, imageFile.getAbsolutePath());
    }

    public static void openUrl(Activity activity, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            throw new NullPointerException("image url is null");
        }
        Intent intent = new Intent(activity, ZoomImageViewActivity.class);
        intent.putExtra(EXTRA_URL, imageUrl);
        activity.startActivity(intent);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
