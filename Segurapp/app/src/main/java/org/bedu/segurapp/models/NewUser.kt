package org.bedu.segurapp.models

class NewUser(var id: String, var pushNotificationKey: String, var name: String, var telephone: String, var message: String, var channel: Channel? = null, var subscribedToList: List<SubscribedTo> = ArrayList())


