<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:date="http://exslt.org/dates-and-times" 
    extension-element-prefixes="date">
	<xsl:output method="text" indent="no" encoding="UTF-8" />
	
	<!-- Arrays for conversion upper and lower case -->
	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />
	
	<!-- root node -->
	<xsl:template match="/PAP">/*
*/
package info.kuechler.bmf.taxcalculator;

import static info.kuechler.bmf.taxcalculator.AccessorImpl.newMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

import javax.annotation.Generated;

/**
 * Steuerberechnungsklasse.
 * 
 * Generiert aus Pseudocode von: &lt;a href="https://www.bmf-steuerrechner.de"&gt;bmf-steuerrechner&lt;/a&gt;
 */
@SuppressWarnings("unused")
@Generated(value="info.kuechler.bmf.taxcalculator", date="<xsl:value-of select="date:date-time()"/>", comments="Generated from pseudo code https://www.bmf-steuerrechner.de")
public class <xsl:value-of select="./@name" /> implements Calculator {
		<xsl:apply-templates select="./VARIABLES" />
		<xsl:apply-templates select="./CONSTANTS" />
		<xsl:apply-templates select="./METHODS" />
		<xsl:call-template name="inputGetterAndSetter"/>
		<xsl:call-template name="outputGetter"/>
		<xsl:call-template name="genericAccessClass"/>
}
	</xsl:template>


	<!-- Templates by match -->
	<xsl:template match="INPUT"><xsl:call-template name="variables" /></xsl:template>
	<xsl:template match="OUTPUT"><xsl:call-template name="variables" /></xsl:template>
	<xsl:template match="INTERNAL"><xsl:call-template name="variables" /></xsl:template>
	<xsl:template match="CONSTANT"><xsl:call-template name="constant" /></xsl:template>
	<xsl:template match="MAIN">public void calculate() {<xsl:apply-templates select="child::node()" />}</xsl:template>
	<xsl:template match="METHOD">private void <xsl:value-of select="./@name" />() {<xsl:apply-templates select="child::node()" />}</xsl:template><xsl:template match="EXECUTE"><xsl:value-of select="./@method" />();</xsl:template>
	<xsl:template match="EVAL"><xsl:value-of select="./@exec" />;</xsl:template>
	<xsl:template match="IF">if (<xsl:value-of select="./@expr" />) {<xsl:apply-templates select="child::node()" />}</xsl:template>
	<xsl:template match="THEN"><xsl:apply-templates select="child::node()" /></xsl:template>
	<!-- Standard case: <IF><THEN>...</THEN><ELSE>...</ELSE></IF> -->
	<xsl:template match="ELSE[parent::IF]">}else {<xsl:apply-templates select="child::node()" /></xsl:template>
	<!-- Bug case: <IF><THEN>...</THEN></IF><ELSE>...</ELSE> -->
	<xsl:template match="ELSE[not(parent::IF)]">else {<xsl:apply-templates select="child::node()" />}</xsl:template>
	<xsl:template match="comment()">/** <xsl:value-of select="."/> */</xsl:template>
	
	
	<!-- Templates by name for 'xsl:call-template' -->
	
	<!-- common template for variables -->
	<xsl:template name="variables">private <xsl:value-of select="./@type" /><xsl:value-of select="' '" /><xsl:value-of select="./@name" /><xsl:if test="@default and string-length(@default)>0"> = (<xsl:value-of select="./@type" />) <xsl:value-of select="./@default" /></xsl:if>;</xsl:template>
	
	<!-- common template for constants -->
	<xsl:template name="constant">private final static <xsl:value-of select="./@type" /><xsl:value-of select="' '" /><xsl:value-of select="./@name" /> = <xsl:value-of select="./@value" />;</xsl:template>
	
	<!-- Getter and Setter for Input variables -->
	<xsl:template name="inputGetterAndSetter">
		<xsl:for-each select="./VARIABLES/INPUTS/INPUT"><xsl:call-template name="getter" /><xsl:call-template name="setter" /></xsl:for-each>
	</xsl:template>
	
	<!-- Getter for Output variables -->
	<xsl:template name="outputGetter">
		<xsl:for-each select="./VARIABLES/OUTPUTS/OUTPUT"><xsl:call-template name="getter" /></xsl:for-each>
	</xsl:template>
	
	<!-- a getter method-->
	<xsl:template name="getter">
	/**
	 * Getter for <xsl:value-of select="./@name" />.
	 * <xsl:text>&lt;p&gt;</xsl:text>
	 * <xsl:value-of select="preceding-sibling::comment()[1]"/>
	 * <xsl:text>&lt;p&gt;</xsl:text>
	 * @return the value
	 */
	public <xsl:value-of select="./@type" /> get<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />() {
		return <xsl:value-of select="./@name" />;
    	}
    </xsl:template>
    
    <!-- a setter method -->
    <xsl:template name="setter">
    /**
     * Setter for <xsl:value-of select="./@name" />.
     * <xsl:text>&lt;p&gt;</xsl:text>
     * <xsl:value-of select="preceding-sibling::comment()[1]"/>
     * <xsl:text>&lt;p&gt;</xsl:text>
     * @param <xsl:value-of select="./@name" /> input value 
     */
	public void set<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />(final <xsl:value-of select="./@type" /><xsl:value-of select="' '" /><xsl:value-of select="./@name" />) {
		this.<xsl:value-of select="./@name" />=<xsl:value-of select="./@name" />;
    	}
    </xsl:template>

	<xsl:template name="genericAccessClass">
		<xsl:variable name="class"><xsl:value-of select="./@name" /></xsl:variable>
		
		/*
		 * Collect getters and setters to call it w/o reflection.
		 */
		
		/**
		  * Getter methods which returns int.
		  */
		private static final Map&lt;String,ToIntFunction&lt;<xsl:value-of select="$class" />&gt;&gt; GETTER_INT_MAP;
		static {
			final Map&lt;String,ToIntFunction&lt;<xsl:value-of select="$class" />&gt;&gt; tmp = newMap();
			// getter from output fields
			<xsl:for-each select='./VARIABLES/OUTPUTS/OUTPUT[@type="int"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::get<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			// getter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="int"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::get<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			GETTER_INT_MAP = tmp;
		}
		
		/**
		  * Getter methods which returns {@link BigDecimal}.
		  */
		private static final Map&lt;String,Function&lt;<xsl:value-of select="$class" />,BigDecimal&gt;&gt; GETTER_BD_MAP;
		static {
		 	final Map&lt;String,Function&lt;<xsl:value-of select="$class" />,BigDecimal&gt;&gt; tmp = newMap();
			// getter from output fields
			<xsl:for-each select='./VARIABLES/OUTPUTS/OUTPUT[@type="BigDecimal"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::get<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			// getter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="BigDecimal"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::get<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			GETTER_BD_MAP = tmp;
		}
		
		/**
		  * Getter methods which returns double.
		  */
		private static final Map&lt;String,ToDoubleFunction&lt;<xsl:value-of select="$class" />&gt;&gt; GETTER_DOUBLE_MAP;
		static {
			final Map&lt;String,ToDoubleFunction&lt;<xsl:value-of select="$class" />&gt;&gt; tmp = newMap();
			// getter from output fields
			<xsl:for-each select='./VARIABLES/OUTPUTS/OUTPUT[@type="double"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::get<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			// getter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="double"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::get<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			GETTER_DOUBLE_MAP = tmp;
		}
		
		/**
		  * Setter methods which returns int.
		  */
		private static final Map&lt;String,ObjIntConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; SETTER_INT_MAP;
		static {
			final Map&lt;String,ObjIntConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; tmp = newMap();
			// setter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="int"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::set<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			SETTER_INT_MAP = tmp;
		}
		
		/**
		  * Setter methods which returns {@link BigDecimal}.
		  */
		private static final Map&lt;String,BiConsumer&lt;<xsl:value-of select="$class" />,BigDecimal&gt;&gt; SETTER_BD_MAP;
		static {
			final Map&lt;String,BiConsumer&lt;<xsl:value-of select="$class" />,BigDecimal&gt;&gt; tmp = newMap();
			// setter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="BigDecimal"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::set<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			SETTER_BD_MAP = tmp;
		}
		
		/**
		  * Setter methods which returns double.
		  */
		private static final Map&lt;String,ObjDoubleConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; SETTER_DOUBLE_MAP;
		static {
			final Map&lt;String,ObjDoubleConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; tmp = newMap();
			// setter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="double"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::set<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			SETTER_DOUBLE_MAP = tmp;
		}
		
		/**
		  * {@link Map} with all output fields with type.
		  */
		private static final Map&lt;String,Class&lt;?&gt;&gt; OUTPUTS;
		static {
			final Map&lt;String,Class&lt;?&gt;&gt; tmp = newMap();
			<xsl:for-each select='./VARIABLES/OUTPUTS/OUTPUT'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="./@type" />.class);</xsl:for-each>
			//
			OUTPUTS = tmp;
		}
		
		/**
		  * {@link Map} with all input fields with type.
		  */
		private static final Map&lt;String,Class&lt;?&gt;&gt; INPUTS;
		static {
		 	final Map&lt;String,Class&lt;?&gt;&gt; tmp = newMap();
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="./@type" />.class);</xsl:for-each>
			// 
			INPUTS = tmp;
		}
		
		/**
		  * {@link Map} with output fields types ("STANDARD" or "DBA").
		  */
		private static final Map&lt;String,String&gt; OUTPUT_TYPES;
		static {
			final Map&lt;String,String&gt; tmp = newMap();
			<xsl:for-each select='./VARIABLES/OUTPUTS/OUTPUT'>tmp.put("<xsl:value-of select="./@name" />", "<xsl:value-of select="../@type" />");</xsl:for-each>
			//
			OUTPUT_TYPES = tmp;
		}

		/**
		  * {@link Accessor} for access fields by {@link String} key.
		  */
		private final Accessor accessor = new AccessorImpl&lt;<xsl:value-of select="$class" />&gt;(GETTER_INT_MAP, GETTER_BD_MAP, GETTER_DOUBLE_MAP, SETTER_INT_MAP, SETTER_BD_MAP, SETTER_DOUBLE_MAP, this, INPUTS, OUTPUTS, OUTPUT_TYPES);
		
		/**
     	  * {@inheritDoc}
     	  */
		@Override
		public Accessor getAccessor() {
			return accessor;
		}
	</xsl:template>
	
</xsl:stylesheet>
