package se.kth.awesome.modelConverter;


import se.kth.awesome.model.*;
import se.kth.awesome.pojos.*;
import se.kth.awesome.util.GsonX;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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

//        public static FriendRequestPojo convert(FriendRequest friendRequest){
//            if( friendRequest == null ) return null;
//            return GsonX.gson.fromJson(friendRequest.toString(), FriendRequestPojo.class );
//        }
//
//        public static MailMessagePojo convert (MailMessage mailMessage){
//            if( mailMessage == null ) return null;
//            return GsonX.gson.fromJson(mailMessage.toString(), MailMessagePojo.class );
//        }
//
//        public static PostPojo convert (Post post){
//            if( post == null ) return null;
//            return GsonX.gson.fromJson(post.toString(), PostPojo.class );
//        }

/*        @SuppressWarnings("unchecked")
        public static Iterable<?> convert (Iterable<?> genericList){

            if( genericList == null ) return null;

            if ( !genericList.iterator().hasNext() ) return null;

            // this can be modified
//		if (somthing instanceof List<?>) {
//
//		}
//       else if (x instanceof Collection<?>) {
//             }

            if(genericList.iterator().next() instanceof AuthorityPojo){
                List<Authority> authorities = new ArrayList<>();
                genericList.forEach( S -> authorities.add( convert( (AuthorityPojo) S) ) );
                return authorities;
            }

            return null;

        }*/


}
