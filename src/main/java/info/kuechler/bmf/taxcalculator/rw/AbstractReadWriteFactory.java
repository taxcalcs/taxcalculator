package info.kuechler.bmf.taxcalculator.rw;

import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.METHOD_PREFIX_GET;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.METHOD_PREFIX_SET;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.getParameterCount;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.getPropertyName;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.isMethod;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.toCaseInsensitiveProperty;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract factory to creates a {@link Writer} to write values to a call.
 * <p>
 * An example can be found on implementation class {@link TaxCalculatorFactory}.
 */
public abstract class AbstractReadWriteFactory {
    private final Map<String /* methodType-key */, Map<String /* property in upper case */, Method>> methodCache;
    private final String calculateMethodName;

    /**
     * Constructor.
     * 
     * @param calculateMethodName
     *            method name to call the calculation. This have to be a method without parameters.
     */
    public AbstractReadWriteFactory(String calculateMethodName) {
        this.calculateMethodName = calculateMethodName;
        this.methodCache = newSyncMap();
    }

    /**
     * This method have to be return the class object for the class referenced by your key. You send your (whatever) key
     * and returns a class object.
     * 
     * @param classKey
     *            the key to reference the class
     * @return the class object
     * @throws ClassNotFoundException
     *             class does not exists
     */
    protected abstract Class<?> getCalculatorClass(final String classKey) throws ClassNotFoundException;

