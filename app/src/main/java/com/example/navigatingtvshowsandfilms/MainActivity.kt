package com.example.navigatingtvshowsandfilms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterForRes: RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val showTypes = arrayOf("Все", "Сериалы", "Фильмы")
        val adapterForS = ArrayAdapter(this, android.R.layout.simple_spinner_item, showTypes)
        spinner.adapter = adapterForS
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(spinner.selectedItem.toString() != "Все")
                    displayAllObjects(spinner.selectedItem.toString().toLowerCase().substring(0, spinner.selectedItem.toString().length -1))
                else
                    displayAllObjects("")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                displayAllObjects("")
            }

        }
        val addBtn = findViewById<Button>(R.id.addBtn)
        val main = this
        val ocAdd: View.OnClickListener = View.OnClickListener {
            val intent = Intent(main, AddScreen::class.java)
            startActivity(intent)
        }
        addBtn.setOnClickListener(ocAdd)

        recyclerView = findViewById<RecyclerView>(R.id.recycler)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        displayAllObjects("")
    }

    fun displayAllObjects(condition: String) {
        val dbh = DataBaseHelper(this)
        adapterForRes = RecyclerViewAdapter(dbh.getObjects(condition), supportFragmentManager)
        recyclerView.adapter = adapterForRes
    }
}