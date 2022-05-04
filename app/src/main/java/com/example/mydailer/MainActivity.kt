package com.example.mydailer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        setSupportActionBar(findViewById(R.id.toolbar))

        // зачем это надо, если мы в лайауте устанаваливаем класс фрагмента и все хорошо воркает -_-
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment, ContactFragment.newInstance(), null)
                .commit()
        }
    }
}