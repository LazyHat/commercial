package ru.lazyhat.work.activitytracker.data.contacts

import ru.lazyhat.work.activitytracker.data.models.Contact

interface ContactsService {
    suspend fun getContacts(): List<Contact>?
}