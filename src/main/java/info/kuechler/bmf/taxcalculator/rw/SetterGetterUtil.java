package info.kuechler.bmf.taxcalculator.rw;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Helper class for getter, setter and methods.
 */
public class SetterGetterUtil {
	
	/**
	 * Method prefix for setter methods.
	 */
    public static final String METHOD_PREFIX_SET = "set";
    
    /**
     * Method prefix for getter methods.
     */
    public static final String METHOD_PREFIX_GET = "get";

	/**
     * Returns if the method from the search type.
     * 
     * @param methodPrefix
     *            the searched method, is the method name prefix
     * @param methodName
     *            current method name
     * @return is the method from the searched type.
     */
    public static boolean isMethod(String methodPrefix, String methodName) {
        return methodName.startsWith(methodPrefix);
    }

    /**
     * Extract the property name from a method name. The property name starts with a upper case letter.
     * 
     * @param methodName
     *            the method name
     * @return the property name.
     */
    public static String getPropertyName(String methodName) {
        return methodName.substring(3);
    }

    /**
     * Returns the parameter count from a method.
     * 
     * @param method
     *            the method
     * @return the count
     */
    public static int getParameterCount(Method method) {
        return method.getParameterTypes().length;
    }

    /**
     * Get the {@link Class} of the first parameter of a method. If the method don't exists an exception is thrown.
     * 
     * @param method
     *            the method
     * @return the class
     */
    public static Class<?> getFirstParameterType(final Method method) {
        return method.getParameterTypes()[0];
    }

    /**
	 * Convert a property to a property name. The first letter will be convert
	 * in a upper case.
	 * 
	 * @param key
	 *            the input property name
	 * @return the converted property.
	 * @deprecated typing error, use {@link #toCaseInsensitiveProperty(String)},
	 *             will be removed in version 2019
	 */
    @Deprecated
    public static String toCaseInsensiviteProperty(final String key) {
        return toCaseInsensitiveProperty(key);
    }
    
    /**
     * Convert a property to a property name. The first letter will be convert in a upper case.
     * 
     * @param key
     *            the input property name
     * @return the converted property.
     */
    public static String toCaseInsensitiveProperty(final String key) {
        return key.toUpperCase(Locale.ENGLISH);
    }

    /**
     * Creates the setter method name for an property.
     * 
     * @param property
     *            the property name.
     * @return the setter name
     */
    public static String createSetterName(String property) {
        return METHOD_PREFIX_SET + toProperty(property);
    }

    /**
     * Creates the getter method name for an property.
     * 
     * @param property
     *            the property name.
     * @return the getter name
     */
    public static String createGetterName(String property) {
        return METHOD_PREFIX_GET + toProperty(property);
    }
    
    /**
     * Convert a property to a property name. The first letter will be convert in a upper case.
     * 
     * @param key
     *            the input property name
     * @return the converted property.
     */
    private static String toProperty(final String key) {
        return Character.toUpperCase(key.charAt(0)) + key.substring(1);
    }

    /**
     * Private default constructor.
     */
    private SetterGetterUtil() {
        // nothing
    }
}
