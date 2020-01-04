package com.hiskia.absensi.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.hiskia.absensi.R
import com.hiskia.absensi.api.ApiClient
import com.hiskia.absensi.api.ApiInterface
import com.hiskia.absensi.helper.Cons
import com.hiskia.absensi.helper.Loading
import com.google.android.gms.vision.barcode.Barcode
import info.androidhive.barcode.BarcodeReader
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class MainActivity : BaseActivity(), BarcodeReader.BarcodeReaderListener {

    var barcodeReader: BarcodeReader? = null
    var pLoading: Loading? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        setToolbar()
    }

    fun setToolbar() {
        setSupportActionBar(toolbar)
        val viewActionBar: View =
            layoutInflater.inflate(R.layout.toolbar_layout, null)
        val params: ActionBar.LayoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        val tvTitleToolbar =
            viewActionBar.findViewById<TextView>(R.id.tv_actionbar)
        tvTitleToolbar.visibility = View.VISIBLE
        tvTitleToolbar.text = "Scanner"
        val ab: ActionBar? = supportActionBar
        ab!!.setCustomView(viewActionBar, params)
        ab.setDisplayShowCustomEnabled(true)
        ab.setDisplayShowTitleEnabled(false)
    }

    fun initUI() {
        pLoading = Loading(this@MainActivity)

        // get the barcode reader instance
        barcodeReader =
            supportFragmentManager.findFragmentById(R.id.barcode_scanner) as BarcodeReader?
    }

    override fun onScanned(barcode: Barcode) { // playing barcode reader beep sound
        barcodeReader?.playBeep()
        barcodeReader?.pauseScanning()
        // ticket details activity by passing barcode
//        val intent = Intent(
//            this@MainActivity,
//            DetailBarangKeluarActivity::class.java
//        )
//        intent.putExtra("barcode", barcode.displayValue)
//        startActivity(intent)

        Log.e("data_qrcode", "" + barcode.displayValue)

        runOnUiThread {
            //                popUpScan();
            try{
                hitAPiScanned(barcode.displayValue.toLong())
            }catch (e :Exception){
                dialogNodataScanned("")
            }

        }
    }

    override fun onScannedMultiple(list: List<Barcode?>?) {}

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode?>?) {}

    override fun onScanError(s: String) {
        Toast.makeText(
            applicationContext,
            "Error occurred while scanning $s",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCameraPermissionDenied() {
        finish()
    }

    override fun onResume() {
        barcodeReader?.resumeScanning()
        super.onResume()
    }

    fun hitAPiScanned(barcode_id: Long) {
        pLoading!!.showLoading(getString(R.string.toast_api_loading), false)
        val service: ApiInterface = ApiClient.getClient()
        val call: Call<ResponseBody> =
            service.scan(barcode_id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                pLoading!!.dismissDialog()
                if (response.isSuccessful) {
                    try {
                        val code = response.body()!!.string()
                        val jsonObject = JSONObject(code)
                        Log.e("response", "" + code)

                        val api_status = jsonObject.getInt(Cons.API_STATUS)
                        val api_message = jsonObject.getString(Cons.API_MESSAGE)
                        if (api_status == Cons.INT_STATUS) {
//                            toast(api_message)
                            popUpScan(api_message)
                            pLoading!!.dismissDialog()
                        } else {
                            pLoading!!.dismissDialog()
                            dialogNodataScanned( api_message)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    pLoading!!.dismissDialog()
                    toast(getString(R.string.toast_api_koneksi1))
                }
            }

            override fun onFailure(
                call: Call<ResponseBody>,
                t: Throwable
            ) {
                pLoading!!.dismissDialog()
                toast(getString(R.string.toast_api_koneksi2))
            }
        })
    }

    fun popUpScan(api_message: String) {
        val dialogFilter = Dialog(this, R.style.DialogLight)
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFilter.setContentView(R.layout.dialog_submit_opname)
        dialogFilter.setCancelable(false)
        val tv_code = dialogFilter.findViewById<TextView>(R.id.tv_code)
        val btn_scan =
            dialogFilter.findViewById<Button>(R.id.btn_scan)

        tv_code.text = api_message

        btn_scan.setOnClickListener {
            barcodeReader!!.resumeScanning()
            dialogFilter.dismiss()
        }
        val wm =
            getSystemService(Context.WINDOW_SERVICE) as WindowManager // for activity use context instead of getActivity()
        val display = wm.defaultDisplay // getting the screen size of device
        val size = Point()
        display.getSize(size)
        val width = size.x - 110 // Set your heights
        //        int height = size.y - 80; // set your widths
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogFilter.window!!.attributes)
        lp.width = width
        //        lp.height = height;
        dialogFilter.window!!.attributes = lp
        dialogFilter.show()
    }

    fun dialogNodataScanned(
        api_message: String?
    ) {
        val dialogFilter = Dialog(this, R.style.DialogLight)
        dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFilter.setContentView(R.layout.dialog_scanned_nodata)
        dialogFilter.setCancelable(false)
        val tv_headMessage = dialogFilter.findViewById<TextView>(R.id.tv_headMessage)
        val tv_message = dialogFilter.findViewById<TextView>(R.id.tv_message)
        val btn_oke =
            dialogFilter.findViewById<Button>(R.id.btn_oke)
//        tv_headMessage.text = head_api_message

        if (api_message != ""){
            tv_message.text = api_message
        }

        btn_oke.setOnClickListener {
            barcodeReader!!.resumeScanning()
            dialogFilter.dismiss()
        }
        val wm =
            getSystemService(Context.WINDOW_SERVICE) as WindowManager // for activity use context instead of getActivity()
        val display = wm.defaultDisplay // getting the screen size of device
        val size = Point()
        display.getSize(size)
        val width = size.x - 110 // Set your width
        //        int height = size.y - 80; // set your height
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogFilter.window!!.attributes)
        lp.width = width
        //        lp.height = height;
        dialogFilter.window!!.attributes = lp
        dialogFilter.show()
    }

}
