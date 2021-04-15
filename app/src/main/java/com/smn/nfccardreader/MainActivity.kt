package com.smn.nfccardreader

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.smn.cardreaderlib.interfaces.CardResultsListener
import com.smn.cardreaderlib.NFCReaderSDK
import com.smn.cardreaderlib.models.EMVCard

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        NFCReaderSDK.getInstance().nfcDevice.startCardReader( this,object :
            CardResultsListener {

            override fun onEMVCardData(aid: String) {
                runOnUiThread {
                    findViewById<TextView>(R.id.text).text = aid
                }
            }

            override fun onNfcNotEnabled() {
                runOnUiThread {
                    findViewById<TextView>(R.id.text).text = String.format("%s","NFC Not Enabled")
                }
            }

            override fun onError(message: String) {
                runOnUiThread {
                    findViewById<TextView>(R.id.text).text = message
                }
            }

        })
    }
}