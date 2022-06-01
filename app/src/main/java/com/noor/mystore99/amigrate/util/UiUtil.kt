package com.noor.mystore99.amigrate.util


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.InputFilter
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.noor.mystore99.BuildConfig
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern


class UiUtil(private val context: Context) {

    private lateinit var snackbar: Snackbar
    private lateinit var progressDialog: ProgresssDialog

    fun showMessage(
        message: String,
        length: Int = Snackbar.LENGTH_SHORT,
        button: Boolean = false,
        buttonText: String = "Ok"
    ) {
        //showSnackBar(message, length, button, buttonText)
    }

    fun showToast(
        message: String,
        length: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(context, message, length)
            .show()
    }

    fun singleLinedInputTextDialog(
        title: String,
        inputText: String = "",
        positiveButtonText: String = "Ok",
        negativeButtonText: String = context.getString(R.string.cancel),
        onPositive: (text: String) -> Unit,
        onNegative: () -> Unit = {},
        onDismiss: () -> Unit = {},
        onShow: () -> Unit = {}
    ) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(title)

        // Set up the input
        val container = LinearLayout(context)
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val marginOffset = 15
        lp.setMargins(marginOffset, 0, marginOffset, 0)
        val input = EditText(context)
        input.layoutParams = lp
        input.setText(inputText)
        input.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(80))
        container.addView(input)
        builder.setView(container)

        builder.setPositiveButton(positiveButtonText) { _, _ ->
            onPositive(input.text.toString())
        }
        builder.setOnDismissListener {
            onDismiss()
        }
        builder.setNegativeButton(negativeButtonText) { _, _ ->
            onNegative()
        }
        builder.show()
        onShow()
    }


    fun showProgress() {
        if (!::progressDialog.isInitialized) {
            progressDialog = ProgresssDialog(context)
        }
        progressDialog.show()
    }

    fun hideProgress() {
        if (!::progressDialog.isInitialized) {
            progressDialog = ProgresssDialog(context)
            return
        }
        progressDialog.dismiss()
    }

//    private fun showSnackBar(
//        message: String,
//        snackBarLength: Int = Snackbar.LENGTH_LONG,
//        button: Boolean = false,
//        buttonText: String = "Ok"
//    ) {
//        if (!::snackbar.isInitialized) {
////            (context as BaseActivity<*, *>).getLayoutBinding()
////                .root.let {
////                    snackbar = Snackbar.make(it, message, snackBarLength)
////                }
//        }
//        if (button) {
//            snackbar.setAction(buttonText) {
//                openSetting(context)
//            }
//        }
//
//        snackbar.setText(message)
//        snackbar.show()
//    }

    fun openSetting(context: Context) {
        context.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            )
        )
    }

    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }


    fun openSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, 101)
    }

    fun toMD5ToUpperCase(string: String): String {
        val MD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance(MD5)
            digest.update(string.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = java.lang.StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString().uppercase()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun isFollowPasswordPolicy(string: String): Boolean {
        if (string.length < 6 || string.length > 50) return false
        if (string.contains(" ")) return false
        if (string.contains("\"") || string.contains("\'")) return false
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*\\(\\){}\\[\\]<>,.?\\\\|/:;]).+$"
        val p = Pattern.compile(regex)
        val m = p.matcher(string)
        return m.matches()
    }


    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun convertFileToBase64StringFromInputStream(inputStream: InputStream): String? {
        try {
            val bytes = getBytes(inputStream)
            return Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getSizeFromUri(uri: Uri): Int {
        try {
            val inputtt = context.contentResolver.openInputStream(uri)
            val byte = inputtt?.let { getBytes(it) }
            return byte?.size ?: 0
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return 0
        }
    }


    fun convertToBase64StringFromUri(uri: Uri): String {
        try {
            val inputtt = context.contentResolver.openInputStream(uri)
            return inputtt?.let { convertFileToBase64StringFromInputStream(it) }.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

}