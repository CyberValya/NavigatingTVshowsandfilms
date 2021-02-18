package com.example.navigatingtvshowsandfilms

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class DialogFragment() : DialogFragment(), Parcelable {
    var _id: Int = 0

    constructor(parcel: Parcel) : this() {
        _id = parcel.readInt()
    }

    constructor(id: Int) : this() {
        _id = id
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val dbh = DataBaseHelper(it.applicationContext)
            val type = dbh.getType(_id)
            val builder = AlertDialog.Builder(it)
            if(type == "сериал") {
                builder.setTitle("Что хотите изменить?")
                    .setMessage("Сменить у шоу:")
                    .setCancelable(true)
                    .setPositiveButton("Просмотр") { dialog, id ->
                        dbh.updateObject(_id, false, true)
                        Toast.makeText(it.applicationContext, "Просмотр изменен!", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Статус",
                        DialogInterface.OnClickListener { dialog, id ->
                            dbh.updateObject(_id, true, false)
                            Toast.makeText(it.applicationContext, "Статус изменен!", Toast.LENGTH_SHORT).show()
                        })
            }else{
                builder.setTitle("Хотите изменить статус просмотра?")
                    .setMessage("Сменить у шоу:")
                    .setCancelable(true)
                    .setPositiveButton("Просмотр") { dialog, id ->
                        dbh.updateObject(_id, false, true)
                        Toast.makeText(it.applicationContext, "Просмотр изменен!", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DialogFragment> {
        override fun createFromParcel(parcel: Parcel): DialogFragment {
            return DialogFragment(parcel)
        }

        override fun newArray(size: Int): Array<DialogFragment?> {
            return arrayOfNulls(size)
        }
    }
}