import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NotesServiceTest {

    private lateinit var notesService: NotesService<Note, Comment>

    @Before
    fun setUp() {
        notesService = NotesService()
    }

    @Test
    fun testAddNote() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        assertEquals(1, notesService.get("Автор").size)
    }

    @Test
    fun testCreateComment() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        val comment = Comment(1, 1, "Комментарий", "Пользователь")
        notesService.createComment(1, comment)
        assertEquals(1, notesService.getComments(1).size)
    }

    @Test
    fun testDeleteNote() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        notesService.delete(1)
        assertTrue(notesService.get("Автор").isEmpty())
    }

    @Test
    fun testDeleteComment() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        val comment = Comment(1, 1, "Комментарий", "Пользователь")
        notesService.createComment(1, comment)
        notesService.deleteComment(1)
        assertTrue(notesService.getComments(1).isEmpty())
    }

    @Test
    fun testEditNote() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        notesService.edit(1, "Новый Заголовок", "Новое Содержание")
        val updatedNote = notesService.getById(1)
        assertEquals("Новый Заголовок", updatedNote?.title)
        assertEquals("Новое Содержание", updatedNote?.content)
    }

    @Test
    fun testEditComment() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        val comment = Comment(1, 1, "Комментарий", "Пользователь")
        notesService.createComment(1, comment)
        notesService.editComment(1, "Новый Комментарий")
        val updatedComment = notesService.getComments(1).firstOrNull()
        assertEquals("Новый Комментарий", updatedComment?.content)
    }

    @Test
    fun testGetNotesByAuthor() {
        notesService.add(Note(1, "Заголовок 1", "Содержание 1", "Автор 1"))
        notesService.add(Note(2, "Заголовок 2", "Содержание 2", "Автор 2"))
        notesService.add(Note(3, "Заголовок 3", "Содержание 3", "Автор 1"))
        assertEquals(2, notesService.get("Автор 1").size)
    }

    @Test
    fun testGetNoteById() {
        notesService.add(Note(1, "Заголовок 1", "Содержание 1", "Автор"))
        val note = notesService.getById(1)
        assertEquals("Заголовок 1", note?.title)
        assertEquals("Содержание 1", note?.content)
    }

    @Test
    fun testGetCommentsForNote() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        notesService.createComment(1, Comment(1, 1, "Комментарий 1", "Пользователь 1"))
        notesService.createComment(1, Comment(2, 1, "Комментарий 2", "Пользователь 2"))
        assertEquals(2, notesService.getComments(1).size)
    }

    @Test
    fun testRestoreDeletedComment() {
        val note = Note(1, "Заголовок", "Содержание", "Автор")
        notesService.add(note)
        val comment = Comment(1, 1, "Комментарий", "Пользователь")
        notesService.createComment(1, comment)
        notesService.deleteComment(1)
        notesService.restoreComment(1)
        assertFalse(notesService.getComments(1).firstOrNull()?.deleted ?: false)
    }
}