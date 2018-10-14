package co.sodalabs.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

class DemoActivity : AppCompatActivity() {

    private val mDisposablesOnCreate = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_demo_styled_recycler_view)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unbind view.
        mDisposablesOnCreate.clear()
    }
}
