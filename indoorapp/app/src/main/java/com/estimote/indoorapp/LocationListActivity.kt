package com.estimote.indoorapp

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.estimote.indoorsdk_module.cloud.Location
import kotlinx.android.synthetic.main.activity_location_list.*
import java.util.*

class LocationListActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    /* Lectura*/
    private var tts: TextToSpeech? = null
   // private var hablar: Button? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.getDefault())
            if (result != TextToSpeech.LANG_MISSING_DATA ||
                    result != TextToSpeech.LANG_NOT_SUPPORTED) {
            }
        }
    }

    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }


    private lateinit var mRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var mAdapter: LocationListAdapter
    private lateinit var mNoLocationsView: TextView
    private lateinit var mLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager
    private lateinit var location: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)


        mNoLocationsView = findViewById(R.id.no_locations_view) as TextView
        mRecyclerView = findViewById(R.id.my_recycler_view) as androidx.recyclerview.widget.RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager
        tts = TextToSpeech(this, this)
        mAdapter = LocationListAdapter(emptyList())
        mRecyclerView.adapter = (mAdapter)
        mAdapter.setOnClickListener { locationId ->

            startActivity(MainActivity.createIntent(this, locationId))
            location = (application as IndoorApplication).locationsById[locationId]!!
                val arrayofString = arrayOf("Haz seleccionado "+location.name+" conócela por dentro")
                for (i in arrayofString) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts!!.speak(i, TextToSpeech.QUEUE_FLUSH, null, null)
                    } else {
                        val hash = HashMap<String, String>()
                        hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                                AudioManager.STREAM_NOTIFICATION.toString())
                        tts!!.speak(i, TextToSpeech.QUEUE_FLUSH, hash)

                    }

                }

        }

/* Reconocimiento de voz */
        microfono.setOnClickListener {
            var i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            startActivityForResult(i, 10)
        }

    }

    override fun onStart() {
        super.onStart()
        val locations = (application as IndoorApplication).locationsById.values.toList()
        if (locations.isEmpty()) {
            mNoLocationsView.visibility = View.VISIBLE
            mRecyclerView.visibility = View.GONE
        } else {
            mAdapter.setLocations((application as IndoorApplication).locationsById.values.toList())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            10 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    var res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    txtRecibeAudio.text = res.get(0)
                }
            }

        }
    }
}