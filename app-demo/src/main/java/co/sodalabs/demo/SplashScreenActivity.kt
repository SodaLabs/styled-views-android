package co.sodalabs.demo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()

        // Do nothing but wait few seconds and launch the start page.
        Observable
            .just(true)
            .delay(150, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this@SplashScreenActivity,
                    DemoActivity::class.java))
                finish()
            }
    }
}
