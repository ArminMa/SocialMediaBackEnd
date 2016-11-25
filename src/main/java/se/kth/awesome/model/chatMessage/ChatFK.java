package se.kth.awesome.model.chatMessage;


import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import se.kth.awesome.model.User.UserEntity;


@Embeddable
public class ChatFK implements java.io.Serializable, Comparable<ChatFK> {

	private UserEntity receivingUser;
	private UserEntity author;

	public ChatFK() {}

	/**
	 * @param receivingUser     2
	 * @param author            3
	 */
	public ChatFK(UserEntity receivingUser, UserEntity author) {
		this.receivingUser = receivingUser;
		this.author = author;
	}

	@ManyToOne(fetch = FetchType.EAGER,  targetEntity = UserEntity.class)
	@JoinColumn(name = "chat_message_receiver_id")
	public UserEntity getReceivingUser() {
		return receivingUser;
	}
	public void setReceivingUser(UserEntity receivingUser) {
		this.receivingUser = receivingUser;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
	@JoinColumn(name = "chat_message_sender_id")
	public UserEntity getAuthor() {
		return author;
	}
	public void setAuthor(UserEntity author) {
		this.author = author;
	}

	@Override
	public int compareTo(ChatFK o) {
		int thisTime = this.hashCode();
		long anotherEntity = o.hashCode();
		return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
	}
}
