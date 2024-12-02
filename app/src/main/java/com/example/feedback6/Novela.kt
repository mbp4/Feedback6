package com.example.feedback6

data class Novela(val titulo: String, val autor: String, val año: Int, val sinopsis: String, val fav: Boolean, val pais: String){
    constructor(): this("", "", 0, "", false, "")
    //creamos un constructor para que no se creen novelas vacias
}