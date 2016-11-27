package se.kth.awesome.model.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

import javax.persistence.*;
import java.util.Date;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Post implements Serializable,Comparable<Post>{


    private String postContent;
    private Date postedDate;


    public Post() {
		    pk = new PostFK();
    }

    public Post( String postContent, UserEntity sender, UserEntity receiver) {
	    this.pk = new PostFK(sender, receiver);
        this.postContent = postContent;
        this.postedDate = new Date(/*System.currentTimeMillis()*/);
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

    @Column(name = "post_content")
    public String getPostContent() {
        return postContent;
    }
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

	private PostFK pk;
	@Embedded
	public PostFK getPk() {
		return pk;
	}
	public void setPk(PostFK mailMessageId) {
		this.pk = mailMessageId;
	}

	@Transient
	public UserEntity getReceiver() {
		return getPk().getReceiver();
	}
	public void setReceiver(UserEntity receivingUser) {
		getPk().setReceiver(receivingUser);
	}

	@Transient
	public UserEntity getSender() {
		return getPk().getSender();
	}
	public void setSender(UserEntity sender) {
		getPk().setSender(sender);
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
//    @CreatedDate
    @Column(name = "posted_date",
            nullable = false,
            insertable = true,
            updatable = false)
    public Date getPostedDate() {
        return postedDate;
    }
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Post post = (Post) o;

		if (postContent != null ? !postContent.equals(post.postContent) : post.postContent != null) return false;
		if (postedDate != null ? !postedDate.equals(post.postedDate) : post.postedDate != null) return false;
		return id != null ? id.equals(post.id) : post.id == null;
	}

	@Override
	public int hashCode() {
		int result = postContent != null ? postContent.hashCode() : 0;
		result = 31 * result + (postedDate != null ? postedDate.hashCode() : 0);
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(Post o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}


	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}
}
