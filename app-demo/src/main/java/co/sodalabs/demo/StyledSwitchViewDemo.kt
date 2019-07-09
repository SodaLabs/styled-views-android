package co.sodalabs.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo_styled_switch.btClose
import kotlinx.android.synthetic.main.activity_demo_styled_switch.cbDisable
import kotlinx.android.synthetic.main.activity_demo_styled_switch.swSwitch

class StyledSwitchViewDemo : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_styled_switch)
        btClose.setOnClickListener {
            onBackPressed()
        }
        cbDisable.setOnCheckedChangeListener { _, isChecked ->
            swSwitch.isEnabled = !isChecked
        }
    }
}