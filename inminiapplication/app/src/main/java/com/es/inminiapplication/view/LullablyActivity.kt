package com.es.inminiapplication.view

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.es.inminiapplication.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.concurrent.TimeUnit

class LullablyActivity : AppCompatActivity() {
    private var startTimeField: TextView? = null
    private var endTimeField: TextView? = null
    private var mediaPlayer: MediaPlayer? = null
    private var startTime = 0.0
    private var finalTime = 0.0
    private val myHandler = Handler()
    private val forwardTime = 5000
    private val backwardTime = 5000
    private var seekbar: SeekBar? = null
    private var playButton: ImageButton? = null
    private var pauseButton: ImageButton? = null
    private var myUri: String? = null
    private var start = 0
    private var length = 0
    private var lullablyref = "gs://inminiapplication.appspot.com/dandini_dandini_dastana.mp3"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lullably)

        startTimeField = findViewById<View>(R.id.textView1) as TextView
        endTimeField = findViewById<View>(R.id.textView2) as TextView
        seekbar = findViewById<View>(R.id.seekBar1) as SeekBar
        playButton = findViewById<View>(R.id.imageButton1) as ImageButton
        pauseButton = findViewById<View>(R.id.imageButton2) as ImageButton

        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference =
            storage.getReferenceFromUrl(lullablyref)

        storageRef.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
            myUri = uri.toString()
        })

        seekbar!!.isClickable = false
    }

    fun play(view: View?) {
        if (start == 0) {
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer!!.setDataSource(myUri)
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
                start = 1

                // OnCompletionListener ekleyerek şarkı bittiğinde başa dön
                mediaPlayer!!.setOnCompletionListener {
                    mediaPlayer!!.start()
                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        } else if (start == 1) {
            mediaPlayer!!.pause()
            length = mediaPlayer!!.currentPosition
            start = 2
        } else if (start == 2) {
            length = mediaPlayer!!.currentPosition
            mediaPlayer!!.seekTo(length)
            mediaPlayer!!.start()
            start = 1
        }
        finalTime = mediaPlayer!!.duration.toDouble()
        if (length.toDouble() == finalTime) {
            start = 1
            mediaPlayer!!.start()
        }
        startTime = mediaPlayer!!.currentPosition.toDouble()
        if (oneTimeOnly == 0) {
            seekbar!!.max = finalTime.toInt()
            oneTimeOnly = 1
        }
        endTimeField!!.text = String.format(
            "%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())
                    )
        )
        startTimeField!!.text = String.format(
            "%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())
                    )
        )
        seekbar!!.progress = startTime.toInt()
        myHandler.postDelayed(updateSongTime, 100)
    }

    private val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer!!.currentPosition.toDouble()
            startTimeField!!.text = String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                        TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())
                        )
            )
            seekbar!!.progress = startTime.toInt()
            myHandler.postDelayed(this, 100)
        }
    }
    fun pause(view: View?) {
        if (start == 1) {
            mediaPlayer?.pause()
            length = mediaPlayer?.currentPosition ?: 0
            start = 2
        }
    }
    fun forward(view: View?) {
        val temp = startTime.toInt()
        if (temp + forwardTime <= finalTime) {
            startTime += forwardTime
            mediaPlayer!!.seekTo(startTime.toInt())
        } else {
            Toast.makeText(
                applicationContext,
                "Son 5 saniyedeyken müziği ilerletemezsiniz",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun rewind(view: View?) {
        val temp = startTime.toInt()
        if (temp - backwardTime > 0) {
            startTime -= backwardTime
            mediaPlayer!!.seekTo(startTime.toInt())
        } else {
            Toast.makeText(
                applicationContext,
                "İlk 5 saniyedeyken müziği geri alamazsınız",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        var oneTimeOnly = 0
    }
    fun x(view: View?){
       lullablyref="gs://inminiapplication.appspot.com/dandini_dandini_dastana.mp3"
       play(view)
    }
    fun y(view: View?){
        lullablyref="gs://inminiapplication.appspot.com/fis_fis_kayikci.mp3"
        play(view)
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
        finish()
    }
}
