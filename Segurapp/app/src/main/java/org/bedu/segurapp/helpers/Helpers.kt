package org.bedu.segurapp.helpers

import android.content.Context
import android.text.InputType.*
import android.widget.EditText
import org.bedu.segurapp.R
import org.bedu.segurapp.models.EmptyFieldResponse

fun userIsLogged(): Boolean = false

fun confirmPasswordMatcher(password: String, confirmPassword: String): Boolean =
    password == confirmPassword

fun makeValidations(fields: Array<EditText>, resources: Context): Boolean {

    val emptyFieldsValidatorResult = validateEmptyFields(fields)

    if (emptyFieldsValidatorResult.isEmpty()) {

        val dataTypeValidatorResult = validateDataType(fields)

        if (dataTypeValidatorResult.isNotEmpty()) {
            dataTypeValidatorResult.forEach {
                it.editText.error = resources.getString(it.messageId)
            }
            return false
        }
    } else {
        emptyFieldsValidatorResult.forEach {
            it.error = resources.getString(R.string.required_field_hint)
        }
        return false
    }

    return true
}

private fun validateEmptyFields(fields: Array<EditText>): List<EditText> =
    fields.filter { it.text.toString().trim() == "" }


private fun validateDataType(fields: Array<EditText>): List<EmptyFieldResponse> {

    val emptyFieldResponseList: MutableList<EmptyFieldResponse> = ArrayList()

    fields.forEach { item ->

        when (item.inputType - 1) {
            TYPE_TEXT_VARIATION_PHONETIC, TYPE_CLASS_PHONE, TYPE_CLASS_NUMBER -> {
                if (!phoneNumberValidator(item)) {
                    emptyFieldResponseList.add(
                        EmptyFieldResponse(
                            R.string.required_telephone_hint,
                            item)
                    )
                    return@forEach
                }
            }

            TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS, TYPE_TEXT_VARIATION_EMAIL_ADDRESS, TYPE_TEXT_VARIATION_EMAIL_SUBJECT,
            -> {
                if (!emailValidator(item)) {
                    emptyFieldResponseList.add(EmptyFieldResponse(R.string.required_email_hint,
                        item))
                    return@forEach
                }
            }

            TYPE_TEXT_VARIATION_PASSWORD -> {
                if (!passwordValidator(item)) {
                    emptyFieldResponseList.add(EmptyFieldResponse(R.string.required_strong_password_hint,
                        item))
                    return@forEach
                }
            }
        }
    }

    return emptyFieldResponseList
}

private fun phoneNumberValidator(mEditText: EditText): Boolean =
    mEditText.text.toString().length == 10

private fun emailValidator(mEditText: EditText): Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(mEditText.text.toString()).matches()

private fun passwordValidator(mEditText: EditText): Boolean {
    val passwordRegex: Regex =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}".toRegex()
    return mEditText.text.toString().matches(passwordRegex)
}
