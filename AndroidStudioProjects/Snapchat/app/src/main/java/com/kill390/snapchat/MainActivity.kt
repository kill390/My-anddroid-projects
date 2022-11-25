package com.kill390.snapchat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()
    lateinit var email: EditText
    lateinit var password: EditText

    fun log(view: View){

        if (!email.text.isNullOrEmpty()&&!email.text.isNullOrEmpty()){

            var button =findViewById<Button>(R.id.button)
            button.isEnabled = false
            mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            loged()
                            button.isEnabled = true
                        } else {
                            button.isEnabled = true
                            mAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid.toString()).child("email").setValue(email.text.toString())
                                            loged()
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show()

                                        }


                                    }
                        }
                    }

        }else{
            Toast.makeText(this, "check your information", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        email=findViewById(R.id.emailEditText)
        password=findViewById(R.id.passwordEditText)

        if(mAuth.currentUser != null){
            loged()
        }

    }

    fun loged(){
        var intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
        finish()
    }
}