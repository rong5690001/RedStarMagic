package com.redstar.magic.pluginapk.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.redstar.magic.pluginapk.R
import com.redstar.magic.pluginlib.components.activity.MagicActivity

class ChajianActivity : MagicActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chajian)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            //activity相关测试
            R.id.btnActivityTest -> {
                startActivity(Intent(this@ChajianActivity, ActivityTestActivity::class.java))
            }
            //service相关测试
            R.id.btnServiceTest -> {
                startActivity(Intent(this@ChajianActivity, ServiceTestActivity::class.java))
            }
            //显示弹窗
            R.id.btnShowDialog -> {
                showDialog()
            }
            //dataBinding测试
            R.id.btnDataBinding -> {
                startActivity(Intent(this@ChajianActivity, DatabindingActivity::class.java))
            }
            //fragment测试
            R.id.btnFragment -> {
                startActivity(Intent(this@ChajianActivity, TestFragmentActivity::class.java))
            }
            else -> {
            }
        }
    }

    private fun showDialog() {
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

}