package com.example.navigatingtvshowsandfilms

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class AddScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_screen)
        val screen = this
        val nameBox = findViewById<EditText>(R.id.name)
        val filmBox = findViewById<RadioButton>(R.id.filmBtn)
        val serialBox = findViewById<RadioButton>(R.id.serialBtn)
        val progressBox = findViewById<CheckBox>(R.id.progress)
        val watchedBox = findViewById<CheckBox>(R.id.watched)

        filmBox.isChecked = true
        progressBox.visibility = View.INVISIBLE

        val ocFilmSerial = View.OnClickListener {
            if(filmBox.isChecked) {
                progressBox.visibility = View.INVISIBLE
            } else {
                progressBox.visibility = View.VISIBLE
            }
        }
        filmBox.setOnClickListener(ocFilmSerial)
        serialBox.setOnClickListener(ocFilmSerial)

        val addBtn = findViewById<Button>(R.id.addShow)
        val ocAdd: View.OnClickListener = View.OnClickListener {
            if(nameBox.text.toString() == "") {
                Toast.makeText(screen, "Заполните название!", Toast.LENGTH_SHORT).show()
            } else{
                val dbh = DataBaseHelper(screen)
                var show = Show()
                show.name = nameBox.text.toString()
                show.type = "фильм"
                if(!filmBox.isChecked) {
                    show.type = "сериал"
                }
                if(show.type == "сериал") {
                    if(progressBox.isChecked) {
                        show.inProgress = "В разработке"
                    } else {
                        show.inProgress = "Завершен"
                    }
                }
                if(watchedBox.isChecked) {
                    show.watched = "Просмотрено"
                } else {
                    show.watched = "Не просмотрено"
                }
                if(dbh.insertObject(show)) {
                    Toast.makeText(screen, "Шоу успешно добавлено!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    val handler = Handler()
                    handler.postDelayed(Runnable { finish() }, 1000)
                } else{
                    Toast.makeText(screen, "Что-то пошло не так...", Toast.LENGTH_SHORT).show()
                    val handler = Handler()
                    handler.postDelayed(Runnable { finish() }, 1000)
                }
            }
        }
        addBtn.setOnClickListener(ocAdd)
    }
}