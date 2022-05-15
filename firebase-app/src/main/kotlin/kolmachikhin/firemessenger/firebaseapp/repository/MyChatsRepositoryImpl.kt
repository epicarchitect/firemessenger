package kolmachikhin.firemessenger.firebaseapp.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kolmachikhin.firemessenger.data.Chat
import kolmachikhin.firemessenger.data.ChatId
import kolmachikhin.firemessenger.data.UserId
import kolmachikhin.firemessenger.repository.MyChatsRepository
import kolmachikhin.firemessenger.repository.MyChatsState
import kolmachikhin.firemessenger.repository.MyUserRepository
import kolmachikhin.firemessenger.repository.MyUserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

class MyChatsRepositoryImpl(
    private val myUserRepository: MyUserRepository
) : MyChatsRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var chatsReference: DatabaseReference? = null
    private var userChatIdsReference: DatabaseReference? = null
    private val chatIdsState = MutableStateFlow<List<ChatId>>(emptyList())
    private val mutableState = MutableStateFlow<MyChatsState>(MyChatsState.Loading())
    override val state = mutableState

    private val userChatIdsEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                chatIdsState.value = snapshot.children.map { it.toString() }
            } catch (t: Throwable) {
                Firebase.auth.signOut()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            mutableState.value = MyChatsState.Loading()
        }
    }

    init {
        myUserRepository.state.onEach {
            when (it) {
                is MyUserState.Loaded -> {

                }
                else -> {
                    mutableState.value = MyChatsState.Loading()
                }
            }
        }.launchIn(coroutineScope)
    }

    override suspend fun createChatWithUser(userId: UserId): ChatId {
        val chatId = generateChatId()
        val myUserId = Firebase.auth.currentUser!!.uid
        checkNotNull(userChatIdsReference).setValue(chatIdsState.value + userId)

        val receiverReference = Firebase.database.getReference("user-chats").child(userId)
        receiverReference.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatIds = snapshot.children.map { it.toString() }
                    receiverReference.setValue(chatIds + myUserId)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )

        return chatId
    }

    override suspend fun sendMessage(chatId: ChatId, message: Chat.Message) {

    }

    private fun generateChatId() = UUID.randomUUID().toString()
}