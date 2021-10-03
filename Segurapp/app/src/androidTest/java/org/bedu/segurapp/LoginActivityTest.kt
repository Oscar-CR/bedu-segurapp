package org.bedu.segurapp

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.bedu.segurapp.helpers.emailValidator
import org.bedu.segurapp.helpers.passwordValidator
import org.bedu.segurapp.helpers.phoneNumberValidator
import org.bedu.segurapp.ui.login.LoginActivity
import org.junit.Assert
import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)
    private val etLoginEmail = onView(withId(R.id.etLoginEmail))
    private val etLoginPassword = onView(withId(R.id.etLoginPassword))
    private val etTelephone = onView(withId(R.id.etRegisterTelephone))
    private val btCreate = onView(withId(R.id.btCreate))



    @Test
   fun validateEmail_returnsFalse(){
       etLoginEmail.perform(typeText("prueba.com"), closeSoftKeyboard())
       etLoginEmail.check { view, _ ->
           Assert.assertEquals(false, emailValidator(view as EditText))
       }
   }

    @Test
    fun validateEmail_returnsTrue(){
        etLoginEmail.perform(typeText("usuario@bedu.com"), closeSoftKeyboard())
        etLoginEmail.check { view, _ ->
            Assert.assertEquals(true, emailValidator(view as EditText))
        }
    }


    @Test
    fun validatePassword_returnsFalse(){
        etLoginPassword.perform(typeText("unsecuredpassword"), closeSoftKeyboard())
        etLoginPassword.check { view , _ ->
            Assert.assertEquals(false, passwordValidator(view as EditText))
        }
    }

    @Test
    fun validatePassword_returnsTrue(){
        etLoginPassword.perform(typeText("securedP4sword$"), closeSoftKeyboard())
        etLoginPassword.check { view , _ ->
            Assert.assertEquals(true, passwordValidator(view as EditText))
        }
    }

    @Test
    fun validate_telephone_returnsTrue(){
        btCreate.perform(scrollTo(), click())
        etTelephone.perform(scrollTo(), typeText("4774955345"), closeSoftKeyboard())
        etTelephone.check { view , _ ->
            Assert.assertEquals(true, phoneNumberValidator(view as EditText))
        }
    }

}