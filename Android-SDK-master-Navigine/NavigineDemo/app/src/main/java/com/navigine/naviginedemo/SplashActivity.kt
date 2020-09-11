package com.navigine.naviginedemo

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import com.navigine.naviginesdk.*
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class SplashActivity : Activity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private val mContext: Context = this
    private var mStatusLabel: TextView? = null

    @Override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setting up NavigineSDK parameters
        NavigineSDK.setParameter(mContext, "debug_level", 2)
        NavigineSDK.setParameter(mContext, "actions_updates_enabled", false)
        NavigineSDK.setParameter(mContext, "location_updates_enabled", true)
        NavigineSDK.setParameter(mContext, "location_loader_timeout", 60)
        NavigineSDK.setParameter(mContext, "location_update_timeout", 300)
        NavigineSDK.setParameter(mContext, "location_retry_timeout", 300)
        NavigineSDK.setParameter(mContext, "post_beacons_enabled", true)
        NavigineSDK.setParameter(mContext, "post_messages_enabled", true)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        mStatusLabel = findViewById(R.id.splash__status_label) as TextView?
        ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
    }

    @Override
    fun onBackPressed() {
        moveTaskToBack(true)
    }

    @Override
    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String?>?,
                                   grantResults: IntArray?) {
        val permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) === PackageManager.PERMISSION_GRANTED
        val permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED
        when (requestCode) {
            101 -> if (!permissionLocation || D.WRITE_LOGS && !permissionStorage) finish() else {
                if (NavigineSDK.initialize(mContext, D.USER_HASH, D.SERVER_URL)) {
                    NavigineSDK.loadLocationInBackground(D.LOCATION_NAME, 30,
                            object : LoadListener() {
                                @Override
                                fun onFinished() {
                                    val intent = Intent(mContext, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    mContext.startActivity(intent)
                                }

                                @Override
                                fun onFailed(error: Int) {
                                    mStatusLabel.setText("Error downloading location 'Navigine Demo' (error " + error + ")! " +
                                            "Please, try again later or contact technical support")
                                }

                                @Override
                                fun onUpdate(progress: Int) {
                                    mStatusLabel.setText("Downloading location: $progress%")
                                }
                            })
                } else {
                    mStatusLabel.setText("Error initializing NavigineSDK! Please, contact technical support")
                }
            }
        }
    }

    companion object {
        private const val TAG = "NAVIGINE.Demo"
    }
}