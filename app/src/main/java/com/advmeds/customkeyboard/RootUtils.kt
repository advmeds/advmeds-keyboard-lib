package com.advmeds.customkeyboard

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.InputStreamReader

object RootUtils {

    val isDeviceRooted = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

    private fun checkRootMethod1(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        listOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        ).forEach { if (File(it).exists()) return true }

        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            val cmdArray = arrayOf(
                "/system/xbin/which",
                "su"
            )
            process = Runtime.getRuntime().exec(cmdArray)
            val `in` = BufferedReader(InputStreamReader(process?.inputStream))
            `in`.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    fun canRunRootCommands(): Boolean {
        var result: Boolean
        val suProcess: Process

        try {
            suProcess = Runtime.getRuntime().exec("su")

            val os = DataOutputStream(suProcess?.outputStream)
            val osRes = BufferedReader(InputStreamReader(suProcess?.inputStream))

            os.writeBytes("id\n")
            os.flush()

            val currUid = osRes.readLine()
            val exitSu: Boolean

            when {
                currUid == null -> {
                    result = false
                    exitSu = false
                    Log.d(
                        "ROOT",
                        "Can't get root access or denied by user"
                    )
                }
                currUid.contains("uid=0") -> {
                    result = true
                    exitSu = true
                    Log.d(
                        "ROOT",
                        "Root access granted"
                    )
                }
                else -> {
                    result = false
                    exitSu = true
                    Log.d(
                        "ROOT",
                        "Root access rejected: $currUid"
                    )
                }
            }

            if (exitSu) {
                os.writeBytes("exit\n")
                os.flush()
            }
        } catch (e: Exception) {
            // Can't get root !
            // Probably broken pipe exception on trying to write to output stream (os) after su failed, meaning that the device is not rooted

            result = false
            Log.e(
                "ROOT",
                "Root access rejected [${e.javaClass.name}] : ${e.message}"
            )
        }

        return result
    }

    fun excute(command: String) {
        try {
            Runtime.getRuntime().exec(command)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}