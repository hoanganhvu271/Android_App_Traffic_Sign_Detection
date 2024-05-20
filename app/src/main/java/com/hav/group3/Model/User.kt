package com.hav.group3.Model

class User(private var userName : String, private var passWord : String) {

    fun getUserName(): String {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun getPassWord(): String {
        return passWord
    }

    fun setPassWord(passWord: String) {
        this.passWord = passWord
    }
}