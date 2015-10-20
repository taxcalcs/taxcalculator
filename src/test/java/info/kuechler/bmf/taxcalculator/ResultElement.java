package info.kuechler.bmf.taxcalculator;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Sub bean class for JAXB marshaling the BMF result.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultElement {

    @XmlAttribute
    private BigDecimal value;
    
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private String status;
    
    public BigDecimal getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
