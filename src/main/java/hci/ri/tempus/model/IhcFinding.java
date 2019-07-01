package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

public class IhcFinding {

    private long idTpsIhcFinding;
    private String strHello;

    public long getIdTpsIhcFinding() { return idTpsIhcFinding; }
    public void setIdTpsIhcFinding(long idTpsIhcFinding) {
        this.idTpsIhcFinding = idTpsIhcFinding;
    }


    public String getStrHello(){ return strHello; }
    public void setStrHello(String strHello){
        this.strHello = strHello;
    }



}
