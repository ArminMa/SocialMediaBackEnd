package se.kth.awesome.model;


import java.util.ArrayList;
import java.util.Collection;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.mailMessage.MailMessage;
import se.kth.awesome.model.mailMessage.MailMessagePojo;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.model.post.Post;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.model.role.rolePojo.UserRolePojo;
import se.kth.awesome.util.gsonX.GsonX;


/**
 * Created by Am on 11/4/2016.
 */
public class ModelConverter {

        public ModelConverter() {
        }

        private static boolean ObjectNotNull(Object o){
            return (o == null ? false : true);
        }


        public static UserPojo convert(UserEntity userEntity){
            if( userEntity == null ) return null;
            return GsonX.gson.fromJson(userEntity.toString(), UserPojo.class );
        }

        public static UserEntity convert(UserPojo userPojo){
            if( userPojo == null ) return null;
            return GsonX.gson.fromJson(userPojo.toString(), UserEntity.class );
        }

    public static MailMessagePojo convert(MailMessage mailMessage){
        if( mailMessage == null ) return null;
        return GsonX.gson.fromJson(mailMessage.toString(), MailMessagePojo.class );
    }

    public static MailMessage convert(MailMessagePojo mailMessagePojo){
        if( mailMessagePojo == null ) return null;
        return GsonX.gson.fromJson(mailMessagePojo.toString(), MailMessage.class );
    }

	public static UserRoleEntity convert(UserRolePojo userAuthorityPojo){
		if( userAuthorityPojo == null ) return null;
		return GsonX.gson.fromJson(userAuthorityPojo.toString(), UserRoleEntity.class );
	}

	public static UserRolePojo convert(UserRoleEntity userRoleEntity){
		if( userRoleEntity == null ) return null;
		return GsonX.gson.fromJson(userRoleEntity.toString(), UserRolePojo.class );
	}

//        public static FriendRequestPojo convert(FriendRequest friendRequest){
//            if( friendRequest == null ) return null;
//            return GsonX.gsonX.fromJson(friendRequest.toString(), FriendRequestPojo.class );
//        }
//
//        public static MailMessagePojo convert (MailMessage mailMessage){
//            if( mailMessage == null ) return null;
//            return GsonX.gsonX.fromJson(mailMessage.toString(), MailMessagePojo.class );
//        }
//
        public static PostPojo convert (Post post){
            if( post == null ) return null;
            return GsonX.gson.fromJson(post.toString(), PostPojo.class );
        }

        public static Post convert (PostPojo postPojo){
            if( postPojo == null ) return null;
            return GsonX.gson.fromJson(postPojo.toString(), Post.class );
        }

        @SuppressWarnings("unchecked")
        public static Iterable<?> convert (Iterable<?> genericList){

            if( genericList == null ) return null;

            if ( !genericList.iterator().hasNext() ) return null;

            // this can be modified
//		if (somthing instanceof List<?>) {
//
//		}
//       else if (x instanceof Collection<?>) {
//             }

            if(genericList.iterator().next() instanceof UserEntity){
                Collection<UserPojo> userPojos = new ArrayList<>();
                genericList.forEach( S -> userPojos.add( convert( (UserEntity) S) ) );
                return userPojos;
            }

            if(genericList.iterator().next() instanceof UserPojo){
                Collection<UserEntity> userPojos = new ArrayList<>();
                genericList.forEach( S -> userPojos.add( convert( (UserPojo) S) ) );
                return userPojos;
            }

            if(genericList.iterator().next() instanceof MailMessage){
                Collection<MailMessagePojo> mailMessagePojos = new ArrayList<>();
                genericList.forEach( S -> mailMessagePojos.add( convert( (MailMessage) S) ) );
                return mailMessagePojos;
            }

            if(genericList.iterator().next() instanceof MailMessagePojo){
                Collection<MailMessage> mailMessages = new ArrayList<>();
                genericList.forEach( S -> mailMessages.add( convert( (MailMessagePojo) S) ) );
                return mailMessages;
            }

            if(genericList.iterator().next() instanceof Post){
                Collection<PostPojo> postPojos = new ArrayList<>();
                genericList.forEach( S -> postPojos.add( convert( (Post) S) ) );
                return postPojos;
            }

            if(genericList.iterator().next() instanceof PostPojo){
                Collection<Post> posts = new ArrayList<>();
                genericList.forEach( S -> posts.add( convert( (PostPojo) S) ) );
                return posts;
            }

	        if(genericList.iterator().next() instanceof UserRoleEntity){
		        Collection<UserRolePojo> userAuthorityPojos = new ArrayList<>();
		        genericList.forEach( S -> userAuthorityPojos.add( convert( (UserRoleEntity) S) ) );
		        return userAuthorityPojos;
	        }

	        if(genericList.iterator().next() instanceof UserRolePojo){
		        Collection<UserRoleEntity> userAuthorities = new ArrayList<>();
		        genericList.forEach( S -> userAuthorities.add( convert( (UserRolePojo) S) ) );
		        return userAuthorities;
	        }

            return null;

        }


}
