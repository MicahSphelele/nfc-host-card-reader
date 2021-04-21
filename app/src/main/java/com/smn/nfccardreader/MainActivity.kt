package com.smn.nfccardreader

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.smn.cardreaderlib.NFCReaderSDK
import com.smn.cardreaderlib.enums.NfcState
import com.smn.cardreaderlib.interfaces.EmvResultsListener
import com.smn.cardreaderlib.models.EmvResults
import com.smn.nfccardreader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NFCReaderSDK.getInstance().nfcDevice.startCardReader( this,object :
                EmvResultsListener {

                override fun onEmvResults(emvResults: EmvResults) {
                    runOnUiThread {
                        binding.text.text = String.format("AID = %s \nCardNumber = %s",
                            emvResults.aid,emvResults.cardNumber)
                    }
                }

                override fun onNfcState(nfcState: NfcState) {
                    runOnUiThread {
                        binding.text.text = String.format("%s",nfcState.name)
                    }
                }

                override fun onError(message: String) {
                    runOnUiThread {
                        binding.text.text = message
                    }
                }

            })
            return
        }
        binding.text.text = this.getString(R.string.txt_nfc_error_message)
    }
}