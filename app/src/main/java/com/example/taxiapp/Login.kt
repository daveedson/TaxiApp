package com.example.taxiapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.github.florent37.materialtextfield.MaterialTextField

import com.parse.LogInCallback
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception

class Login : AppCompatActivity() {


        private  lateinit var emailf :EditText
    private  lateinit var passwordf :EditText
    private  lateinit var btnLogin :Button
    private  lateinit var txtSignUp :TextView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailf = findViewById(R.id.email_txt)
        passwordf = findViewById(R.id.password_txt)
        btnLogin = findViewById(R.id.btnLogin)
        txtSignUp = findViewById(R.id.txtviewSignUp)

        txtSignUp.setOnClickListener {

            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



        if(ParseUser.getCurrentUser() != null){
            TransitionToPassengerActivity()

        }

        btnLogin.setOnClickListener {

            if (email_txt.text.toString().isEmpty()|| password_txt.text.toString().isEmpty()){
                Toast.makeText(this,"Input all required Fields",Toast.LENGTH_SHORT).show()
            }else if (email_txt.text.toString().length<6||password_txt.text.toString().length<6){
                Toast.makeText(this,"All Fields should not be less than 6 characters",Toast.LENGTH_SHORT).show()
            }else{
                signUp()
            }
    }





    }

    //Method to Signup user to parse server
  private  fun signUp(){
    //    var registerActivity  = RegisterActivity()

        var user = ParseUser()
        ParseUser.logInInBackground(email_txt.text.toString(),password_txt.text.toString()
        ) { user, e ->
            if (user != null){
                TransitionToPassengerActivity()
              alertDialogDisplayer("Login Successful","Welcome"  + "!",false)

            }else{
                ParseUser.logOut()
            alertDialogDisplayer("Login Failed", e.message + "Please re-try",true)
            }
            finish()
        }
    }


    //This method displays the alert dialog to the user after log in successfull
    fun alertDialogDisplayer( title :String?, message: String?, error: Boolean?){

        var builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
                if (!error!!){
//                    var intent = Intent(this,Login::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK / Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
                }
                finish()
            }

        var ok = builder.create()
        ok.show()
    }

    private fun TransitionToPassengerActivity(){
        if(ParseUser.getCurrentUser() != null){

            if (ParseUser.getCurrentUser().get("as") == "Passenger"){
                var intent = Intent(this,PassengerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


}
