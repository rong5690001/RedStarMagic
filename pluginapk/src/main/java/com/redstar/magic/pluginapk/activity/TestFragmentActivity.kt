package com.redstar.magic.pluginapk.activityimport android.os.Bundleimport com.redstar.magic.pluginapk.Rimport com.redstar.magic.pluginapk.fragment.BlankFragmentimport com.redstar.magic.pluginlib.components.activity.MagicActivityclass TestFragmentActivity : MagicActivity() {    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_test_fragment)        fragmentManager.beginTransaction().replace(R.id.fragment, BlankFragment()).commitAllowingStateLoss()    }}