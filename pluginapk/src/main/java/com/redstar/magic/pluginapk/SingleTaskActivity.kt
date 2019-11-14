package com.redstar.magic.pluginapk

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.redstar.magic.pluginlib.components.activity.MagicActivity

class SingleTaskActivity : MagicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singletask)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnStartSingleTask -> {
                startActivity(Intent(this@SingleTaskActivity, SingleTaskActivity::class.java))
            }
            R.id.btnStartSingleTop -> {
                startActivity(Intent(this@SingleTaskActivity, SingleTopActivity::class.java))
            }
            else -> {
            }
        }
    }
}
