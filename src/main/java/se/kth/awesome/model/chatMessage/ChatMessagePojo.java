package se.kth.awesome.model.chatMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessagePojo {
    private Long id;
}
