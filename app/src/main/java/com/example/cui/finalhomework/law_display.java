package com.example.cui.finalhomework;

/**
 * Created by cui on 2018/5/30.
 */

public class law_display {
    private String lawname;
    private String lawnum;
    private String lawcontent;

    public law_display(String lawname,String lawnum,String lawcontent)
    {
       this.lawname=lawname;
       this.lawnum=lawnum;
       this.lawcontent=lawcontent;
    }
    public String getLawname()
    {
        return lawname;
    }
    public String getLawnum()
    {
        return lawnum;
    }
    public String getLawcontent()
    {
        return lawcontent;
    }
}
