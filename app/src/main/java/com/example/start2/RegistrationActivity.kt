package com.example.start2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer

import com.example.start2.auth.BirthdayStepFragment
import com.example.start2.home.NavigatorActivity

class RegistrationActivity : AppCompatActivity(), RegistrationStepsListener{

    private val TAG = "RegistrationActivity"
    private val registrationViewModel by viewModels<RegistrationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val userName = intent.getStringExtra("userName")
        userName?.let {
            registrationViewModel.saveUsername(it)
        }

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {


            if (supportFragmentManager.findFragmentById(R.id.fragment_container) is BirthdayStepFragment) {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                finish() // Eğer com.example.start2.auth.MainFragment Activity'nin bir parçası değilse Activity'yi kapat
            } else {
                supportFragmentManager.popBackStack()
            }
        }

        Log.d(TAG, "onCreate called")
        // changes with the response will be made here
        registrationViewModel.response.observe(this, Observer { responseBody ->
            // Logic to handle the response
            // If the response meets certain conditions, start a new activity
            if (responseBody != "Req") {
                val intent = Intent(this, NavigatorActivity::class.java)
                // Optionally, you can pass data to the new activity
                intent.putExtra("responseKey", responseBody)
                startActivity(intent)
                finish()  // If you want to close the current activity
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BirthdayStepFragment())
                .addToBackStack(null)
                .commit()

            Log.d(TAG, "Initial fragment (BirthdayStepFragment) is set")
        }

    }


    override fun onBirthdaySelected(birthday: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsernameStepFragment())
            .addToBackStack(null)
            .commit()
        Log.d(TAG, "DOĞUM GÜNÜMDÜ: $birthday")

        registrationViewModel.saveBirthday(birthday)

    }

    override fun onEmailSelected(email: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PasswordStepFragment())
            .addToBackStack(null)
            .commit()
        Log.d(TAG, "MAİLİMDİR BU BENİM:  $email")


        registrationViewModel.saveEmail(email)
    }

    override fun onPasswordSelected(password: String) {
        registrationViewModel.savePassword(password)
            Log.d(TAG, "PASSWORDUMDUR BU BENİM:  $password")

        registrationViewModel.sendUserDataToApi()

    }

    override fun onSpotifySelected() {
        registrationViewModel.sendSpotifyIntent()
    }








}
