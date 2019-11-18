package com.redstar.magic.pluginapk.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.redstar.magic.pluginapk.R
import com.redstar.magic.pluginlib.components.activity.MagicActivity

/**
 * activity相关测试页面
 */
class ActivityTestActivity : MagicActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_test)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            //启动插件内部Activity
            R.id.btnLaunchPluginActivity -> {
                startActivity(Intent(this@ActivityTestActivity, PluginLifeCycleActivity::class.java))
            }
            //启动singleTopActivity
            R.id.btnStartSingleTop -> {
                startActivity(Intent(this@ActivityTestActivity, SingleTopActivity::class.java))
            }
            //启动singleTaskActivity
            R.id.btnStartSingleTask -> {
                startActivity(Intent(this@ActivityTestActivity, SingleTaskActivity::class.java))
            }
            //启动singleInstanceActivity
            R.id.btnStartSingleInstance -> {
                startActivity(Intent(this@ActivityTestActivity, SingleInstanceActivity::class.java))
            }
            //启动ForResultActivity
            R.id.btnStartActivityForResult -> {
                startActivityForResult(Intent(this@ActivityTestActivity, ActivityForResultTestActivity::class.java), 1000)
            }
            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1000 -> {
                    data?.getStringExtra("data")?.let { makeTextShort("for result data:$it") }
                }
                else -> {
                }
            }
        }
    }
}
