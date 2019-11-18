package com.redstar.magic.pluginapk.serviceimport android.content.Intentimport android.os.Binderimport android.os.IBinderimport androidx.databinding.ObservableFieldimport com.redstar.magic.pluginapk.activity.makeTextShortimport com.redstar.magic.pluginlib.components.service.MagicServiceclass ServiceTest : MagicService() {    companion object {        val sMessage = ObservableField<String>()    }    override fun onBind(intent: Intent): IBinder? {        sendMessage("onBind")        return ServiceBinder()    }    override fun onUnbind(intent: Intent?): Boolean {        sendMessage("onUnbind")        return super.onUnbind(intent)    }    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {        sendMessage("onStartCommand")        return super.onStartCommand(intent, flags, startId)    }    override fun onCreate() {        super.onCreate()        sendMessage("onCreate")    }    override fun onDestroy() {        sendMessage("onDestroy")        super.onDestroy()    }    override fun onRebind(intent: Intent?) {        super.onRebind(intent)        sendMessage("onRebind")    }    private fun sendMessage(message: String) {        makeTextShort(message)        sMessage.set("ServiceTest: $message")    }    class ServiceBinder : Binder(), IServiceTest {        override fun test(): String {            return "i'm ServiceBinder test method"        }    }    interface IServiceTest {        fun test() : String    }}