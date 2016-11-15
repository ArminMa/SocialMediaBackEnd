package se.kth.awesome.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class PostPojo {
}
