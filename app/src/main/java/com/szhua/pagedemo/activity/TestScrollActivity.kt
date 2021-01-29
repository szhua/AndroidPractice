package com.szhua.pagedemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.szhua.pagedemo.R
import com.szhua.pagedemo.db.data.User
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.ArrayList


open class Szhua(val name:String ,val age :Int)

class TestScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_scroll)

       // fromJson<Szhua>("{}")

        /**
         * [{"age":1,"name":"szhua"},{"age":2,"name":"leilei"}]
         */


      val s =  fromJson<List<Szhua>>("[{\"age\":1,\"name\":\"szhua\"},{\"age\":2,\"name\":\"leilei\"}]")

      LogUtils.d(s)


     val szhua = Szhua("ss",1).javaClass.genericSuperclass

     val leilei = object: Szhua(name = "ss", age = 1){

     }.javaClass.genericSuperclass

        LogUtils.d(Szhua("ss",1).javaClass)
        LogUtils.d(object: Szhua(name = "ss", age = 1){

        }.javaClass)

      LogUtils.d(szhua)
        LogUtils.d(leilei)


    }




    inline fun <reified T> fromJson(json:String) :T? {
        val type =  object : TypeLiteral<T>(){}.type
        return try {
            Gson().fromJson(json,type)
        }catch (e:Exception){
            null
        }
    }




    open class TypeLiteral<T> {
        val type: Type
             get() {
                 LogUtils.d(javaClass)
                 return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
             }
    }


}