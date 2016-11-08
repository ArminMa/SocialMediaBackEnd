package se.kth.awesome.model;

import java.io.Serializable;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import se.kth.awesome.pojos.UserPojo;

@Entity
@Table(name = "friend_request")
public class FriendRequest implements Serializable,Comparable<FriendRequest>{



    public FriendRequest() {
    }

    public FriendRequest(UserEntity sender, UserEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
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

    private UserEntity sender;
/*    @Column(name = "sender")*/

    @OneToOne(orphanRemoval = false, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "sender_id")
    public UserEntity getSender() {
        return sender;
    }
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }


    private UserEntity receiver;
    @OneToOne(orphanRemoval = false, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "receiver_id")
    public UserEntity getReceiver() {
        return receiver;
    }
    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendRequest that = (FriendRequest) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    @Override
    public int compareTo(FriendRequest o) {
        int thisObject= this.hashCode();
        long anotherObject = o.hashCode();
        return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
    }
}
