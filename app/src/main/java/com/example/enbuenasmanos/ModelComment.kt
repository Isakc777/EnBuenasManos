package com.example.enbuenasmanos

class ModelComment {

    //las variables, deben tener el mismo formato y tipo que agregamos en firebase
    var id = ""
    var questionId = ""
    var timestamp = ""
    var comment = ""
    var uid = ""

    // contructor vacio, que es requerido por firebase
    constructor()

    //parametros del consturctor
    constructor(id: String, questionId: String, timestamp: String, comment: String, uid: String) {
        this.id = id
        this.questionId = questionId
        this.timestamp = timestamp
        this.comment = comment
        this.uid = uid
    }
}