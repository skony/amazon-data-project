package pl.put.fc;

import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class SomeObject {
    
    @JsonProperty
    int x;
    
    public SomeObject() {
    }
    
    public SomeObject(int x) {
        this.x = x;
    }
}
