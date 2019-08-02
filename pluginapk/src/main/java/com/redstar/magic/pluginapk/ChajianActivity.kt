package com.redstar.magic.pluginapk

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.redstar.magic.pluginlib.components.activity.MagicActivity
import kotlinx.android.synthetic.main.activity_chajian.*

class ChajianActivity : MagicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chajian)
        //启动插件内部Activity
        btnLaunchPluginActivity.setOnClickListener {
            startActivity(Intent(this@ChajianActivity, PluginLifeCycleActivity::class.java))
        }
        //显示弹窗
        btnShowDialog.setOnClickListener {
            AlertDialog.Builder(this@ChajianActivity)
                    .setTitle("title")
                    .setMessage("我是插件里的弹窗")
                    .setPositiveButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("确定") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
        }
        //dataBinding测试
        btnDataBinding.setOnClickListener {
            startActivity(Intent(this@ChajianActivity, DatabindingActivity::class.java))
        }
    }

}
