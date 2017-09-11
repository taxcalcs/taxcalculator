<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml">
	<xsl:output method="text" indent="no" encoding="UTF-8" />
	
	<xsl:template name="accessBuilder">
		<xsl:variable name="class"><xsl:value-of select="./@name" /></xsl:variable>
	/**
	 * Builder to collect getters and setters to call it w/o reflection.
	 * Provides an {@link Accessor} to access this data.
	 *
	 * @see #build(<xsl:value-of select="$class" />)
	 * @since 2018.0.0
	 */	
	private static final class AccessorBuilder {
		
		/**
		 * Holder for lazy singleton instance.
		 */
		private static final class Holder {
        	private static final AccessorBuilder INSTANCE = new AccessorBuilder();
      	}
      	
      	/** private constructor for singleton */
      	private AccessorBuilder() {}
		
		/**
		 * Creates a Accessor for this class.
		 * 
		 * @param calculator
		 *            the calculator for accessor access.
		 * @return the accessor
		 */
		public static Accessor build(final <xsl:value-of select="$class" /> calculator) {
			final AccessorBuilder i = Holder.INSTANCE;
			return new AccessorImpl&lt;<xsl:value-of select="$class" />&gt;(i.GETTER_INT_MAP, i.GETTER_BD_MAP, i.GETTER_DOUBLE_MAP, i.SETTER_INT_MAP, i.SETTER_BD_MAP, i.SETTER_DOUBLE_MAP, calculator, i.INPUTS, i.OUTPUTS, i.OUTPUT_TYPES);
		}
		
		/**
		  * Getter methods which returns int.
		  */
		private final Map&lt;String,ToIntFunction&lt;<xsl:value-of select="$class" />&gt;&gt; GETTER_INT_MAP;
		{
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
		private final Map&lt;String,Function&lt;<xsl:value-of select="$class" />,BigDecimal&gt;&gt; GETTER_BD_MAP;
		{
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
		private final Map&lt;String,ToDoubleFunction&lt;<xsl:value-of select="$class" />&gt;&gt; GETTER_DOUBLE_MAP;
		{
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
		private final Map&lt;String,ObjIntConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; SETTER_INT_MAP;
		{
			final Map&lt;String,ObjIntConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; tmp = newMap();
			// setter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="int"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::set<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			SETTER_INT_MAP = tmp;
		}
		
		/**
		  * Setter methods which returns {@link BigDecimal}.
		  */
		private final Map&lt;String,BiConsumer&lt;<xsl:value-of select="$class" />,BigDecimal&gt;&gt; SETTER_BD_MAP;
		{
			final Map&lt;String,BiConsumer&lt;<xsl:value-of select="$class" />,BigDecimal&gt;&gt; tmp = newMap();
			// setter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="BigDecimal"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::set<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			SETTER_BD_MAP = tmp;
		}
		
		/**
		  * Setter methods which returns double.
		  */
		private final Map&lt;String,ObjDoubleConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; SETTER_DOUBLE_MAP;
		{
			final Map&lt;String,ObjDoubleConsumer&lt;<xsl:value-of select="$class" />&gt;&gt; tmp = newMap();
			// setter from input fields
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT[@type="double"]'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="$class" />::set<xsl:value-of select="translate(substring(./@name, 1, 1),$smallcase, $uppercase)" /><xsl:value-of select="substring(./@name, 2)" />);</xsl:for-each>
			//
			SETTER_DOUBLE_MAP = tmp;
		}
		
		/**
		  * {@link Map} with all output fields with type.
		  */
		private final Map&lt;String,Class&lt;?&gt;&gt; OUTPUTS;
		{
			final Map&lt;String,Class&lt;?&gt;&gt; tmp = newMap();
			<xsl:for-each select='./VARIABLES/OUTPUTS/OUTPUT'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="./@type" />.class);</xsl:for-each>
			//
			OUTPUTS = tmp;
		}
		
		/**
		  * {@link Map} with all input fields with type.
		  */
		private final Map&lt;String,Class&lt;?&gt;&gt; INPUTS;
		{
		 	final Map&lt;String,Class&lt;?&gt;&gt; tmp = newMap();
			<xsl:for-each select='./VARIABLES/INPUTS/INPUT'>tmp.put("<xsl:value-of select="./@name" />", <xsl:value-of select="./@type" />.class);</xsl:for-each>
			// 
			INPUTS = tmp;
		}
		
		/**
		  * {@link Map} with output fields types ("STANDARD" or "DBA").
		  */
		private final Map&lt;String,String&gt; OUTPUT_TYPES;
		{
			final Map&lt;String,String&gt; tmp = newMap();
			<xsl:for-each select='./VARIABLES/OUTPUTS/OUTPUT'>tmp.put("<xsl:value-of select="./@name" />", "<xsl:value-of select="../@type" />");</xsl:for-each>
			//
			OUTPUT_TYPES = tmp;
		}
	}
	</xsl:template>
</xsl:stylesheet>