package com.longforus.onekeyreinforceandpatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.taobao.sophix.SophixManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SophixManager.getInstance().queryAndLoadNewPatch()
        findViewById<Button>(R.id.btn_test).setOnClickListener {
            val a = 10 /0
            //修好了就不会崩溃了
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
        }
    }
}