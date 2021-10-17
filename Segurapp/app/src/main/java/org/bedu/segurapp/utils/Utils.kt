package org.bedu.segurapp.utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

fun getNameByUri(uri: Uri, contentResolver: ContentResolver): String? {

    var name: String? = null
    val currentContact = contentResolver.query(
        uri, arrayOf(ContactsContract.Contacts.DISPLAY_NAME), null, null, null)

    if (currentContact != null && currentContact.moveToFirst())  name = currentContact.getString(0)
    currentContact?.close()
    return name
}

fun getPhoneByUri(uri: Uri, contentResolver: ContentResolver): String? {

    var id: String? = null
    var phone: String? = null

    val contactCursor: Cursor? = contentResolver.query(
        uri, arrayOf(ContactsContract.Contacts._ID),
        null, null, null)

    if (contactCursor != null && contactCursor.moveToFirst()) id = contactCursor.getString(0)

    contactCursor?.close()

    val selectionArgs = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ? AND ${ContactsContract.CommonDataKinds.Phone.TYPE}= ${ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE}"

    val phoneCursor: Cursor? = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
        selectionArgs, arrayOf(id),
        null
    )

    if (phoneCursor != null && phoneCursor.moveToFirst()) phone = phoneCursor.getString(0)

    phoneCursor?.close()

    return phone
}


class Constants {
    companion object{
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAAuiMnb7A:APA91bHsAyEsQo-ywac3jtjyHnwxWDvUMaivD0_S_vQHbsg20vG2DHUzY3exoHIln08wkovoDyf7SyxS3-KW4-99vWpITNI6_hIKlTLd26a9rtSsNDVM4BcSXV6Mv3jDqOWfIX11P9Wf"
        const val CONTENT_TYPE = "application/json"
    }
}