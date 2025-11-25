package com.nano.min.faker

import com.nano.min.network.Chat
import com.nano.min.network.Message
import com.nano.min.network.User
import io.github.serpro69.kfaker.Faker
import java.util.Random
import kotlin.random.nextInt
import kotlin.random.nextLong

private val faker = Faker()
private val r = kotlin.random.Random(42)

private fun longId(): Long {
    return r.nextLong(0L..Long.MAX_VALUE)
}

private fun personName(): String {
    return faker.name.firstName() + " " + faker.name.lastName()
}

fun makeUser(): User {
    val id = longId()
    val name = personName()
    val email = faker.internet.email(name)
    val username = email.substringBefore("@")
    return User(
        id = id,
        name = name,
        username = username,
        email = email
    )
}

fun makeChat(users: List<User>): Chat {
    val id = longId()
    return Chat(
        id = id,
        title = faker.string.letterify("C_######"),
        users = users,
    )
}

private fun createMessage(chat: Chat): Message {
    return Message(
        id = longId(),
        senderId = chat.users.random().id,
        timestamp = System.currentTimeMillis() - r.nextLong(0L..1_000_000L)
    )
}