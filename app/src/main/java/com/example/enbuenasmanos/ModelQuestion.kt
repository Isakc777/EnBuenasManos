package com.example.enbuenasmanos

class ModelQuestion {
    //las variables, deben coincidir como en firebase
    var id:String = ""
    var question:String = ""
    var timestamp:Long = 0
    var uid:String = ""

    // constructor vacío, requerido por firebase
    constructor()

    //contructor parametrizado
    constructor(id: String, question: String, timestamp: Long, uid: String) {
        this.id = id
        this.question = question
        this.timestamp = timestamp
        this.uid = uid
    }




}