package com.example.bdobosstimer

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    //uwu!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateBoss()
    }

    private fun updateBoss() {
        val nextBoss = BossHelper.instance.getNextBoss()
        main_text_boss_title.text = getString(
            R.string.nextBossAnnounce,
            nextBoss.name,
            nextBoss.timeSpawn,
            nextBoss.minutesToSpawn
        )
        main_image_boss_one.setImageResource(nextBoss.bossOneImageResource!!)
        if (nextBoss.bossTwoImageResource != null) {
            main_image_boss_two.visibility = VISIBLE
            main_image_boss_two.setImageResource(nextBoss.bossTwoImageResource!!)
        } else {
            main_image_boss_two.visibility = GONE
        }
    }

    fun onButtonClicked(view: View){
        if (view.id == R.id.main_button_refresh){
            updateBoss()
        }

    }
}
