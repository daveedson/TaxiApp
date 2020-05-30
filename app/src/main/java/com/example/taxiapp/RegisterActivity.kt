package com.example.taxiapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.*
import com.parse.ParseInstallation
import com.parse.ParseUser
import com.parse.SignUpCallback
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {







       private  lateinit var regEmail: EditText
       private lateinit var  regUsername:EditText
       private lateinit var  regPassword:EditText
       private lateinit var btnRegister:Button
       private lateinit var  rGroup: RadioGroup
       private lateinit var  radioBtnDriver: RadioButton
       private lateinit var  radioBtnRegPassenger: RadioButton








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        regEmail = findViewById(R.id.regemail_txt)
        regUsername = findViewById(R.id.regUsername_txt)
        regPassword = findViewById(R.id.regpassword_txt)
        btnRegister = findViewById(R.id.btnRegister)
        rGroup = findViewById(R.id.radioGroup)
        radioBtnDriver = findViewById(R.id.RbtDriverReg)
        radioBtnRegPassenger = findViewById(R.id.RbtnPassengerReg)


        ParseInstallation.getCurrentInstallation().saveInBackground()
        if (ParseUser.getCurrentUser() != null){

        }




        btnRegister.setOnClickListener {

            //Code to validate TextFields
            if (regemail_txt.text.isEmpty()||regpassword_txt.text.isEmpty()|| regUsername_txt.text.isEmpty()){
                Toast.makeText(this,"Input all required Fields",Toast.LENGTH_SHORT).show()
            }else if (!radioBtnDriver.isChecked && !radioBtnRegPassenger.isChecked){
                Toast.makeText(this,"Are you a driver or a passenger",Toast.LENGTH_SHORT).show()

            }else  if  (regEmail.text.toString().length<6 ||regPassword.text.toString().length<6|| regUsername.text.toString().length<6){
                Toast.makeText(this,"All Fields should not be less than 6 characters",Toast.LENGTH_SHORT).show()
            }else{
                register()
            }
        }




   }

    private  fun register(){


        try {

            regemail_txt.error = null
            regpassword_txt.error = null

            var user = ParseUser()
            user.setPassword(regPassword.text.toString())
            user.email = regemail_txt.text.toString()
            user.username = regUsername.text.toString()

            if (radioBtnRegPassenger.isChecked){
                user.put("as","Passenger")
            }else if (radioBtnDriver.isChecked){
                user.put("as","Passenger")

            }

            //Code to display progress dialog
            var progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Signing up " + regUsername.text.toString())
            progressDialog.show()

            user.signUpInBackground(SignUpCallback {
                if (it == null){
                   // Toast.makeText(this,"Signed Up Success",Toast.LENGTH_SHORT).show()
                    alertDialogDisplayer("Account Created Successfully !","Please Verify email before you login",false)

                }else{
                    ParseUser.logOut()
                    alertDialogDisplayer("Error ! Account Creation Failed", "Account could not be created:${it.message}",true)
                }
                progressDialog.dismiss()
            })

        } catch (e: Exception){

        }



    }

         fun alertDialogDisplayer( title :String?, message: String?, error: Boolean?){

        var builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                if (!error!!){
                    var intent = Intent(this,Login::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK / Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)


                }
            }

        var ok = builder.create()
        ok.show()
    }


        private fun TransitionToPassengerActivity(){
            if(ParseUser.getCurrentUser() != null){

                if (ParseUser.getCurrentUser().get("as") == "Passenger"){
                    var intent = Intent(this,PassengerActivity::class.java)
                    startActivity(intent)
                }
            }
        }

}
















