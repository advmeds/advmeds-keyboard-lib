package com.advmeds.customkeyboard

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.advmeds.keyboard.MyInputMethodService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        Handler().post {
            if (RootUtils.isDeviceRooted && RootUtils.canRunRootCommands()) {
                val allIMEs = (application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).inputMethodList
                val mIME = allIMEs.first { it.id.contains(BuildConfig.APPLICATION_ID) && it.id.contains(MyInputMethodService::class.java.simpleName) }
                RootUtils.excute("su -c ime enable ${mIME.id}")
                RootUtils.excute("su -c ime set ${mIME.id}")
            }
        }
    }
}