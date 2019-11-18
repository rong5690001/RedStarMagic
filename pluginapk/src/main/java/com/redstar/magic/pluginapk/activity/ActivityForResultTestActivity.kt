package com.redstar.magic.pluginapk.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.redstar.magic.pluginapk.R
import com.redstar.magic.pluginlib.components.activity.MagicActivity

/**
 * activityForResult测试页面
 */
class ActivityForResultTestActivity : MagicActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_result_test)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnForResult -> {
                val intent = Intent()
                val et = findViewById<EditText>(R.id.et)
                intent.putExtra("data", et.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else -> {

            }
        }
    }
}
