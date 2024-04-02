import java.util.*

fun main() {
    val notesService = NotesService<Note, Comment>()

    notesService.add(Note(1, "Заголовок", "Содержание", "пользователь1"))

    val noteId = notesService.get("пользователь1").firstOrNull()?.id ?: return
    notesService.createComment(noteId, Comment(noteId, 1, "Комментарий", "пользователь 1"))

    notesService.edit(noteId, "Обновленный заголовок", "Обновленное содержание.")

    notesService.delete(noteId)

    val commentId = notesService.getComments(noteId).firstOrNull()?.id ?: return
    notesService.restoreComment(commentId)

    val user1Notes = notesService.get("пользователь 1")
    println("Заметки пользователя 1:")
    user1Notes.forEach { println(it) }

    val retrievedNote = notesService.getById(noteId)
    println("Полученная заметка: $retrievedNote")

    val comments = notesService.getComments(noteId)
    println("Комментарии к заметке $noteId:")
    comments.forEach { println(it) }
}

data class Note(
    val id: Int = generateId(),
    var title: String,
    var content: String,
    val author: String,
    val createdAt: Date = Date(),
    var deleted: Boolean = false,
    val comments: MutableList<Comment> = mutableListOf()
)

data class Comment(
    val id: Int = generateId(),
    val noteId: Int,
    var content: String,
    val author: String,
    val createdAt: Date = Date(),
    var deleted: Boolean = false
)

var idCounter = 0
fun generateId(): Int {
    return idCounter++
}

class NotesService<T : Note, U : Comment> {
    private val notes: MutableList<T> = mutableListOf()

    fun add(note: T) {
        notes.add(note)
    }

    fun createComment(noteId: Int, comment: U) {
        val note = getNoteById(noteId)
        if (note != null) {
            note.comments.add(comment)
        }
    }

    fun delete(noteId: Int) {
        val note = getNoteById(noteId)
        note?.deleted = true
    }

    fun deleteComment(commentId: Int) {
        val comment = getCommentById(commentId)
        comment?.deleted = true
    }

    fun edit(noteId: Int, newTitle: String, newContent: String) {
        val note = getNoteById(noteId)
        note?.apply {
            title = newTitle
            content = newContent
        }
    }

    fun editComment(commentId: Int, newContent: String) {
        val comment = getCommentById(commentId)
        comment?.apply {
            content = newContent
        }
    }

    fun get(author: String): List<T> {
        return notes.filter { !it.deleted && it.author == author }
    }

    fun getById(noteId: Int): T? {
        return notes.find { it.id == noteId && !it.deleted }
    }

    fun getComments(noteId: Int): List<Comment> {
        val note = getNoteById(noteId)
        return note?.comments?.filter { !it.deleted } ?: emptyList()
    }

    fun restoreComment(commentId: Int) {
        val comment = getCommentById(commentId)
        comment?.deleted = false
    }

    private fun getNoteById(noteId: Int): T? {
        return notes.find { it.id == noteId }
    }

    private fun getCommentById(commentId: Int): Comment? {
        return notes.flatMap { it.comments }.find { it.id == commentId }
    }
}
