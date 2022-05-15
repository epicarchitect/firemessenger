package kolmachikhin.firemessenger.data

typealias UserId = String
typealias UserEmail = String
typealias UserNickname = String

data class UserData(
    val id: UserId,
    val nickname: UserNickname,
    val email: UserEmail
)