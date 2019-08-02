package com.redstar.magic.pluginapkimport android.content.Contextimport android.os.Bundleimport android.widget.Toastimport com.redstar.magic.pluginlib.components.activity.MagicActivityclass PluginLifeCycleActivity : MagicActivity() {    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_plugin_internal)        makeTextShort("onCreate")    }    override fun onSaveInstanceState(outState: Bundle?) {        super.onSaveInstanceState(outState)        makeTextShort("onSaveInstanceState")    }    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {        super.onRestoreInstanceState(savedInstanceState)        makeTextShort("onSaveInstanceState")    }    override fun onResume() {        super.onResume()        makeTextShort("onResume")    }    override fun onStop() {        super.onStop()        makeTextShort("onStop")    }    override fun onDestroy() {        super.onDestroy()        makeTextShort("onDestroy")    }}fun Context.makeTextShort(value: String) {    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()}