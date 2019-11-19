package com.redstar.magic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;

import com.redstar.magic.pluginlib.MagicPlugin;
import com.redstar.magic.pluginlib.tools.Logger;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


public class MainActivity extends Activity {

    boolean layout_main = true;
    ConstraintSet constraintSet1 = new ConstraintSet();
    ConstraintSet constraintSet2 = new ConstraintSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MagicPlugin.install(this);
        Logger.debug(true);

        //TODO 演示ConstrainsSet
        final ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        constraintSet1.clone(constraintLayout);
        constraintSet2.clone(this, R.layout.activity_main2);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {///data/data/com.redstar.magic/cache
                    getCacheDir().delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    if (layout_main) {
                        constraintSet2.applyTo(constraintLayout);
                    } else {
                        constraintSet1.applyTo(constraintLayout);
                    }
                    layout_main = !layout_main;
                }
//                String apkPath = FileUtils.copyAssetToFile(MainActivity.this, "plugin.apk");
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
                        , "com.redstar.magic.pluginapk.activity.ChajianActivity"));
//                intent.setComponent(new ComponentName("com.redstar"
//                        , "com.redstar.magic.pluginapk.activity.ChajianActivity"));
                MagicPlugin.startActivity2(MainActivity.this, intent);
            }
        });


//        String aaa = new String();
//        Log.v("sdfddd", "1  "+aaa.getClass().getClassLoader().toString());
//        View v = new View(this);
//        Log.v("sdfddd", "2  "+v.getClass().getClassLoader().toString());
//        PackageManager pm=getPackageManager();
//        Log.v("sdfddd", "3  "+pm.getClass().getClassLoader().toString());
//        Log.v("sdfddd", "4 "+this.getClass().getClassLoader().toString());
//        Context ss=this.getBaseContext();
//        Log.v("sdfddd", "5  "+ss.getClass().getClassLoader().toString());
    }
}
