package com.songlib.domain.repository

import android.content.*
import com.songlib.core.utils.PrefConstants
import com.songlib.data.models.*
import com.songlib.data.sources.local.*
import com.songlib.data.sources.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.*
import androidx.core.content.edit

@Singleton
class BookRepository @Inject constructor(
    context: Context,
    private val apiService: ApiService
)  {
    private val prefs = context.getSharedPreferences(PrefConstants.PREFERENCE_FILE, Context.MODE_PRIVATE)

    private var booksDao: BookDao?

    init {
        val db = AppDatabase.getDatabase(context)
        booksDao = db?.booksDao()
    }

    fun getBooks(): Flow<List<Book>> = flow {
        val books = apiService.getBooks()
        emit(books)
    }

    suspend fun saveBook(book: Book) {
        withContext(Dispatchers.IO) {
            booksDao?.insert(book)
        }
    }

    suspend fun getAllBooks(): List<Book> {
        var allBooks: List<Book>
        withContext(Dispatchers.IO) {
            allBooks = booksDao?.getAll() ?: emptyList()
        }
        return allBooks
    }

    fun savePrefs(selectedBooks: String) {
        prefs.edit { putString(PrefConstants.SELECTED_BOOKS, selectedBooks) }
        prefs.edit { putBoolean(PrefConstants.DATA_SELECTED, true) }
    }
}