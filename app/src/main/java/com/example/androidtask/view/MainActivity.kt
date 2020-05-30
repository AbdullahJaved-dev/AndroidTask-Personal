package com.example.androidtask.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtask.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_show_users_list.setOnClickListener {
            applicationContext.startActivity(Intent(this, ShowUsersListActivity::class.java))
        }
        btn_show_users_on_map.setOnClickListener {

            applicationContext.startActivity(Intent(this, ShowUsersMapActivity::class.java))

        }
    }
}
