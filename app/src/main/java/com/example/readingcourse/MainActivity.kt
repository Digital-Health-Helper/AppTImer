package com.example.readingcourse

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import sun.jvm.hotspot.utilities.IntArray
import java.util.*


class MainActivity : AppCompatActivity() {
    private val recyclerView: RecyclerView? = null
    private val mAdapter: RecyclerView.Adapter? = null
    private val dbHelper: AppTimerDbHelper? = null
    var usageStatsManager: UsageStatsManager? = null
    var context: Context? = null
    var topAppsList: ArrayList<HashMap> = ArrayList()
    var loader: ConstraintLayout? = null
    var appListAdapter: AppListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
