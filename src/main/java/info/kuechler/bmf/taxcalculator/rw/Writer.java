package info.kuechler.bmf.taxcalculator.rw;

import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.getFirstParameterType;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.toCaseInsensitiveProperty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Class to set input values in tax calculator classes.
 * 
 * <p>
 * To create this class use the {@link AbstractReadWriteFactory} class.
 * </p>
 * 
 * @see AbstractReadWriteFactory#create(String)
 */
public class Writer {

    private final Object object;
    private final Map<String /* property with upper cases */, Method> setter;
    private final Map<String /* property with upper cases */, Method> getter;
    private final Method calculate;

    /**
     * Constructor.
     * 
     * @param calculate
     *            the calculate method.
     * @param setter
     *            the map with setter methods.
     * @param getter
     *            the map with getter methods.
     * @param object
     *            the object for manipulation.
     */
    Writer(final Method calculate, final Map<String, Method> setter, final Map<String, Method> getter, Object object) {
        this.calculate = calculate;
        this.setter = setter;
        this.getter = getter;
        this.object = object;
    }

    /**
     * Initialize all values with zero.
     * 
     * @return the {@link Writer} object itself.
     * @throws ReadWriteException
     *             Error while setting the values.
     */
    public Writer setAllToZero() throws ReadWriteException {
        for (final Map.Entry<String, Method> entry : setter.entrySet()) {
            set(entry.getKey(), getZero(getFirstParameterType(entry.getValue())));
        }
        return this;
    }

    /**
     * Set a value. The type of the value have to be correct.
     * 
     * @param key
     *            the property name, is case insensitive
     * @param value
     *            the value to set.
     * @param <T>
     *            the result type
     * @return the {@link Writer} object itself.
     * @throws ReadWriteException
     *             Error while set the values.
     */
    public <T> Writer set(final String key, final T value) throws ReadWriteException {
        final Method method = setter.get(toCaseInsensitiveProperty(key));
        if (method == null) {
            throw new ReadWriteException("Setter for property not found: " + key);
        }
        try {
            method.invoke(object, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReadWriteException("Cannot set property: " + key, e);
        } catch (IllegalArgumentException e) {
            throw new ReadWriteException("Cannot set property: " + key + ". Maybe wrong type: should: "
                    + getFirstParameterType(method) + ", is: " + value.getClass(), e);
        }
        return this;
    }

    /**
     * Set all the values from the Map.
     * 
     * @param values
     *            the values to set.
     * @param <T>
     *            the result type
     * @return the current {@link Writer} object itself
     * @throws ReadWriteException
     *             Error while setting the values.
     * @see #set(String, Object)
     */
    public <T> Writer setAll(final Map<String, ?> values) throws ReadWriteException {
        for (final Map.Entry<String, ?> entry : values.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Call the calculation method.
     * 
     * @return The {@link Reader} object to access the output values.
     * @throws ReadWriteException
     *             Error while setting the values.
     */
    public Reader calculate() throws ReadWriteException {
        try {
            calculate.invoke(object);
            return new Reader(getter, object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ReadWriteException("Cannot call calculate method.", e);
        }
    }

    /**
     * Returns a zero value for numbers. In the other cases return <code>null</code>.
     * 
     * @param clazz
     *            the type of the requested value.
     * @return the zero value.
     */
    private Object getZero(Class<?> clazz) {
        if (int.class == clazz) {
            return Integer.valueOf(0);
        }
        if (double.class == clazz) {
            return Double.valueOf(.0);
        }

        if (BigDecimal.class == clazz) {
            return BigDecimal.ZERO;
        }
        if (byte.class == clazz) {
            return Byte.valueOf((byte) 0);
        }
        if (short.class == clazz) {
            return Short.valueOf((short) 0);
        }
        if (long.class == clazz) {
            return Long.valueOf(0l);
        }
        if (float.class == clazz) {
            return Float.valueOf(0f);
        }
        return null;
    }

}
