package com.example.persondatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

 private lateinit var database:DatabaseReference

    private var users = ArrayList<String>()

    private val changeListener: ValueEventListener=object :ValueEventListener{

        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.hasChildren()){
                var count = snapshot.childrenCount

                users.clear()

                for(child in snapshot.children){
                    val holdData = child.getValue().toString()

                    users.add(holdData)

                    Log.i("child", child.key)
                    Log.i("value", child.getValue().toString())
                }
                listViewItems.adapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_list_item_1,users)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //database = Firebase.database.reference
        database = Firebase.database.getReference("/users")
        database.addValueEventListener(changeListener)


        btnSave.setOnClickListener {
            var fname = editTextFirstName.text.toString()
            var lname = editTextLastName.text.toString()
            var address = editTextAddress.text.toString()
            var id = editTextIDNumber.text.toString()

            var currUser = Person(fname,lname,address,id)

            database.child(id).setValue(currUser)

            editTextFirstName.setText("")
            editTextLastName.setText("")
            editTextAddress.setText("")
            editTextAddress.setText("")
            editTextIDNumber.setText("")

        }



    }

    override fun onDestroy() {
        super.onDestroy()
        database.removeEventListener(changeListener)
    }


}