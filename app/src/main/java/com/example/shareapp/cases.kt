package com.example.shareapp

class cases {

    var caid:String?=null
    var subm:String?=null

    constructor(){}

    constructor(caid: String?, subm: String?) {
        this.caid = caid
        this.subm = subm
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("CaseId", caid!!)
        result.put("SDate", subm!!)

        return result
    }
}