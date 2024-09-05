package br.edu.satc.todolistcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import br.edu.satc.todolistcompose.database.AppDatabase
import br.edu.satc.todolistcompose.database.TaskDao
import br.edu.satc.todolistcompose.ui.screens.HomeScreen
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init database.
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "br.edu.satc.contatosapp"
        )
            .allowMainThreadQueries() // allows in MainActivity
            .build()

        // Init DAO
        val taskDao = db.taskDao()

        setContent {
            ToDoListComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // get tasks from DB to "state by remember"
                    var tasksState by remember {
                        mutableStateOf(taskDao.getAll())
                    }

                    // Open "HomeScreen", pass tasks and listening newTasks
//                    HomeScreen(tasks = tasksState){
//                        taskDao.insertAll(it)
//                        tasksState = taskDao.getAll()
//                    }
                    HomeScreen(tasks = tasksState, onNewTaskCreated = {
                        taskDao.insertAll(it)
                        tasksState = taskDao.getAll()
                    }) { task, complete ->
                        run {
                            task.complete = complete
                            taskDao.updateAll(task)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoListComposeTheme {
        Greeting("Android")
    }
}