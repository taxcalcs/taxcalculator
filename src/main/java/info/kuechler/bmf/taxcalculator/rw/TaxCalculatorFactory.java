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
    
    /**
     * Returns the year you to use with {@link TaxCalculatorFactory#create(String)}.
     * 
     * @param month
     *            1..12 for the month, with 0 you can choose the key from the end of year
     * @param year
     *            the year, have to be &gt;= 2006
     * @return the key
     */
    public String getYearKey(final int month, final int year) {
        if (year < 2006 || month > 12 || month < 0) {
            throw new IllegalArgumentException("Month have to be between 0 and 12, year >= 2006");
        }
        if (year == 2011) {
            if (month == 12 || month == 0) {
                return "2011December";
            }
            return "2011November";
        }
        if (year == 2015 && (month == 12 || month == 0)) {
            return "2015Dezember";
        }
        return Integer.toString(year);
    }
}