    /**
     * Create a {@link Writer}. The key is used to create a instance and detect all methods (calculate, getter, setter).
     * 
     * @param classKey
     *            the key to reference the class. The method {@link #getCalculatorClass(String)} is called with this
     *            key.
     * @return a {@link Writer}
     * @throws ReadWriteException
     *             error during create a {@link Writer}
     */
    public Writer create(final String classKey) throws ReadWriteException {

        try {
            final Class<?> clazz = getCalculatorClass(classKey);
            final Object taxCalculator = clazz.newInstance();
            return new Writer(getCalculateMethod(clazz, classKey), getSetter(classKey, clazz),
                    getGetter(classKey, clazz), taxCalculator);
        } catch (ClassNotFoundException e) {
            throw new ReadWriteException("Class not found for key: " + classKey, e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReadWriteException("Cannot create instance for class referenced by key: " + classKey, e);
        }
    }

    /**
     * Get all input fields from a class.
     * 
     * @param classKey
     *            the key to reference the class. The method {@link #getCalculatorClass(String)} is called with this
     *            key.
     * @return a {@link Set} of input names. Names are case insensitive.
     * @throws ReadWriteException
     *             class cannot detect
     */
    public Set<String> getInputs(final String classKey) throws ReadWriteException {
        try {
            final Class<?> clazz = getCalculatorClass(classKey);
            return new HashSet<>(getSetter(classKey, clazz).keySet());
        } catch (ClassNotFoundException e) {
            throw new ReadWriteException("Class not found for key: " + classKey, e);
        }
    }

    /**
     * Get all input fields from a class with the type. Should be {@link BigDecimal}, int or double.
     * 
     * @param classKey
     *            the key to reference the class. The method {@link #getCalculatorClass(String)} is called with this
     *            key.
     * @return {@link Map} with name and type. Names are case insensitive.
     * @throws ReadWriteException
     *             class cannot detect
     * @since 2016.2.0
     */
    public Map<String, Class<?>> getInputsWithType(final String classKey) throws ReadWriteException {
        try {
            final Class<?> clazz = getCalculatorClass(classKey);
            return convertIntoFirstParameterMap(getSetter(classKey, clazz));
        } catch (ClassNotFoundException e) {
            throw new ReadWriteException("Class not found for key: " + classKey, e);
        }
    }

    /**
     * Get all output fields from a class.
     * 
     * @param classKey
     *            the key to reference the class. The method {@link #getCalculatorClass(String)} is called with this
     *            key.
     * @return a {@link Set} of output names. Names are case insensitive.
     * @throws ReadWriteException
     *             class cannot detect
     */
    public Set<String> getOutputs(final String classKey) throws ReadWriteException {
        try {
            final Class<?> clazz = getCalculatorClass(classKey);
            final Set<String> result = new HashSet<>(getGetter(classKey, clazz).keySet());
            result.removeAll(getSetter(classKey, clazz).keySet());
            return result;
        } catch (ClassNotFoundException e) {
            throw new ReadWriteException("Class not found for key: " + classKey, e);
        }
    }

    /**
     * Get all output fields from a class. Should be {@link BigDecimal}, int or double.
     * 
     * @param classKey
     *            the key to reference the class. The method {@link #getCalculatorClass(String)} is called with this
     *            key.
     * @return a {@link Map} of output names and type. Names are case insensitive.
     * @throws ReadWriteException
     *             class cannot detect
     * @since 2016.2.0
     */
    public Map<String, Class<?>> getOutputsWithType(final String classKey) throws ReadWriteException {
        try {
            final Class<?> clazz = getCalculatorClass(classKey);
            final Map<String, Method> result = new HashMap<>(getGetter(classKey, clazz));
            for (final String setter : getSetter(classKey, clazz).keySet()) {
                result.remove(setter);
            }
            return convertIntoReturnTypeMap(result);
        } catch (ClassNotFoundException e) {
            throw new ReadWriteException("Class not found for key: " + classKey, e);
        }
    }

    /**
     * Returns the <code>calculate</code> method. Use cache and use {@link #calcCalculateMethod(Class)} to find not
     * cached method.
     * 
     * @param clazz
     *            the class
     * @param key
     *            the class key
     * @return the method
     * @throws ReadWriteException
     *             an error
     * @see #calcCalculateMethod(Class)
     */
    protected Method getCalculateMethod(final Class<?> clazz, final String key) throws ReadWriteException {
        final String mapKey = key + '-' + "calculate-" + calculateMethodName;
        if (!methodCache.containsKey(mapKey)) {
            final Map<String, Method> methodCollection = calcCalculateMethod(clazz);
            // maybe more than one initializations parallel
            methodCache.put(mapKey, methodCollection);
        }
        return methodCache.get(mapKey).get(calculateMethodName);
    }

    /**
     * Get the <code>calculate</code> method from the class. Not cached.
     * 
     * @param clazz
     *            the class
     * @return the method
     * @throws ReadWriteException
     *             an error
     * @see #getCalculateMethod(Class, String)
     */
    protected Map<String, Method> calcCalculateMethod(Class<?> clazz) throws ReadWriteException {
        final Map<String, Method> methodCollection;
        try {
            methodCollection = Collections.singletonMap(calculateMethodName,
                    clazz.getDeclaredMethod(calculateMethodName));
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ReadWriteException("Cannot detect calculate method for class referenced by key.", e);
        }
        return methodCollection;
    }

    /**
     * Creates a new synchronized {@link Map}.
     * 
     * @param <K>
     *            key type
     * @param <V>
     *            value type
     * 
     * @return the new {@link Map}
     */
    protected <K, V> Map<K, V> newSyncMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * Get all getter methods from the class. Use the cache.
     * 
     * @param classKey
     *            the class key
     * @param clazz
     *            the class
     * @return {@link Map} of getter (key=property key (upper case), value= {@link Method})
     */
    protected Map<String, Method> getGetter(final String classKey, final Class<?> clazz) {
        return getMethods(clazz, classKey, METHOD_PREFIX_GET, 0);
    }

    /**
     * Get all setter methods from the class. Use the cache.
     * 
     * @param classKey
     *            the class key
     * @param clazz
     *            the class
     * @return {@link Map} of getter (key=property key (upper case), value= {@link Method})
     */
    protected Map<String, Method> getSetter(final String classKey, final Class<?> clazz) {
        return getMethods(clazz, classKey, METHOD_PREFIX_SET, 1);
    }

    /**
     * 
     * Search methods which expected prefix and parameter count on a class. Use a cache and use
     * {@link #calcMethods(Class, String, int)} to find not cached entries.
     * 
     * @param clazz
     *            the class
     * @param key
     *            the class key
     * @param methodTypePrefix
	 *            expected method prefix like {@value SetterGetterUtil#METHOD_PREFIX_GET} or
	 *            {@value SetterGetterUtil#METHOD_PREFIX_SET}
     * @param parameterCount
     *            expected count of parameter
     * @return the {@link Map} with methods which is expected (key=property key (upper case), value= {@link Method})
     * @see #calcMethods(Class, String, int)
     */
    protected Map<String, Method> getMethods(final Class<?> clazz, final String key, final String methodTypePrefix,
            final int parameterCount) {
        final String mapKey = key + '-' + methodTypePrefix + '-' + parameterCount;
        if (!methodCache.containsKey(mapKey)) {
            final Map<String, Method> methodCollection = calcMethods(clazz, methodTypePrefix, parameterCount);
            // maybe more than one initializations parallel
            methodCache.put(mapKey, methodCollection);
        }
        return methodCache.get(mapKey);
    }

    /**
     * Search methods which expected prefix and parameter count on a class. Not cached.
     * 
     * @param clazz
     *            the class
	 * @param methodTypePrefix
	 *            expected method prefix like {@value SetterGetterUtil#METHOD_PREFIX_GET} or
	 *            {@value SetterGetterUtil#METHOD_PREFIX_SET}
     * @param parameterCount
     *            expected count of parameter
     * @return the {@link Map} with methods which is expected (key=property key (upper case), value= {@link Method})
     * @see #getMethods(Class, String, String, int)
     */
    protected Map<String, Method> calcMethods(final Class<?> clazz, final String methodTypePrefix,
            final int parameterCount) {
        final Map<String, Method> methodCollection = newSyncMap();
        final Set<String> filterMethods = getIgnoredMethods();
        for (final Method method : clazz.getMethods()) {
            final String name = method.getName();
            if (isMethod(methodTypePrefix, name) && getParameterCount(method) == parameterCount
                    && !filterMethods.contains(name)) {
                methodCollection.put(toCaseInsensitiveProperty(getPropertyName(name)), method);
            }
        }
        return methodCollection;
    }

    /**
     * Returns the methods which are ignored.
     * 
     * @return {@link Set}
     */
    protected Set<String> getIgnoredMethods() {
        return Collections.singleton("getClass");
    }

    /**
     * Converts a {@link Map} with property name and method into a {@link Map} with property key and return type.
     * 
     * @param input
     *            {@link Map} with property name and method
     * @return {@link Map} with property key and return type
     */
    protected Map<String, Class<?>> convertIntoReturnTypeMap(final Map<String, Method> input) {
        final Map<String, Class<?>> result = new HashMap<>();
        for (final Map.Entry<String, Method> entry : input.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getReturnType());
        }
        return result;
    }

    /**
     * Converts a {@link Map} with property name and method into a {@link Map} with property key and type of the first
     * parameter.
     * 
     * @param input
     *            {@link Map} with property name and method
     * @return {@link Map} with property key and and type of the first parameter
     */
    protected Map<String, Class<?>> convertIntoFirstParameterMap(final Map<String, Method> input) {
        final Map<String, Class<?>> result = new HashMap<>();
        for (final Map.Entry<String, Method> entry : input.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getParameterTypes()[0]);
        }
        return result;
    }
}
