package se.kth.awesome.model.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.util.gson.GsonX;


@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL )
public class UserRolePojo  implements Serializable,Comparable<UserRolePojo>{

    public UserRolePojo() {
    }

    public UserRolePojo(Role role) {
        this.id = (long) (role.ordinal()+1);
        this.role = role;
    }

    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Role role;
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRolePojo thatUserRolePojo = (UserRolePojo) o;

        if (id != null ? !id.equals(thatUserRolePojo.id) : thatUserRolePojo.id != null) return false;
        return role == thatUserRolePojo.role;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(UserRolePojo o) {
        int thisObject= this.hashCode();
        long anotherObject = o.hashCode();
        return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
    }

    @Override
    public String toString() {
        return  GsonX.gson.toJson(this);
    }


}
