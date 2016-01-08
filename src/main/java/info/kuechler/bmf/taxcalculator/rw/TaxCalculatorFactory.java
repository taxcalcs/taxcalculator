package info.kuechler.bmf.taxcalculator.rw;

/**
 * Implementation for a ReadWriteFactory for the BMF tax calculator.
 * <p>
 * Example: <code>
 *     final TaxCalculatorFactory factory = new TaxCalculatorFactory();
 *     final Writer input = factory.create("2015Dezember").setAllToZero();
 *     
 *     // set values
 *     input.set("KVZ", new BigDecimal("0.90"));
 *     // ...
 *     
 *     // calculate
 *     final Reader output = input.calculate();
 *     
 *     // read
 *     System.out.println("LSTLZZ " + output.get("LSTLZZ"));
 * </code>
 */
public class TaxCalculatorFactory extends AbstractReadWriteFactory {

    /**
     * Constructor.
     */
    public TaxCalculatorFactory() {
        super("calculate");
    }

    /**
     * {@inheritDoc}
     * 
     * Creates a object from following class:
     * <code>"info.kuechler.bmf.taxcalculator.Lohnsteuer" + yearKey + "Big"</code>
     */
    @Override
    protected Class<?> getCalculatorClass(final String yearKey) throws ClassNotFoundException {
        final Class<?> clazz = Class.forName("info.kuechler.bmf.taxcalculator.Lohnsteuer" + yearKey + "Big");
        return clazz;
    }
}
