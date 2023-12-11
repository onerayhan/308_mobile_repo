package com.example.start2.auth

interface RegistrationStepsListener {

        fun onBirthdaySelected(birthday: String)
        fun onEmailSelected(username: String)
        fun onPasswordSelected(password: String)
        fun onSpotifySelected()
    }
