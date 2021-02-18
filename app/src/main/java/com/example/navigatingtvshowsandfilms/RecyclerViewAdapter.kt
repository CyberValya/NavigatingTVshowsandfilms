package com.example.navigatingtvshowsandfilms

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class RecyclerViewAdapter(private val list: ArrayList<Show>, private val manager: FragmentManager) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_card,
            null
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id?.text = list[position].id.toString()
        holder.name?.text = list[position].name
        holder.progress?.text = list[position].inProgress
        if(list[position].inProgress == "В разработке"){
            holder.progress?.setBackgroundColor(Color.parseColor("#FFE11E"))
        } else if(list[position].inProgress == "Завершен"){
            holder.progress?.setBackgroundColor(Color.parseColor("#48EC4F"))
        }
        holder.watched?.text = list[position].watched
        if(list[position].watched == "Не просмотрено"){
            holder.watched?.setTextColor(Color.RED)
        } else if(list[position].watched == "Просмотрено"){
            holder.watched?.setTextColor(Color.parseColor("#324FE3"))
        }

        val deleteOC: View.OnClickListener = View.OnClickListener {
            val dbh = DataBaseHelper(it.context)
            if(dbh.deleteObject(holder.id?.text.toString()) == 0) {
                Toast.makeText(it.context, "Шоу успешно удалено!", Toast.LENGTH_SHORT).show()
            }
        }
        holder.deleteBtn?.setOnClickListener(deleteOC)

        holder.itemView.setOnClickListener {
            val dialog = DialogFragment(holder.id?.text.toString().toInt())
            dialog.show(manager, "dialog")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image: ImageView? = null
        var name: TextView? = null
        var progress: TextView? = null
        var watched: TextView? = null
        var deleteBtn: Button? = null
        var id: TextView? = null

        init {
            image = itemView.findViewById(R.id.objImage)
            name = itemView.findViewById(R.id.objName)
            progress = itemView.findViewById(R.id.objProgress)
            watched = itemView.findViewById(R.id.objWatched)
            deleteBtn = itemView.findViewById(R.id.deleteBtn)
            id = itemView.findViewById(R.id.objId)
        }
    }
}