package com.smn.nfccardreader

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.smn.cardreaderlib.NFCReaderSDK
import com.smn.cardreaderlib.enums.NfcState
import com.smn.cardreaderlib.interfaces.EmvResultsListener
import com.smn.cardreaderlib.models.EmvResults

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NFCReaderSDK.getInstance().nfcDevice.startCardReader( this,object :
            EmvResultsListener {

            override fun onEmvResults(emvResults: EmvResults) {
                runOnUiThread {
                    findViewById<TextView>(R.id.text).text = String.format("AID = %s \nCardNumber = %s",
                        emvResults.aid,emvResults.cardNumber)
                }
            }

            override fun onNfcState(nfcState: NfcState) {
                runOnUiThread {
                    findViewById<TextView>(R.id.text).text = String.format("%s",nfcState.name)
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