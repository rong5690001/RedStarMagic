package com.redstar.magic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.redstar.magic.pluginlib.MagicPlugin;
import com.redstar.magic.pluginlib.PluginManager;
import com.redstar.magic.pluginlib.utils.FileUtils;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PluginManager.getInstance().init(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getCacheDir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                String apkPath = FileUtils.copyAssetAndWrite(MainActivity.this, "plugin.apk");
//                // 加载apk
//                PluginManager.getInstance().loadApk(apkPath);
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这里除了指定类名外 还能怎么获取要跳转的activity？怎么知道的？
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.redstar.magic.pluginapk"
                        , "com.redstar.magic.pluginapk.ChajianActivity"));
                MagicPlugin.startActivity(MainActivity.this, "plugin", intent);
            }
        });
    }
}
