package info.kuechler.bmf.taxcalculator.rw;

import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.toCaseInsensiviteProperty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Class to read values in tax calculator classes.
 * 
 * <p>
 * Create the calculate method from {@link Writer}.
 * </p>
 * 
 * @see Writer#calculate()
 */
public class Reader {
    private final Object object;
    private final Map<String /* property with upper cases */, Method> getter;

    /**
     * Constructor.
     * 
     * @param getter
     *            the map with getter methods.
     * @param object
     *            the object for manipulation.
     */
    Reader(final Map<String, Method> getter, Object object) {
        this.getter = getter;
        this.object = object;
    }

    /**
     * Reads a value.
     * 
     * @param key
     *            the property name, is case insensitive
     * @param <T>
     *            Type of the result object
     * @return the value
     * @throws ReadWriteException
     *             Error while read the values.
     */
    public <T> T get(final String key) throws ReadWriteException {
        final Method method = getter.get(toCaseInsensiviteProperty(key));
        if (method == null) {
            throw new ReadWriteException("Getter for property not found: " + key);
        }
        try {
            @SuppressWarnings("unchecked")
            final T invoke = (T) method.invoke(object);
            return invoke;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ReadWriteException("Cannot read property: " + key, e);
        } catch (ClassCastException e) {
            throw new ReadWriteException(
                    "Cannot cast property: " + key + ". Maybe wrong type: should: " + method.getReturnType(), e);
        }
    }
}
