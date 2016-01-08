package info.kuechler.bmf.taxcalculator.rw;

import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.getParameterCount;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.getPropertyName;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.isMethod;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.toCaseInsensiviteProperty;

import java.lang.reflect.Method;
import java.util.Collections;
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
    private final Map<String /* methodType-key */, Map<String /*
                                                               * property in upper case
                                                               */, Method>> methodCache;
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
     * Create a {@link Writer}. The key is used to create a
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

    private Method getCalculateMethod(final Class<?> clazz, final String key) throws ReadWriteException {
        final String mapKey = key + '-' + "calculate-" + calculateMethodName;
        if (!methodCache.containsKey(mapKey)) {
            final Map<String, Method> methodCollection = calcCalculateMethod(clazz);
            // maybe more than one initializations parallel
            methodCache.put(mapKey, methodCollection);
        }
        return methodCache.get(mapKey).get(calculateMethodName);
    }

    private Map<String, Method> calcCalculateMethod(Class<?> clazz) throws ReadWriteException {
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
     * @return the new {@link Map}
     */
    private <K, V> Map<K, V> newSyncMap() {
        return new ConcurrentHashMap<>();
    }

    private Map<String, Method> getGetter(final String classKey, final Class<?> clazz) {
        return getMethods(clazz, classKey, "get", 0);
    }

    private Map<String, Method> getSetter(final String classKey, final Class<?> clazz) {
        return getMethods(clazz, classKey, "set", 1);
    }

    private Map<String, Method> getMethods(final Class<?> clazz, final String key, final String methodTypePrefix,
            final int parameterCount) {
        final String mapKey = key + '-' + methodTypePrefix + '-' + parameterCount;
        if (!methodCache.containsKey(mapKey)) {
            final Map<String, Method> methodCollection = calcMethods(clazz, methodTypePrefix, parameterCount);
            // maybe more than one initializations parallel
            methodCache.put(mapKey, methodCollection);
        }
        return methodCache.get(mapKey);
    }

    private Map<String, Method> calcMethods(final Class<?> clazz, final String methodTypePrefix,
            final int parameterCount) {
        final Map<String, Method> methodCollection = newSyncMap();
        for (final Method method : clazz.getMethods()) {
            final String name = method.getName();
            if (isMethod(methodTypePrefix, name) && getParameterCount(method) == parameterCount) {
                methodCollection.put(toCaseInsensiviteProperty(getPropertyName(name)), method);
            }
        }
        return methodCollection;
    }
}
