package com.redstar.magic.pluginapk.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import androidx.databinding.Observable
import com.redstar.magic.pluginapk.R
import com.redstar.magic.pluginapk.service.ServiceTest
import com.redstar.magic.pluginapk.service.ServiceTest.IServiceTest
import com.redstar.magic.pluginlib.components.activity.MagicActivity

/**
 * service测试
 */
class ServiceTestActivity : MagicActivity() {

    var tvLog = lazy { findViewById<TextView>(R.id.tvLog) } as TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_test)
        tvLog.movementMethod = ScrollingMovementMethod.getInstance()
        ServiceTest.sMessage.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                tvLog.append(ServiceTest.sMessage.get() + "\n")
            }
        })
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnStartService -> {
                startService(Intent(this@ServiceTestActivity, ServiceTest::class.java))
            }
            R.id.btnBindService -> {
                bindService(Intent(this@ServiceTestActivity, ServiceTest::class.java), object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        val serviceTest = service as IServiceTest
                        tvLog.append(serviceTest.test())
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                    }

                }, BIND_AUTO_CREATE)
            }
            else -> {
            }
        }
    }
}
