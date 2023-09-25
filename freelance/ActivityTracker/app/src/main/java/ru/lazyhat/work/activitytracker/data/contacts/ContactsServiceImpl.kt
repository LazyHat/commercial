package ru.lazyhat.work.activitytracker.data.contacts

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import ru.lazyhat.work.activitytracker.data.models.Contact

private val PROJECTION = arrayOf(
    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
    ContactsContract.Contacts.DISPLAY_NAME,
    ContactsContract.CommonDataKinds.Phone.NUMBER
)

class ContactsServiceImpl(private val context: Context) : ContactsService {
    override suspend fun getContacts(): List<Contact>? {
        var contactList: MutableList<Contact>? = null
        val cr: ContentResolver = context.contentResolver

        val cursor = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor != null) {
            contactList = mutableListOf()
            val mobileNoSet = HashSet<String>()

            cursor.use {

                val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex =
                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                while (it.moveToNext()) {

                    val number = it.getString(numberIndex).replace(" ", "")
                    val name = it.getString(nameIndex)

                    if (!mobileNoSet.contains(number)) {
                        contactList.add(
                            Contact(
                                name,
                                number
                            )
                        )

                        mobileNoSet.add(number)

                        Log.d(
                            "CONTACT_SERVICE", "onCreateView  Phone Number: name = " + name
                                    + " No = " + number
                        )
                    }
                }
            }
        }
        return contactList?.map { contact ->
            Contact(
                contact.name,
                contact.phone.filter { it.isDigit() || it in listOf('+', '#', '*') }.let {
                    if (it[0] == '8')
                        "+7${it.drop(1)}"
                    else
                        it
                })
        }
    }
}