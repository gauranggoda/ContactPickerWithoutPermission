package com.example.contactpicker

import android.app.Activity
import android.content.Intent
import android.database.DatabaseUtils
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.act_contact_picker.*

class ContactPickerAct : AppCompatActivity() {
    private val PICK_CONTACT: Int = 2001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_contact_picker)
        btn_pick_contact.setOnClickListener {
            openSystemContactPicker()
        }
    }

    private fun openSystemContactPicker() {
        // open a system contact for selecting specific contact
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, PICK_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
/*       fetch selected contact details from onActivityResult method*/
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {

            /* add Projection to make query and get filtered data only*/
            /*val projection = arrayOf(
                ContactsContract.Contacts.Entity.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
            )*/

            val cursor = data?.data?.let {
                contentResolver?.query(it, null, null, null, null)
//              add projection for filtered response
/*              contentResolver?.query(it, projection, null, null, null)*/
            }
            if (cursor?.moveToFirst() == true) {
                val selectedContactName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val selectedContactNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))
                tv_selected_contact_name.text = "$selectedContactName : $selectedContactNumber"
            } else {
                // something went wrong while selecting contact
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

/*            check the Logcat below line will print all the value of cursor.*/
            DatabaseUtils.dumpCursor(cursor)
        }
    }

}
