package com.smn.nfccardreader

import android.app.Application
import com.smn.cardreaderlib.NFCReaderSDK
import com.smn.cardreaderlib.logger.NFCReaderSDKLogger

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        try {

            NFCReaderSDK.configureSDK()
                .withContext(this)
                .withLogger(NFCReaderSDKLogger())
                .apply()

        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
}