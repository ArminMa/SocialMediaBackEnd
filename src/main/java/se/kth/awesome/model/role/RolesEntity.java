package se.kth.awesome.model.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.util.gsonX.GsonX;


@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "roles_entity")
public class RolesEntity implements Serializable, Comparable<RolesEntity> {



	private Role role;


    public RolesEntity() {

    }

    public RolesEntity(Role theRoles) {
        this.role = theRoles;
        this.id = (long) (theRoles.ordinal()+1);

    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    private Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", insertable = false, updatable = false, unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public int compareTo(RolesEntity o) {
        int thisObject= this.hashCode();
        long anotherObject = o.hashCode();
        return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
    }

    @Override
    public String toString() {
        return GsonX.gson.toJson(this);
    }
}
