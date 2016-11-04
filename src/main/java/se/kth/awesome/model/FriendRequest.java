//package se.kth.awesome.model;
//
//import org.hibernate.annotations.LazyCollection;
//import org.hibernate.annotations.LazyCollectionOption;
//import org.hibernate.annotations.SortNatural;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "friend_request")
//public class FriendRequest {
//
//
//
//    public FriendRequest() {
//    }
//
//    public FriendRequest(UserEntity sender, UserEntity receiver) {
//        this.sender = sender;
//        this.receiver = receiver;
//    }
//
//    private Long id;
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", insertable = false, updatable = false, unique = true, nullable = false)
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    private UserEntity sender;
//    @Column(name = "sender")
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = false/*, mappedBy = "UserEntity.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public UserEntity getSender() {
//        return sender;
//    }
//    public void setSender(UserEntity sender) {
//        this.sender = sender;
//    }
//
//
//    private UserEntity receiver;
//    @Column(name = "receiver")
//    @OneToOne( /*, mappedBy = "UserEntity.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public UserEntity getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(UserEntity receiver) {
//        this.receiver = receiver;
//    }
//
//}
