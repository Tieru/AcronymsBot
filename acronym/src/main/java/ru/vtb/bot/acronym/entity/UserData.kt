package ru.vtb.bot.acronym.entity

data class UserData(
    val id: Long,
    val username: String?,
    val firstName: String,
    val lastName: String?,
) {
    fun concatUsernameAndId(): String {
        val title = if (username.isNullOrBlank()) {
            "$firstName ${lastName ?: ""}".trim()
        } else {
            "@$username"
        }

        return formatUsernameAndId(username = title, id = id)
    }

    val isAdmin: Boolean by lazy {
        id == 45174710L || id == 229627617L || id == 286761662L
    }

    companion object {
        fun formatUsernameAndId(username: String, id: Long): String {
            return "$username / $id"
        }
    }
}
