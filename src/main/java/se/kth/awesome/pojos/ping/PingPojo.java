package se.kth.awesome.pojos.ping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;
import se.kth.awesome.util.GsonX;


import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PingPojo implements Serializable,Comparable<PingPojo>{
    @Expose
    private String helloPing;
    @JsonIgnore
    private String ignore;
    private String notIgnored;
    private String nulString;

    public PingPojo() {
    }

    public PingPojo(String helloPing, String ignoreThis, String doNotIgnor) {
        this.helloPing = helloPing;
        this.ignore = ignoreThis;
        this.notIgnored = doNotIgnor;
    }



    public String getHelloPing() {
        return helloPing;
    }

    public void setHelloPing(String helloPing) {
        this.helloPing = helloPing;
    }

    public String getNotIgnored() {
        return notIgnored;
    }

    public void setNotIgnored(String notIgnored) {
        this.notIgnored = notIgnored;
    }


    public String getIgnore() {
        return ignore;
    }

    public void setIgnore(String ignore) {
        this.ignore = ignore;
    }

    public String getNulString() {
        return nulString;
    }

    public void setNulString(String nulString) {
        this.nulString = nulString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PingPojo ping = (PingPojo) o;

        if (helloPing != null ? !helloPing.equals(ping.helloPing) : ping.helloPing != null) return false;
        if (ignore != null ? !ignore.equals(ping.ignore) : ping.ignore != null) return false;
        return notIgnored != null ? notIgnored.equals(ping.notIgnored) : ping.notIgnored == null;

    }

    @Override
    public int hashCode() {
        int result = helloPing != null ? helloPing.hashCode() : 0;
        result = 31 * result + (ignore != null ? ignore.hashCode() : 0);
        result = 31 * result + (notIgnored != null ? notIgnored.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(PingPojo o) {
        int thisTime = this.hashCode();
        long anotherEntity = o.hashCode();
        return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
    }

    @Override
    public String toString() {
        return GsonX.gson.toJson(this);
    }
}
