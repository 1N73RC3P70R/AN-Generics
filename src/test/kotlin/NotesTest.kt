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
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        assertEquals(1, notesService.get("Author").size)
    }

    @Test
    fun testCreateComment() {
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        val comment = Comment(1, 1, "Comment", "User")
        notesService.createComment(1, comment)
        assertEquals(1, notesService.getComments(1).size)
    }

    @Test
    fun testDeleteNote() {
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        notesService.delete(1)
        assertTrue(notesService.get("Author").isEmpty())
    }

    @Test
    fun testDeleteComment() {
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        val comment = Comment(1, 1, "Comment", "User")
        notesService.createComment(1, comment)
        notesService.deleteComment(1)
        assertTrue(notesService.getComments(1).isEmpty())
    }

    @Test
    fun testEditNote() {
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        notesService.edit(1, "New Title", "New Content")
        val updatedNote = notesService.getById(1)
        assertEquals("New Title", updatedNote?.title)
        assertEquals("New Content", updatedNote?.content)
    }

    @Test
    fun testEditComment() {
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        val comment = Comment(1, 1, "Comment", "User")
        notesService.createComment(1, comment)
        notesService.editComment(1, "New Comment")
        val updatedComment = notesService.getComments(1).firstOrNull()
        assertEquals("New Comment", updatedComment?.content)
    }

    @Test
    fun testGetNotesByAuthor() {
        notesService.add(Note(1, "Title1", "Content1", "Author1"))
        notesService.add(Note(2, "Title2", "Content2", "Author2"))
        notesService.add(Note(3, "Title3", "Content3", "Author1"))
        assertEquals(2, notesService.get("Author1").size)
    }

    @Test
    fun testGetNoteById() {
        notesService.add(Note(1, "Title1", "Content1", "Author"))
        val note = notesService.getById(1)
        assertEquals("Title1", note?.title)
        assertEquals("Content1", note?.content)
    }

    @Test
    fun testGetCommentsForNote() {
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        notesService.createComment(1, Comment(1, 1, "Comment1", "User1"))
        notesService.createComment(1, Comment(2, 1, "Comment2", "User2"))
        assertEquals(2, notesService.getComments(1).size)
    }

    @Test
    fun testRestoreDeletedComment() {
        val note = Note(1, "Title", "Content", "Author")
        notesService.add(note)
        val comment = Comment(1, 1, "Comment", "User")
        notesService.createComment(1, comment)
        notesService.deleteComment(1)
        notesService.restoreComment(1)
        assertFalse(notesService.getComments(1).firstOrNull()?.deleted ?: false)
    }
}
