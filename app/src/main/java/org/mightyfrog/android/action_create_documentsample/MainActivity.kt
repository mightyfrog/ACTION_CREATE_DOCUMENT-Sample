package org.mightyfrog.android.action_create_documentsample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            createFile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestFileOpen && resultCode == RESULT_OK) {
            val uri = data?.data
            uri?.apply {
                val outputStream = contentResolver.openOutputStream(this)
                assets.open(filename).copyTo(outputStream!!, 1024)
                openFile(this)
            } ?: run {
                Toast.makeText(this, R.string.err, Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createFile() {
        Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            type = mimeType
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            // pass a filename to populate the file chooser
            // framework takes care of duplicate filenames
            putExtra(Intent.EXTRA_TITLE, filename)
            startActivityForResult(this, requestFileOpen)
        }
    }

    private fun openFile(uri: Uri) {
        Intent(Intent.ACTION_VIEW).apply {
            // create & open document mime type must be the same!!!
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    companion object {
        const val requestFileOpen = 1
        const val mimeType = "image/png"
        const val filename = "sample.png"
    }
}