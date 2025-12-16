package com.example.ca1giftcardwr.helpers

import android.content.Context
import java.io.*

fun write(context: Context, fileName: String, data: String) {
    val outputStreamWriter = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
    outputStreamWriter.write(data)
    outputStreamWriter.close()
}

fun read(context: Context, fileName: String): String {
    val inputStreamReader = InputStreamReader(context.openFileInput(fileName))
    val bufferedReader = BufferedReader(inputStreamReader)
    val stringBuilder = StringBuilder()
    var line: String?

    while (bufferedReader.readLine().also { line = it } != null) {
        stringBuilder.append(line)
    }

    return stringBuilder.toString()
}

fun exists(context: Context, fileName: String): Boolean {
    val file = context.getFileStreamPath(fileName)
    return file.exists()
}