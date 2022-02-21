package com.example.enbuenasmanos.logica

import com.example.enbuenasmanos.casosUso.UsuarioUseCase

class UsuarioBL {

    fun LoginUser(name: String, pass: String): Boolean {
        var us = UsuarioUseCase().getUserNameAndPass(name, pass)
        return (us.id == "-1L")
    }

}