package com.sktl.mrtask

/**
 * Created by USER-PC on 18.02.2018.
 */

class Process {

    var id: String? = null
    var name: String? = null

    constructor(name: String) {

        this.name = name

    }


    constructor(id: String, name: String) {
        this.id = id

        this.name = name

    }


}
