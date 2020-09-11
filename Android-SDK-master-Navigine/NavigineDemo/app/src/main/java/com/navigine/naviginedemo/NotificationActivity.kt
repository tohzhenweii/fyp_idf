package com.navigine.naviginedemo

import com.navigine.naviginesdk.*
import android.text.method.ScrollingMovementMethod
import android.view.View.*
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class NotificationActivity : Activity() {
    // GUI parameters
    private var mTitleLabel: TextView? = null
    private var mTextLabel: TextView? = null
    private var mId = 0
    private var mName = ""
    private var mUuid = ""

    /** Called when the activity is first created  */
    @Override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_notification)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        mId = getIntent().getIntExtra("zone_id", 0)
        mUuid = getIntent().getStringExtra("zone_uuid")
        mName = getIntent().getStringExtra("zone_name")
        mTitleLabel = findViewById(R.id.notification__title_label) as TextView?
        mTextLabel = findViewById(R.id.notification__text_label) as TextView?
        mTextLabel.setText("You have entered zone '$mName'")
    }

    fun onClose(v: View?) {
        finish()
    }
}