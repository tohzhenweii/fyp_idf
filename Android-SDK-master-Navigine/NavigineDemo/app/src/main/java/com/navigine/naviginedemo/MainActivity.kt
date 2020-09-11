package com.navigine.naviginedemo

import android.graphics.drawable.Icon
import android.view.View.*
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.navigine.naviginesdk.*
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class MainActivity : Activity() {
    var mydb: MyDbAdapter? = null

    // NavigationThread instance
    private var mNavigation: NavigationThread? = null

    // UI Parameters
    private var mLocationView: LocationView? = null
    private var mPrevFloorButton: Button? = null
    private var mNextFloorButton: Button? = null
    private val search_button: Button? = null
    private val LVenue: Spinner? = null
    var text1: String? = null
    var text2: String? = null
    var cat: String? = null
    var VenueList: List<String> = ArrayList<String>()
    private var mBackView: View? = null
    private var mNextRoute: View? = null
    private var mMap: View? = null
    private var mPrevFloorView: View? = null
    private var mNextFloorView: View? = null
    private var mZoomInView: View? = null
    private var mZoomOutView: View? = null
    private var mAdjustModeView: View? = null
    private var mCurrentFloorLabel: TextView? = null
    private var mErrorMessageLabel: TextView? = null
    private val mHandler: Handler = Handler()
    private var mDisplayDensity = 0.0f
    private var mAdjustMode = false
    private var mAdjustTime: Long = 0

    // Location parameters
    private var mLocation: Location? = null
    private var mCurrentSubLocationIndex = -1

    // Device parameters
    private var mDeviceInfo: DeviceInfo? = null // Current device
    private var mPinPoint: LocationPoint? = null // Potential device target
    private var mTargetPoint: LocationPoint? = null // Current device target
    private var mPinPointRect: RectF? = null
    private var mVenueBitmap: Bitmap? = null
    private var mTargetVenue: Venue? = null
    private var mSelectedVenue: Venue? = null
    private var mSelectedVenueRect: RectF? = null
    private var mSelectedZone: Zone? = null

    // My codes
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        cat = getIntent().getStringExtra("cat")
        Log.d(TAG, "MainActivity started")
        //My codes for Venue List
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        //My codes for dropdown list
        mydb = MyDbAdapter(this)


/*
    LVenue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+VenueList.get(position) ,Toast.LENGTH_SHORT).show();
      }
    });
*/


        //Ends here
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // Setting up GUI parameters
        mBackView = findViewById(R.id.navigation__back_view) as View?
        mNextRoute = findViewById(R.id.Next_destination) as View?
        mMap = findViewById(R.id.navigation__map_view) as View?
        mPrevFloorButton = findViewById(R.id.navigation__prev_floor_button) as Button?
        mNextFloorButton = findViewById(R.id.navigation__next_floor_button) as Button?
        mPrevFloorView = findViewById(R.id.navigation__prev_floor_view) as View?
        mNextFloorView = findViewById(R.id.navigation__next_floor_view) as View?
        mCurrentFloorLabel = findViewById(R.id.navigation__current_floor_label) as TextView?
        mZoomInView = findViewById(R.id.navigation__zoom_in_view) as View?
        mZoomOutView = findViewById(R.id.navigation__zoom_out_view) as View?
        mAdjustModeView = findViewById(R.id.navigation__adjust_mode_view) as View?
        mErrorMessageLabel = findViewById(R.id.navigation__error_message_label) as TextView?
        mBackView.setVisibility(View.INVISIBLE)
        mNextRoute.setVisibility(View.INVISIBLE)
        mMap.setVisibility(View.INVISIBLE)
        mPrevFloorView.setVisibility(View.INVISIBLE)
        mNextFloorView.setVisibility(View.INVISIBLE)
        mCurrentFloorLabel.setVisibility(View.INVISIBLE)
        mZoomInView.setVisibility(View.INVISIBLE)
        mZoomOutView.setVisibility(View.INVISIBLE)
        mAdjustModeView.setVisibility(View.INVISIBLE)
        mErrorMessageLabel.setVisibility(View.GONE)
        mVenueBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elm_venue)

        // Initializing location view
        mLocationView = findViewById(R.id.navigation__location_view) as LocationView?
        mLocationView.setBackgroundColor(-0x141415)
        mLocationView.setListener(
                object : Listener() {
                    @Override
                    fun onClick(x: Float, y: Float) {
                        handleClick(x, y)
                    }

                    @Override
                    fun onLongClick(x: Float, y: Float) {
                        handleLongClick(x, y)
                    }

                    @Override
                    fun onScroll(x: Float, y: Float, byTouchEvent: Boolean) {
                        handleScroll(x, y, byTouchEvent)
                    }

                    @Override
                    fun onZoom(ratio: Float, byTouchEvent: Boolean) {
                        handleZoom(ratio, byTouchEvent)
                    }

                    @Override
                    fun onDraw(canvas: Canvas) {
                        drawZones(canvas)
                        drawPoints(canvas)
                        drawVenues(canvas)
                        drawDevice(canvas)
                    }
                }
        )

        // Loading map only when location view size is known
        mLocationView.addOnLayoutChangeListener(
                object : OnLayoutChangeListener() {
                    @Override
                    fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        val width = right - left
                        val height = bottom - top
                        if (width == 0 || height == 0) return
                        Log.d(TAG, "Layout chaged: " + width + "x" + height)
                        val oldWidth = oldRight - oldLeft
                        val oldHeight = oldBottom - oldTop
                        if (oldWidth != width || oldHeight != height) loadMap()
                    }
                }
        )
        mDisplayDensity = getResources().getDisplayMetrics().density
        mNavigation = NavigineSDK.getNavigation()

        // Setting up device listener
        if (mNavigation != null) {
            mNavigation.setDeviceListener(
                    object : Listener() {
                        @Override
                        fun onUpdate(info: DeviceInfo) {
                            handleDeviceUpdate(info)
                        }
                    }
            )
        }

        /*// Setting up zone listener
    if (mNavigation != null)
    {
      mNavigation.setZoneListener
      (
        new Zone.Listener()
        {
          @RequiresApi(api = Build.VERSION_CODES.O)
          @Override public void onEnterZone(Zone z) { handleEnterZone(z); }
          //@Override public void onLeaveZone(Zone z) { handleLeaveZone(z); }
        }
      );
    }*/if (NOTIFICATIONS_ENABLED) {
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= 26) notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL, "default",
                    NotificationManager.IMPORTANCE_LOW))
        }
    }

    @Override
    fun onDestroy() {
        if (mNavigation != null) {
            NavigineSDK.finish()
            mNavigation = null
        }
        super.onDestroy()
    }

    @Override
    fun onBackPressed() {
        moveTaskToBack(true)
    }

    fun toggleAdjustMode(v: View?) {
        mAdjustMode = !mAdjustMode
        mAdjustTime = 0
        val adjustModeButton: Button = findViewById(R.id.navigation__adjust_mode_button) as Button
        adjustModeButton.setBackgroundResource(if (mAdjustMode) R.drawable.btn_adjust_mode_on else R.drawable.btn_adjust_mode_off)
        mLocationView.redraw()
    }

    fun onNextFloor(v: View?) {
        if (loadNextSubLocation()) mAdjustTime = System.currentTimeMillis() + ADJUST_TIMEOUT
    }

    fun onPrevFloor(v: View?) {
        if (loadPrevSubLocation()) mAdjustTime = System.currentTimeMillis() + ADJUST_TIMEOUT
    }

    fun onZoomIn(v: View?) {
        mLocationView.zoomBy(1.25f)
    }

    fun onZoomOut(v: View?) {
        mLocationView.zoomBy(0.8f)
    }

    fun onMakeRoute(v: View?) {
        if (mNavigation == null) return
        if (mPinPoint == null) return
        mTargetPoint = mPinPoint
        mTargetVenue = null
        mPinPoint = null
        mPinPointRect = null
        mNavigation.setTarget(mTargetPoint)
        mBackView.setVisibility(View.VISIBLE)
        mLocationView.redraw()
    }

    fun onCancelRoute(v: View?) {
        if (mNavigation == null) return
        mTargetPoint = null
        mTargetVenue = null
        mPinPoint = null
        mPinPointRect = null
        mNavigation.cancelTargets()
        mBackView.setVisibility(View.GONE)
        mLocationView.redraw()
    }

    private fun handleClick(x: Float, y: Float) {
        Log.d(TAG, String.format(Locale.ENGLISH, "Click at (%.2f, %.2f)", x, y))
        if (mLocation == null || mCurrentSubLocationIndex < 0) return
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return
        if (mPinPoint != null) {
            if (mPinPointRect != null && mPinPointRect.contains(x, y)) {
                mTargetPoint = mPinPoint
                mTargetVenue = null
                mPinPoint = null
                mPinPointRect = null
                mNavigation.setTarget(mTargetPoint)
                mBackView.setVisibility(View.VISIBLE)
                return
            }
            cancelPin()
            return
        }
        if (mSelectedVenue != null) {
            if (mSelectedVenueRect != null && mSelectedVenueRect.contains(x, y)) {
                mTargetVenue = mSelectedVenue
                mTargetPoint = null
                mNavigation.setTarget(LocationPoint(mLocation.getId(), subLoc.getId(), mTargetVenue.getX(), mTargetVenue.getY()))
                mBackView.setVisibility(View.VISIBLE)
            }
            cancelVenue()
            return
        }

        // Check if we touched venue
        mSelectedVenue = getVenueAt(x, y)
        mSelectedVenueRect = RectF()

        // Check if we touched zone
        if (mSelectedVenue == null) {
            val Z: Zone? = getZoneAt(x, y)
            if (Z != null) mSelectedZone = if (mSelectedZone === Z) null else Z
        }
        mLocationView.redraw()
    }

    private fun handleLongClick(x: Float, y: Float) {
        Log.d(TAG, String.format(Locale.ENGLISH, "Long click at (%.2f, %.2f)", x, y))
        makePin(mLocationView.getAbsCoordinates(x, y))
        cancelVenue()
    }

    private fun handleScroll(x: Float, y: Float, byTouchEvent: Boolean) {
        if (byTouchEvent) mAdjustTime = NavigineSDK.currentTimeMillis() + ADJUST_TIMEOUT
    }

    private fun handleZoom(ratio: Float, byTouchEvent: Boolean) {
        if (byTouchEvent) mAdjustTime = NavigineSDK.currentTimeMillis() + ADJUST_TIMEOUT
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun handleEnterZone(z: Zone) {
        Log.d(TAG, "Enter zone " + z.getName())
        if (NOTIFICATIONS_ENABLED) {
            val notificationIntent = Intent(this, NotificationActivity::class.java)
            notificationIntent.putExtra("zone_id", z.getId())
            notificationIntent.putExtra("zone_name", z.getName())
            notificationIntent.putExtra("zone_color", z.getColor())
            notificationIntent.putExtra("zone_alias", z.getAlias())

            // Setting up a notification
            val notificationBuilder: Notification.Builder = Builder(this, NOTIFICATION_CHANNEL)
            notificationBuilder.setContentIntent(PendingIntent.getActivity(this, z.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            notificationBuilder.setContentTitle("New zone")
            notificationBuilder.setContentText("You have entered zone '" + z.getName().toString() + "'")
            notificationBuilder.setSmallIcon(R.drawable.elm_logo)
            notificationBuilder.setAutoCancel(true)

            // Posting a notification
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(z.getId(), notificationBuilder.build())
        }
    }

    /*  private void handleLeaveZone(Zone z)
  {
    Log.d(TAG, "Leave zone " + z.getName());
    if (NOTIFICATIONS_ENABLED)
    {
      NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.cancel(z.getId());
    }
  }*/
    private fun handleDeviceUpdate(deviceInfo: DeviceInfo) {
        mDeviceInfo = deviceInfo
        if (mDeviceInfo == null) return

        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0) return
        if (mDeviceInfo.isValid()) {
            cancelErrorMessage()
            mBackView.setVisibility(if (mTargetPoint != null || mTargetVenue != null) View.VISIBLE else View.GONE)
            if (mAdjustMode) adjustDevice()
        } else {
            mBackView.setVisibility(View.GONE)
            when (mDeviceInfo.getErrorCode()) {
                4 -> setErrorMessage("You are out of navigation zone! Please, check that your bluetooth is enabled!")
                else -> setErrorMessage(String.format(Locale.ENGLISH,
                        "Something is wrong with location '%s' (error code %d)! " +
                                "Please, contact technical support!",
                        mLocation.getName(), mDeviceInfo.getErrorCode()))
            }
        }

        // This causes map redrawing
        mLocationView.redraw()
    }

    private fun setErrorMessage(message: String) {
        mErrorMessageLabel.setText(message)
        mErrorMessageLabel.setVisibility(View.VISIBLE)
    }

    private fun cancelErrorMessage() {
        mErrorMessageLabel.setVisibility(View.GONE)
    }

    private fun loadMap(): Boolean {
        if (mNavigation == null) {
            Log.e(TAG, "Can't load map! Navigine SDK is not available!")
            return false
        }
        mLocation = mNavigation.getLocation()
        mCurrentSubLocationIndex = -1
        if (mLocation == null) {
            Log.e(TAG, "Loading map failed: no location")
            return false
        }
        if (mLocation.getSubLocations().size() === 0) {
            Log.e(TAG, "Loading map failed: no sublocations")
            mLocation = null
            return false
        }
        if (!loadSubLocation(0)) {
            Log.e(TAG, "Loading map failed: unable to load default sublocation")
            mLocation = null
            return false
        }
        if (mLocation.getSubLocations().size() >= 2) {
            mPrevFloorView.setVisibility(View.VISIBLE)
            mNextFloorView.setVisibility(View.VISIBLE)
            mCurrentFloorLabel.setVisibility(View.VISIBLE)
        }
        mZoomInView.setVisibility(View.VISIBLE)
        mZoomOutView.setVisibility(View.VISIBLE)
        mAdjustModeView.setVisibility(View.VISIBLE)
        mNavigation.setMode(NavigationThread.MODE_NORMAL)
        if (D.WRITE_LOGS) mNavigation.setLogFile(getLogFile("log"))
        mLocationView.redraw()
        //Priya's Code
        /*
    SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);
    Log.d(TAG, String.format(Locale.ENGLISH, "%s ",mCurrentSubLocationIndex));
    for(int i = 0; i < subLoc.getVenues().size(); i++)
    {
      Venue ven = subLoc.getVenues().get(i);
        VenueList.add(ven.getName());
    }
    Set<String> NewVenue = new HashSet<>(VenueList);
    VenueList.clear();
    VenueList.addAll(NewVenue);

     */text1 = getIntent().getStringExtra("Position1")
        text2 = getIntent().getStringExtra("Position2")
        if (text1 !== "---Select---") {
            Search()
        }
        return true
    }

    private fun loadSubLocation(index: Int): Boolean {
        if (mNavigation == null) return false
        if (mLocation == null || index < 0 || index >= mLocation.getSubLocations().size()) return false
        val subLoc: SubLocation = mLocation.getSubLocations().get(index)
        Log.d(TAG, String.format(Locale.ENGLISH, "Loading sublocation %s (%.2f x %.2f)", subLoc.getName(), subLoc.getWidth(), subLoc.getHeight()))
        if (subLoc.getWidth() < 1.0f || subLoc.getHeight() < 1.0f) {
            Log.e(TAG, String.format(Locale.ENGLISH, "Loading sublocation failed: invalid size: %.2f x %.2f", subLoc.getWidth(), subLoc.getHeight()))
            return false
        }
        if (!mLocationView.loadSubLocation(subLoc)) {
            Log.e(TAG, "Loading sublocation failed: invalid image")
            return false
        }
        val viewWidth: Float = mLocationView.getWidth()
        val viewHeight: Float = mLocationView.getHeight()
        val minZoomFactor: Float = Math.min(viewWidth / subLoc.getWidth(), viewHeight / subLoc.getHeight())
        val maxZoomFactor: Float = LocationView.ZOOM_FACTOR_MAX
        mLocationView.setZoomRange(minZoomFactor, maxZoomFactor)
        mLocationView.setZoomFactor(minZoomFactor)
        Log.d(TAG, String.format(Locale.ENGLISH, "View size: %.1f x %.1f", viewWidth, viewHeight))
        mAdjustTime = 0
        mCurrentSubLocationIndex = index
        mCurrentFloorLabel.setText(String.format(Locale.ENGLISH, "%d", mCurrentSubLocationIndex))
        if (mCurrentSubLocationIndex > 0) {
            mPrevFloorButton.setEnabled(true)
            mPrevFloorView.setBackgroundColor(Color.parseColor("#90aaaaaa"))
        } else {
            mPrevFloorButton.setEnabled(false)
            mPrevFloorView.setBackgroundColor(Color.parseColor("#90dddddd"))
        }
        if (mCurrentSubLocationIndex + 1 < mLocation.getSubLocations().size()) {
            mNextFloorButton.setEnabled(true)
            mNextFloorView.setBackgroundColor(Color.parseColor("#90aaaaaa"))
        } else {
            mNextFloorButton.setEnabled(false)
            mNextFloorView.setBackgroundColor(Color.parseColor("#90dddddd"))
        }
        cancelVenue()
        mLocationView.redraw()
        return true
    }

    private fun loadNextSubLocation(): Boolean {
        return if (mLocation == null || mCurrentSubLocationIndex < 0) false else loadSubLocation(mCurrentSubLocationIndex + 1)
    }

    private fun loadPrevSubLocation(): Boolean {
        return if (mLocation == null || mCurrentSubLocationIndex < 0) false else loadSubLocation(mCurrentSubLocationIndex - 1)
    }

    private fun makePin(P: PointF) {
        if (mLocation == null || mCurrentSubLocationIndex < 0) return
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return
        if (P.x < 0.0f || P.x > subLoc.getWidth() || P.y < 0.0f || P.y > subLoc.getHeight()) {
            // Missing the map
            return
        }
        if (mTargetPoint != null || mTargetVenue != null) return
        if (mDeviceInfo == null || !mDeviceInfo.isValid()) return
        mPinPoint = LocationPoint(mLocation.getId(), subLoc.getId(), P.x, P.y)
        mPinPointRect = RectF()
        mLocationView.redraw()
    }

    private fun cancelPin() {
        if (mLocation == null || mCurrentSubLocationIndex < 0) return
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return
        if (mTargetPoint != null || mTargetVenue != null || mPinPoint == null) return
        mPinPoint = null
        mPinPointRect = null
        mLocationView.redraw()
    }

    private fun cancelVenue() {
        mSelectedVenue = null
        mLocationView.redraw()
    }

    private fun getVenueAt(x: Float, y: Float): Venue? {
        if (mLocation == null || mCurrentSubLocationIndex < 0) return null
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return null
        var v0: Venue? = null
        var d0 = 1000.0f
        for (i in 0 until subLoc.getVenues().size()) {
            val v: Venue = subLoc.getVenues().get(i)
            val P: PointF = mLocationView.getScreenCoordinates(v.getX(), v.getY())
            val d: Float = Math.abs(x - P.x) + Math.abs(y - P.y)
            if (d < 30.0f * mDisplayDensity && d < d0) {
                v0 = v
                d0 = d
            }
        }
        return v0
    }

    fun Search() {
        //text = "L306";
        val subLoc: SubLocation = mLocation.getSubLocations().get(0)
        for (i in 0 until subLoc.getVenues().size()) {
            val ven: Venue = subLoc.getVenues().get(i)
            Log.d(TAG, String.format(Locale.ENGLISH, " %s == %s ", ven.getName(), text1))
            //Log.d(TAG, String.format(Locale.ENGLISH, " %s == %s ", ven.getCategory().getName(), text1));
            if (ven.getName().equals(text1)) {
                Log.d(TAG, String.format(Locale.ENGLISH, "Got it"))
                Log.d(TAG, String.format(Locale.ENGLISH, "Click at (%.2f, %.2f)", ven.getX(), ven.getY()))
                //
                handleClick(ven.getX(), ven.getY())
                mSelectedVenue = subLoc.getVenues().get(i)
                mTargetVenue = mSelectedVenue
                mNavigation.setTarget(LocationPoint(mLocation.getId(), subLoc.getId(), mTargetVenue.getX(), mTargetVenue.getY()))
                if (text2!!.equals("---Select---")) {
                    continue
                } else {
                    mNextRoute.setVisibility(View.VISIBLE)
                }
            }
        }
        return
    }

    fun onNextRoute(v: View?) {
        //text = "L306";
        Log.d(TAG, String.format(Locale.ENGLISH, text1))
        val subLoc: SubLocation = mLocation.getSubLocations().get(0)
        for (i in 0 until subLoc.getVenues().size()) {
            val ven: Venue = subLoc.getVenues().get(i)
            Log.d(TAG, String.format(Locale.ENGLISH, " %s == %s ", ven.getName(), text1))
            if (ven.getName().equals(text2)) {
                Log.d(TAG, String.format(Locale.ENGLISH, "Got it"))
                Log.d(TAG, String.format(Locale.ENGLISH, "Click at (%.2f, %.2f)", ven.getX(), ven.getY()))
                //
                handleClick(ven.getX(), ven.getY())
                mSelectedVenue = subLoc.getVenues().get(i)
                mTargetVenue = mSelectedVenue
                mNextRoute.setVisibility(View.INVISIBLE)
                mNavigation.setTarget(LocationPoint(mLocation.getId(), subLoc.getId(), mTargetVenue.getX(), mTargetVenue.getY()))
            }
        }
        return
    }

    fun onMap(v: View?) {
        cat = null
        mMap.setVisibility(View.INVISIBLE)
        return
    }

    private fun getZoneAt(x: Float, y: Float): Zone? {
        if (mLocation == null || mCurrentSubLocationIndex < 0) return null
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return null
        val P: PointF = mLocationView.getAbsCoordinates(x, y)
        val LP = LocationPoint(mLocation.getId(), subLoc.getId(), P.x, P.y)
        for (i in 0 until subLoc.getZones().size()) {
            val Z: Zone = subLoc.getZones().get(i)
            if (Z.contains(LP)) return Z
        }
        return null
    }

    private fun drawPoints(canvas: Canvas) {
        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0) return

        // Get current sublocation displayed
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return
        val solidColor: Int = Color.argb(255, 64, 163, 205) // Light-blue color
        val circleColor: Int = Color.argb(127, 64, 163, 205) // Semi-transparent light-blue color
        val arrowColor: Int = Color.argb(255, 255, 255, 255) // White color
        val dp = mDisplayDensity
        val textSize = 16 * dp

        // Preparing paints
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setStyle(Paint.Style.FILL_AND_STROKE)
        paint.setTextSize(textSize)
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))

        // Drawing pin point (if it exists and belongs to the current sublocation)
        if (mPinPoint != null && mPinPoint.subLocation === subLoc.getId()) {
            val T: PointF = mLocationView.getScreenCoordinates(mPinPoint)
            val tRadius = 10 * dp
            paint.setARGB(255, 0, 0, 0)
            paint.setStrokeWidth(4 * dp)
            canvas.drawLine(T.x, T.y, T.x, T.y - 3 * tRadius, paint)
            paint.setColor(solidColor)
            paint.setStrokeWidth(0)
            canvas.drawCircle(T.x, T.y - 3 * tRadius, tRadius, paint)
            val text = "Make route"
            val textWidth: Float = paint.measureText(text)
            val h = 50 * dp
            val w: Float = Math.max(120 * dp, textWidth + h / 2)
            val x0: Float = T.x
            val y0: Float = T.y - 75 * dp
            mPinPointRect.set(x0 - w / 2, y0 - h / 2, x0 + w / 2, y0 + h / 2)
            paint.setColor(solidColor)
            canvas.drawRoundRect(mPinPointRect, h / 2, h / 2, paint)
            paint.setARGB(255, 255, 255, 255)
            canvas.drawText(text, x0 - textWidth / 2, y0 + textSize / 4, paint)
        }

        // Drawing target point (if it exists and belongs to the current sublocation)
        if (mTargetPoint != null && mTargetPoint.subLocation === subLoc.getId()) {
            Log.d(TAG, String.format(Locale.ENGLISH, "Drawing Path"))
            val T: PointF = mLocationView.getScreenCoordinates(mTargetPoint)
            val tRadius = 10 * dp
            paint.setARGB(255, 0, 0, 0)
            paint.setStrokeWidth(4 * dp)
            canvas.drawLine(T.x, T.y, T.x, T.y - 3 * tRadius, paint)
            paint.setColor(solidColor)
            canvas.drawCircle(T.x, T.y - 3 * tRadius, tRadius, paint)
        }
    }

    private fun drawVenues(canvas: Canvas) {
        if (mLocation == null || mCurrentSubLocationIndex < 0) return
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
        val dp = mDisplayDensity
        val textSize = 16 * dp
        val venueSize = 30 * dp
        val venueColor: Int = Color.argb(255, 0xCD, 0x88, 0x50) // Venue color
        val paint = Paint()
        paint.setStyle(Paint.Style.FILL_AND_STROKE)
        paint.setStrokeWidth(0)
        paint.setColor(venueColor)
        paint.setTextSize(textSize)
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        for (i in 0 until subLoc.getVenues().size()) {
            val v: Venue = subLoc.getVenues().get(i)
            if (v.getSubLocationId() !== subLoc.getId()) continue
            if (cat != null) {
                if (v.getCategory().getName().equals(cat)) {
                    mMap.setVisibility(View.VISIBLE)
                    val P: PointF = mLocationView.getScreenCoordinates(v.getX(), v.getY())
                    val x0: Float = P.x - venueSize / 2
                    val y0: Float = P.y - venueSize / 2
                    val x1: Float = P.x + venueSize / 2
                    val y1: Float = P.y + venueSize / 2
                    canvas.drawBitmap(mVenueBitmap, null, RectF(x0, y0, x1, y1), paint)
                }
            } else {
                mMap.setVisibility(View.INVISIBLE)
                val P: PointF = mLocationView.getScreenCoordinates(v.getX(), v.getY())
                val x0: Float = P.x - venueSize / 2
                val y0: Float = P.y - venueSize / 2
                val x1: Float = P.x + venueSize / 2
                val y1: Float = P.y + venueSize / 2
                canvas.drawBitmap(mVenueBitmap, null, RectF(x0, y0, x1, y1), paint)
            }
        }
        if (mSelectedVenue != null) {
            val T: PointF = mLocationView.getScreenCoordinates(mSelectedVenue.getX(), mSelectedVenue.getY())
            val textWidth: Float = paint.measureText(mSelectedVenue.getName())
            val h = 50 * dp
            val w: Float = Math.max(120 * dp, textWidth + h / 2)
            val x0: Float = T.x
            val y0: Float = T.y - 50 * dp
            mSelectedVenueRect.set(x0 - w / 2, y0 - h / 2, x0 + w / 2, y0 + h / 2)
            paint.setColor(venueColor)
            canvas.drawRoundRect(mSelectedVenueRect, h / 2, h / 2, paint)
            paint.setARGB(255, 255, 255, 255)
            canvas.drawText(mSelectedVenue.getName(), x0 - textWidth / 2, y0 + textSize / 4, paint)
        }
    }

    private fun drawZones(canvas: Canvas) {
        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0) return

        // Get current sublocation displayed
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return

        // Preparing paints
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setStyle(Paint.Style.FILL_AND_STROKE)
        for (i in 0 until subLoc.getZones().size()) {
            val Z: Zone = subLoc.getZones().get(i)
            if (Z.getPoints().size() < 3) continue
            val selected = Z === mSelectedZone
            val path = Path()
            val P0: LocationPoint = Z.getPoints().get(0)
            val Q0: PointF = mLocationView.getScreenCoordinates(P0)
            path.moveTo(Q0.x, Q0.y)
            for (j in 0 until Z.getPoints().size()) {
                val P: LocationPoint = Z.getPoints().get((j + 1) % Z.getPoints().size())
                val Q: PointF = mLocationView.getScreenCoordinates(P)
                path.lineTo(Q.x, Q.y)
            }
            val zoneColor: Int = Color.parseColor(Z.getColor())
            val red = zoneColor shr 16 and 0xff
            val green = zoneColor shr 8 and 0xff
            val blue = zoneColor shr 0 and 0xff
            paint.setColor(Color.argb(if (selected) 200 else 100, red, green, blue))
            canvas.drawPath(path, paint)
        }
    }

    private fun drawDevice(canvas: Canvas) {
        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0) return

        // Check if navigation is available
        if (mDeviceInfo == null || !mDeviceInfo.isValid()) return

        // Get current sublocation displayed
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
                ?: return
        val solidColor: Int = Color.argb(255, 64, 163, 205) // Light-blue color
        val circleColor: Int = Color.argb(127, 64, 163, 205) // Semi-transparent light-blue color
        val arrowColor: Int = Color.argb(255, 255, 255, 255) // White color
        val dp = mDisplayDensity

        // Preparing paints
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setStyle(Paint.Style.FILL_AND_STROKE)
        paint.setStrokeCap(Paint.Cap.ROUND)

        /// Drawing device path (if it exists)
        Log.d(TAG, String.format(Locale.ENGLISH, "Making path sublocation %s ", mDeviceInfo.getPaths()))
        if (mDeviceInfo.getPaths() != null && mDeviceInfo.getPaths().size() > 0) {
            val path: RoutePath = mDeviceInfo.getPaths().get(0)
            if (path.getPoints().size() >= 2) {
                paint.setColor(solidColor)
                for (j in 1 until path.getPoints().size()) {
                    val P: LocationPoint = path.getPoints().get(j - 1)
                    val Q: LocationPoint = path.getPoints().get(j)
                    Log.d(TAG, String.format(Locale.ENGLISH, "Clicked Q  %s  ", path.getPoints().get(j)))
                    if (P.subLocation === subLoc.getId() && Q.subLocation === subLoc.getId()) //
                    {

                        //Log.d(TAG, String.format(Locale.ENGLISH, "Clicked Q sublocation %s == %s " , Q.subLocation,subLoc.getId() ));
                        //Log.d(TAG, String.format(Locale.ENGLISH, "Clicked P sublocation %s == %s " , P.subLocation,subLoc.getId() ));
                        Log.d(TAG, String.format(Locale.ENGLISH, "Drawing path "))
                        paint.setStrokeWidth(3 * dp)
                        val P1: PointF = mLocationView.getScreenCoordinates(P)
                        val Q1: PointF = mLocationView.getScreenCoordinates(Q)
                        canvas.drawLine(P1.x, P1.y, Q1.x, Q1.y, paint) //
                        //Log.d(TAG, String.format(Locale.ENGLISH, "Making path sublocation %s "));
                    }
                }
            }
        }
        paint.setStrokeCap(Paint.Cap.BUTT)

        // Check if device belongs to the current sublocation
        if (mDeviceInfo.getSubLocationId() !== subLoc.getId()) return
        val x: Float = mDeviceInfo.getX()
        val y: Float = mDeviceInfo.getY()
        val r: Float = mDeviceInfo.getR()
        val angle: Float = mDeviceInfo.getAzimuth()
        val sinA = Math.sin(angle) as Float
        val cosA = Math.cos(angle) as Float
        val radius: Float = mLocationView.getScreenLengthX(r) // External radius: navigation-determined, transparent
        val radius1 = 25 * dp // Internal radius: fixed, solid
        val O: PointF = mLocationView.getScreenCoordinates(x, y)
        val P = PointF(O.x - radius1 * sinA * 0.22f, O.y + radius1 * cosA * 0.22f)
        val Q = PointF(O.x + radius1 * sinA * 0.55f, O.y - radius1 * cosA * 0.55f)
        val R = PointF(O.x + radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y + radius1 * sinA * 0.44f + radius1 * cosA * 0.55f)
        val S = PointF(O.x - radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y - radius1 * sinA * 0.44f + radius1 * cosA * 0.55f)

        // Drawing transparent circle
        paint.setStrokeWidth(0)
        paint.setColor(circleColor)
        canvas.drawCircle(O.x, O.y, radius, paint)

        // Drawing solid circle
        paint.setColor(solidColor)
        canvas.drawCircle(O.x, O.y, radius1, paint)
        if (ORIENTATION_ENABLED) {
            // Drawing arrow
            paint.setColor(arrowColor)
            val path = Path()
            path.moveTo(Q.x, Q.y)
            path.lineTo(R.x, R.y)
            path.lineTo(P.x, P.y)
            path.lineTo(S.x, S.y)
            path.lineTo(Q.x, Q.y)
            canvas.drawPath(path, paint)
        }
    }

    private fun adjustDevice() {
        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0) return

        // Check if navigation is available
        if (mDeviceInfo == null || !mDeviceInfo.isValid()) return
        val timeNow: Long = System.currentTimeMillis()

        // Adjust map, if necessary
        if (timeNow >= mAdjustTime) {
            // Firstly, set the correct sublocation
            val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
            if (mDeviceInfo.getSubLocationId() !== subLoc.getId()) {
                for (i in 0 until mLocation.getSubLocations().size()) if (mLocation.getSubLocations().get(i).getId() === mDeviceInfo.getSubLocationId()) loadSubLocation(i)
            }

            // Secondly, adjust device to the center of the screen
            val center: PointF = mLocationView.getScreenCoordinates(mDeviceInfo.getX(), mDeviceInfo.getY())
            val deltaX: Float = mLocationView.getWidth() / 2 - center.x
            val deltaY: Float = mLocationView.getHeight() / 2 - center.y
            mAdjustTime = timeNow
            mLocationView.scrollBy(deltaX, deltaY)
        }
    }

    private fun getLogFile(extension: String): String? {
        return try {
            val extDir: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath().toString() + "/Navigine.Demo"
            File(extDir).mkdirs()
            if (!File(extDir).exists()) return null
            val calendar: Calendar = Calendar.getInstance()
            calendar.setTimeInMillis(System.currentTimeMillis())
            String.format(Locale.ENGLISH, "%s/%04d%02d%02d_%02d%02d%02d.%s", extDir,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND),
                    extension)
        } catch (e: Throwable) {
            null
        }
    }

    fun ClickVenueList(v: View?) {
        val intent = Intent(this, VenuePage::class.java)
        startActivity(intent)
    }

    fun Search_Btn(v: View?) {
        val intent = Intent(this, SearchPage::class.java)
        //intent.putExtra("VenueList", (Serializable) VenueList);
        startActivity(intent)
    }

    companion object {
        private const val TAG = "NAVIGINE.Demo"
        private const val NOTIFICATION_CHANNEL = "NAVIGINE_DEMO_NOTIFICATION_CHANNEL"
        private const val UPDATE_TIMEOUT = 100 // milliseconds
        private const val ADJUST_TIMEOUT = 5000 // milliseconds
        private const val ERROR_MESSAGE_TIMEOUT = 5000 // milliseconds
        private const val ORIENTATION_ENABLED = true // Show device orientation?
        private const val NOTIFICATIONS_ENABLED = true // Show zone notifications?
    }
}