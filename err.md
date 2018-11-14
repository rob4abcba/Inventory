
esanderson(ABND) [Nov 9th at 12:49 PM]
I am unable to create the database in my project 9 app
This is the error I get when I try to install on my phone. App immediately crashes> Here is my repository: https://github.com/emscape/Inventory
2018-11-09 14:40:31.565 31100-31131/? E/SQLiteLog: (1) no such table: items
2018-11-09 14:40:31.587 31100-31131/? E/AndroidRuntime: FATAL EXCEPTION: AsyncTask #1
   Process: com.example.pwesc62.inventory, PID: 31100
   java.lang.RuntimeException: An error occurred while executing doInBackground()
       at android.os.AsyncTask$3.done(AsyncTask.java:354)
       at java.util.concurrent.FutureTask.finishCompletion(FutureTask.java:383)
       at java.util.concurrent.FutureTask.setException(FutureTask.java:252)
       at java.util.concurrent.FutureTask.run(FutureTask.java:271)
       at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
       at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
       at java.lang.Thread.run(Thread.java:764)
    Caused by: android.database.sqlite.SQLiteException: no such table: items (code 1 SQLITE_ERROR): , while compiling: SELECT _id, product_name, product_creator FROM items
       at android.database.sqlite.SQLiteConnection.nativePrepareStatement(Native Method)
       at android.database.sqlite.SQLiteConnection.acquirePreparedStatement(SQLiteConnection.java:903)
       at android.database.sqlite.SQLiteConnection.prepare(SQLiteConnection.java:514)
       at android.database.sqlite.SQLiteSession.prepare(SQLiteSession.java:588)
GitHub
emscape/Inventory
Contribute to emscape/Inventory development by creating an account on GitHub.
 


6 replies
thebkline[ABND] [5 days ago]
I downloaded your project and was able to successfully install it on an emulator without it crashing.  It did crash after I clicked save when adding an item though.  You mention that the app immediately crashes.  Are you saying that it never shows anything and just crashes right after Android Studio installs it?

Dp_chua [5 days ago]
Did you run it before successfully then added a table or had a typo in the database creation? You can try deleting the database data from your emulator and then re run it if that’s the case


steven.b [5 days ago]
You can set up an upgrade policy to delete the db and then simply increase the version number to clear your old database. This works well for rapid iterations. (edited)


esanderson(ABND) [5 days ago]
Steven, how do I do that?


steven.b [5 days ago]
Your overridden SqliteDbHelper has an onUpgrade method. Just uncomment it and add the constant String with the "DROP TABLES" sql command.


steven.b [5 days ago]
Your constructor passes your database version into the super constructor. When you change the version it will run the onUpgrade method on its own (edited)
