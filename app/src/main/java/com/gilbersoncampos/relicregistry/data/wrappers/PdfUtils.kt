package com.gilbersoncampos.relicregistry.data.wrappers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.gilbersoncampos.relicregistry.BuildConfig

fun openPDF(
    viewModel: PdfViewModelInterface,
    context: Context
) {
    viewModel.generatePdf()
    val file = viewModel.getPdf()
    val uri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider", file
    )
    val target = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    Log.d("Activity", uri.toString())
    val intent = Intent.createChooser(target, "Open Files")
    try {
        startActivity(context, intent, null)
    } catch (e: Exception) {
        Log.e("Activity", e.message.toString())
    }
}