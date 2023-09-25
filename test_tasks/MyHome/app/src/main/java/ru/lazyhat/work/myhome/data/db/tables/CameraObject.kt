package ru.lazyhat.work.myhome.data.db.tables

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CameraObject : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var snapshot: String = ""
    var room: String? = null
    var favorite: Boolean = false
    var rec: Boolean = false
}