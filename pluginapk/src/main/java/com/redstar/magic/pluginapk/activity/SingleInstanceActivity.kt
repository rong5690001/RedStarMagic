package com.redstar.magic.pluginapk.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.redstar.magic.pluginapk.R
import com.redstar.magic.pluginlib.components.activity.MagicActivity

class SingleInstanceActivity : MagicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singleinstance)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnStartSingleTask -> {
                startActivity(Intent(this@SingleInstanceActivity, SingleInstanceActivity::class.java))
            }
            else -> {
            }
        }
    }
}
