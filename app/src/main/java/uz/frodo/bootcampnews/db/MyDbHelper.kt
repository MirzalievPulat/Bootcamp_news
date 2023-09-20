package uz.frodo.bootcampnews.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.VERSION
import uz.frodo.bootcampnews.News

class MyDbHelper(context: Context):SQLiteOpenHelper(context, DB_NAME,null, VERSION) {

    companion object{
        const val DB_NAME = "News_db"
        const val VERSION = 1

        const val TABLE_NAME = "news"
        const val ID = "id"
        const val TYPE = "type"
        const val TITLE = "title"
        const val BODY = "body"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = "create table $TABLE_NAME ($ID integer not null primary key autoincrement, $TYPE text not null," +
                "$TITLE text not null, $BODY text not null)"
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(p0)
    }

    fun addNews(news:News){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(TYPE,news.type)
            put(TITLE,news.title)
            put(BODY,news.body)
        }
        db.insert(TABLE_NAME,null,cv)
        db.close()
    }

    fun deleteNews(id:Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"$ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllByType(type:String):ArrayList<News>{
        val list = ArrayList<News>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(ID, TYPE, TITLE, BODY), "$TYPE = ?", arrayOf(type),null,null,
            TITLE)
        while (cursor.moveToNext()){
            list.add(News(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)))
        }
        cursor.close()
        db.close()
        return list
    }

    fun getById(id:Int):News{
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(ID, TYPE, TITLE, BODY),"$ID = ?", arrayOf(id.toString()),null,null,null)
        cursor.moveToFirst()
        val news = News(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(2))
        cursor.close()
        db.close()
        return news
    }

    fun editNews(news: News){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(TYPE,news.type)
            put(TITLE,news.title)
            put(BODY,news.body)
        }
        db.update(TABLE_NAME,cv,"$ID = ?", arrayOf("${news.id}"))
        db.close()
        println("ishlayapti: ")
    }
}