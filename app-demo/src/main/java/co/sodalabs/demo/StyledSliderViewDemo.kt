package co.sodalabs.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import co.sodalabs.view.IStyledSliderListener
import kotlinx.android.synthetic.main.activity_demo_styled_slider.buttonAddSlMarker
import kotlinx.android.synthetic.main.activity_demo_styled_slider.buttonAddSlNormal
import kotlinx.android.synthetic.main.activity_demo_styled_slider.buttonSubtractSlMarker
import kotlinx.android.synthetic.main.activity_demo_styled_slider.buttonSubtractSlNormal
import kotlinx.android.synthetic.main.activity_demo_styled_slider.labelSlMarker
import kotlinx.android.synthetic.main.activity_demo_styled_slider.labelSlNormal
import kotlinx.android.synthetic.main.activity_demo_styled_slider.slMarker
import kotlinx.android.synthetic.main.activity_demo_styled_slider.slNormal
import kotlinx.android.synthetic.main.activity_demo_styled_switch.btClose

class StyledSliderViewDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_demo_styled_slider)

        btClose.setOnClickListener {
            onBackPressed()
        }

        buttonAddSlNormal.setOnClickListener {
            slNormal.max += 10
        }

        buttonSubtractSlNormal.setOnClickListener {
            if (slNormal.max > 0) {
                slNormal.max -= 10
            }
        }

        buttonAddSlMarker.setOnClickListener {
            slMarker.max += 1
        }

        buttonSubtractSlMarker.setOnClickListener {
            if (slMarker.max > 0) {
                slMarker.max -= 1
            }
        }

        updateNormalSeekbarLabels(slNormal)
        updateMarkerSeekbarLabels(slMarker)

        slNormal.setOnSeekBarChangeListener(object : IStyledSliderListener {
            override fun onMarkerLevelUpdated(seekBar: SeekBar, markerLevel: Int) {
                println("onMarkerLevelUpdated, markerLevel=$markerLevel")
                updateNormalSeekbarLabels(seekBar)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                println("onProgressChanged, progress=$progress")
                updateNormalSeekbarLabels(seekBar)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                println("onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                println("onStopTrackingTouch")
            }
        })

        slMarker.setOnSeekBarChangeListener(object : IStyledSliderListener {
            override fun onMarkerLevelUpdated(seekBar: SeekBar, markerLevel: Int) {
                println("onMarkerLevelUpdated, markerLevel=$markerLevel")
                updateMarkerSeekbarLabels(seekBar)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                println("onProgressChanged, progress=$progress")
                updateMarkerSeekbarLabels(seekBar)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                println("onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                println("onStopTrackingTouch")
            }
        })
    }

    fun updateNormalSeekbarLabels(seekBar: SeekBar) {
        labelSlNormal.text = String.format("Current/Max - ${seekBar.progress}/${seekBar.max}")
    }

    fun updateMarkerSeekbarLabels(seekBar: SeekBar) {
        labelSlMarker.text = String.format("Current/Max - ${seekBar.progress}/${seekBar.max}")
    }
}