//package se.kth.awsome.model;
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
//    public FriendRequest(ApplicationUser sender, ApplicationUser receiver) {
//        this.sender = sender;
//        this.receiver = receiver;
//    }
//
//    private ApplicationUser sender;
//    @Column(name = "sender")
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = false/*, mappedBy = "ApplicationUser.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public ApplicationUser getSender() {
//        return sender;
//    }
//    public void setSender(ApplicationUser sender) {
//        this.sender = sender;
//    }
//
//
//    private ApplicationUser receiver;
//    @Column(name = "receiver")
//    @OneToOne( /*, mappedBy = "ApplicationUser.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public ApplicationUser getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(ApplicationUser receiver) {
//        this.receiver = receiver;
//    }
//
//}
