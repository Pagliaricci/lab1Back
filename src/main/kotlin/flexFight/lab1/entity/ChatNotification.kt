package flexFight.lab1.entity



data class ChatNotification (
    val id: String,
    val senderId: String,
    val recipientId: String,
    val content : String,
)