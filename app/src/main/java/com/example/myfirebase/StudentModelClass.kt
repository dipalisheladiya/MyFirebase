package com.example.myfirebase

class StudentModelClass{
    var firstname: String = ""
    var midname: String = ""
    var lastname: String = ""
    var email: String = ""
    var number: String =""
    var fees: String = ""
    var key: String = ""

    constructor(firstname: String,midname: String, lastname: String,email: String,number: String,fees: String,key: String)
    {
        this.firstname=firstname
        this.midname=midname
        this.lastname=lastname
        this.email=email
        this.number=number
        this.fees=fees
        this.key=key
    }
    constructor()
}

