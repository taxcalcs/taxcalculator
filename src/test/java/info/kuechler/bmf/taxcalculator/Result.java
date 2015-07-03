package info.kuechler.bmf.taxcalculator;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement(name = "lohnsteuer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {

    @XmlElementWrapper(name = "ausgaben")
    @XmlElement(name = "ausgabe")
    private List<ResultElement> output;

    @XmlElementWrapper(name = "eingaben")
    @XmlElement(name = "eingabe")
    private List<ResultElement> input;
    
    public List<ResultElement> getOutput() {
        return output;
    }

    public List<ResultElement> getInput() {
        return input;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
