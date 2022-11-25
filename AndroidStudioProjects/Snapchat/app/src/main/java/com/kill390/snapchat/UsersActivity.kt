package com.kill390.snapchat

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class UsersActivity : AppCompatActivity() {

    lateinit var listView: ListView
    var emails: ArrayList<String> = ArrayList()
    var uid: ArrayList<String> = ArrayList()
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        setTitle("select Users")

        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(object: ChildEventListener{

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                emails.add(p0.child("email").value.toString())
                uid.add(p0.key.toString())
                adapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(p0: DataSnapshot) {}




        })

        listView =findViewById(R.id.listView)
        adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        listView.adapter = adapter

        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            var map = mapOf<String,String>("from" to FirebaseAuth.getInstance().currentUser?.email.toString()
                    , "imageName" to intent.getStringExtra("imagename")
                    , "imageUrl" to intent.getStringExtra("imageurl")
                    , "message" to intent.getStringExtra("message") )

            FirebaseDatabase.getInstance().getReference().child("users").child(uid.get(position)).child("snaps").push().setValue(map)

            val intent = Intent(this, SnapsActivity::class.java)
            startActivity(intent)
            finish()

        })
    }
}
