# taxcalculator [![Build Status](https://travis-ci.org/admiralsmaster/taxcalculator.svg?branch=master)](https://travis-ci.org/admiralsmaster/taxcalculator)
Generated code for german tax from https://www.bmf-steuerrechner.de/


## Usage

You can download it from maven central repository:

```
<dependency>
    <groupId>info.kuechler.bmf.taxcalculator</groupId>
    <artifactId>taxcalculator</artifactId>
    <version>2015.0.0</version>
</dependency>
```

## Example

```
Lohnsteuer2015Big tax = new Lohnsteuer2015Big();
        
tax.setLZZ(2); // monthly payment
tax.setSTKL(1); // tax class
tax.setRE4(new BigDecimal("223456")); // income in cent
tax.setLZZFREIB(BigDecimal.ZERO); // Freibeträge
tax.setPVS(0); // not in saxony
tax.setPVZ(0); // Additional care insurance for employee: birth > 1940, older than 23, no kids
tax.setR(0); // no church
tax.setZKF(new BigDecimal("0.5")); // a half child :)
tax.setKVZ(new BigDecimal("0.90")); // additional med insurance [percent]
tax.setKRV(1); // pensions fund: east germany
  
tax.setVBEZ(BigDecimal.ZERO);
tax.setLZZHINZU(BigDecimal.ZERO);
tax.setSONSTB(BigDecimal.ZERO);
tax.setVKAPA(BigDecimal.ZERO);
tax.setVMT(BigDecimal.ZERO);
        
tax.calculate();
        
System.out.println("Lohnsteuer: " + tax.getLSTLZZ().divide(new BigDecimal("100")) + " EUR");
System.out.println("Soli: " + tax.getSOLZLZZ().divide(new BigDecimal("100")) + " EUR");
```

## License

[MIT License](http://opensource.org/licenses/mit-license.php)