package com.example.speedword

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import android.os.CountDownTimer
import android.content.DialogInterface
import android.media.MediaPlayer




class MainActivity : AppCompatActivity() {
    lateinit var boom:MediaPlayer
    var score = 0
    private val REQUEST_CODE_SPEECH_INPUT = 100
    var bam = getRandomString(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        boom = MediaPlayer.create(this, R.raw.motus)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)

        newb.setOnClickListener{
            score = 0
            bam = getRandomString(1)
            Log.d("bam","$bam")
            Log.d("bama","coucou")
            speak();
            rat.setText("")

        }


    }

    fun getRandomString(length: Int) : String {
        val allowedChars = "abcdefghiklmnopqrstuvwxyz"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")

    }

    private fun speak() {
        Handler().postDelayed({
            Log.d("trop","trop tard !")
            return@postDelayed
        }, 2000)
        var mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "dis un mot qui commence par $bam")
        mIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 0)
        mIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 0);
        mIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1500)

        try {
                startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
        }
        catch (e: Exception) {
          Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when(requestCode){
            REQUEST_CODE_SPEECH_INPUT -> {

                if(resultCode == Activity.RESULT_OK && null != data){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val lettre = result[0][0]
                    val fword = result[0]
                    //val bam = "m"
                    val yop = bam[0]
                    //var f = result[0][0]
                    //hasPrefix(f)
                    Log.d("first letter", "$lettre");
                    //Log.d("bam", "$bam");
                    val str: String = rat.text.toString()
                    Log.d("the value is", "$str")

                    if(lettre.equals(yop, true) && !("$str".contains("$fword", ignoreCase = true)) && !("$fword".contains(" ", ignoreCase = true)) ) {
                        Log.d("rat", "$rat")
                        Log.d("mot dit", "$fword")
                        rat.append(result[0])
                        rat.append(" - ")

                        Log.d("str", "$str")
                        Log.d("mot dit","$fword")
                        if("$str".contains("$fword", ignoreCase = true)){
                            rat.append(" perdu normalement ")
                        }


                        val dialog = AlertDialog.Builder(this)
                            .setTitle("JOUER SUIVANT").setMessage(
                                "Passez le téléphone au joueur qui suit"
                            )

                        val alert = dialog.create()
                        alert.show()

                        object : CountDownTimer(5000, 1000) {

                            override fun onTick(millisUntilFinished: Long) {
                                // TODO Auto-generated method stub

                            }

                            override fun onFinish() {
                                // TODO Auto-generated method stub

                                alert.dismiss()
                            }
                        }.start()
                        score += 1

                        Handler().postDelayed({
                            speak()
                        }, 1000)

                    }else {
                        rat.append(" ")
                        boom.start()
                        perdu.setText("PERDU")
                        txtt.setText("")

                        scor.setText("Nombre de tours : $score")
                    }

                }
            }

        }
    }


}
