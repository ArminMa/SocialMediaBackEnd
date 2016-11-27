package se.kth.awesome.model.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class
PostPojo implements Serializable,Comparable<PostPojo>{
	private String postContent;

	public PostPojo() {
		pk = new PostPojoFK();
	}

	public PostPojo(String postContent, UserPojo sender, UserPojo receiver) {
		this.pk = new PostPojoFK(sender, receiver);
		this.postContent = postContent;
		this.postedDate = new Date(/*System.currentTimeMillis()*/);
	}

	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

	private PostPojoFK pk;
	public PostPojoFK getPk() {
		return pk;
	}
	public void setPk(PostPojoFK postPojoFK) {
		this.pk = postPojoFK;
	}

	public UserPojo getReceiver() {
		return getPk().getReceiver();
	}
	public void setReceiver(UserPojo receivingUser) {
		getPk().setReceiver(receivingUser);
	}

	public UserPojo getSender() {
		return getPk().getSender();
	}
	public void setSender(UserPojo sender) {
		getPk().setSender(sender);
	}

	private Date postedDate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	public Date getPostedDate() {
		return postedDate;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PostPojo postPojo = (PostPojo) o;

		if (postContent != null ? !postContent.equals(postPojo.postContent) : postPojo.postContent != null)
			return false;
		if (postedDate != null ? !postedDate.equals(postPojo.postedDate) : postPojo.postedDate != null) return false;
		return id != null ? id.equals(postPojo.id) : postPojo.id == null;
	}

	@Override
	public int hashCode() {
		int result = postContent != null ? postContent.hashCode() : 0;
		result = 31 * result + (postedDate != null ? postedDate.hashCode() : 0);
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(PostPojo o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}


	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}
}
