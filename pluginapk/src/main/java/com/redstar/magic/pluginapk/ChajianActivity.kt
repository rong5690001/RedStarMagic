package com.redstar.magic.pluginapk

import android.content.Intent
import android.os.Bundle
import android.widget.Button

import com.redstar.magic.pluginlib.MagicActivity

class ChajianActivity : MagicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chajian)
        findViewById<Button>(R.id.btnLaunchPluginActivity).setOnClickListener {
            startActivity(Intent(this@ChajianActivity, PluginInternalActivity::class.java))
        }
    }
}
