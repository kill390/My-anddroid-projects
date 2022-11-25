package com.kill390.snapchat


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class SnapsActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()
    var emails = ArrayList<String>()
    var snapKey = ArrayList<DataSnapshot>()
    lateinit var mAdView:AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)

        MobileAds.initialize(this) { }
        mAdView = findViewById(R.id.adView)
        val adRequest: AdRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        setTitle("Snaps Feed")

        var listView = findViewById<ListView>(R.id.snapsListView)
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,emails)
        listView.adapter =adapter

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString())
                .child("snaps").addChildEventListener(object :ChildEventListener{
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        emails.add(p0.child("from").value.toString())
                        snapKey.add(p0)
                        adapter.notifyDataSetChanged()
                    }
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                    override fun onChildChanged(p0: DataSnapshot, p1: String?){}
                    override fun onChildRemoved(p0: DataSnapshot) {
                        var index = 0
                        for (snap:DataSnapshot in snapKey){
                            if (snap.key == p0.key){
                                emails.removeAt(index)
                                snapKey.removeAt(index)
                                adapter.notifyDataSetChanged()
                            }
                            index++
                        }
                    }
                })

        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            val intent = Intent(this,ViewSnapActivity::class.java)
            intent.putExtra("message",snapKey.get(position).child("message").value.toString())
            intent.putExtra("imageURL",snapKey.get(position).child("imageUrl").value.toString())
            intent.putExtra("imageName",snapKey.get(position).child("imageName").value.toString())
            intent.putExtra("key",snapKey.get(position).key.toString())
            startActivity(intent)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.createSnap) {
            val intent = Intent(this, CreateSnapActivity::class.java)
            startActivity(intent)

        }else if (item.itemId == R.id.logout) {
            mAuth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
