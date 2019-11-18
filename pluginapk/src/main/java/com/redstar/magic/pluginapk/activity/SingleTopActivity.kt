package com.redstar.magic.pluginapk.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.redstar.magic.pluginapk.R
import com.redstar.magic.pluginlib.components.activity.MagicActivity

class SingleTopActivity : MagicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singletop)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnStartSingleTop -> {
                startActivity(Intent(this@SingleTopActivity, SingleTopActivity::class.java))
            }
            R.id.btnStartSingleTask -> {
                startActivity(Intent(this@SingleTopActivity, SingleTaskActivity::class.java))
            }
            else -> {
            }
        }
    }
}
