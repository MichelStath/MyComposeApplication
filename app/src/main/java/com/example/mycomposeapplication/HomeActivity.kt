package com.example.mycomposeapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var books = arrayListOf<Book>()

        val urlA = R.drawable.offline_image
        val urlB = R.drawable.online_mage

        books.add(Book(1,"title1",R.drawable.online_mage))
        books.add(Book(2,"title2",R.drawable.online_mage))
        books.add(Book(3,"title3",R.drawable.online_mage))
        books.add(Book(4,"title4",R.drawable.online_mage))
        books.add(Book(5,"title4",R.drawable.online_mage))

        setContent {
            MyHomeUI(books){

                Log.i("testList",books[it.id].toString())
                val bookPosition = books.indexOf(it)
                Log.i("Book Title",it.title)
                Log.i("Book URL",it.imageURL.toString())
                val updatedBook = books[bookPosition].apply {
                    if(imageURL == urlA){
                        it.imageURL = urlB
                        Toast.makeText(applicationContext,"Delete Book ${it.title}",Toast.LENGTH_SHORT).show()
                    }else{
                        it.imageURL = urlA
                        Toast.makeText(applicationContext,"Downloading Book ${it.title}",Toast.LENGTH_SHORT).show()
                    }
                }
                //changes image but not refresh view
                books[bookPosition] = updatedBook
                Log.i("Updated book Title",books[bookPosition].title)
                Log.i("Updated book URL",books[bookPosition].imageURL.toString())

            }
        }
    }
}

data class Book(
    var id: Int,
    var title: String,
    var imageURL: Int
)

@Composable
fun ImageLoader(imageURL: Int){
    Image(
        painter = painterResource(imageURL),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(150.dp))
}

@Composable
fun ListItem(book: Book, selectedBook:(Book)->Unit){
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { selectedBook(book) },
        elevation = 8.dp
    ) {
        Column() {
            ImageLoader(imageURL = book.imageURL)
            Text(text = book.title,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )
            Divider(color = Color.Red)
        }
    }
}


@Composable
fun MyHomeUI(books: List<Book>,selected: (Book)->Unit){
    LazyColumn{
        items(
            items = books,
            itemContent = {
                ListItem(it, selected)
            }
        )
    }
}
