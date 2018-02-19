package com.sktl.mrtask;

/**
 * Created by USER-PC on 18.02.2018.
 */

public class Process {

    private String id;
    private String name;



    public Process(String id,String name) {
        this.id = id;

        this.name = name;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
