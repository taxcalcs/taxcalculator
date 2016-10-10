# taxcalculator [![Build Status](https://travis-ci.org/admiralsmaster/taxcalculator.svg?branch=master)](https://travis-ci.org/admiralsmaster/taxcalculator) [![Codacy Badge](https://api.codacy.com/project/badge/grade/7170c566b7024b55ab256165c4fd4bec)](https://www.codacy.com/app/github-ariel/taxcalculator) [![version](https://img.shields.io/maven-central/v/info.kuechler.bmf.taxcalculator/taxcalculator.svg)](http://search.maven.org/#search|gav|1|g%3A%22info.kuechler.bmf.taxcalculator%22%20AND%20a%3A%22taxcalculator%22) 

Generated code for german tax from https://www.bmf-steuerrechner.de/

Javadoc: http://admiralsmaster.github.io/taxcalculator/

For an API to use the test interfaces see [taxapi](https://github.com/admiralsmaster/taxapi/), for an own test server see [taxserver](https://github.com/admiralsmaster/taxserver).

## Usage

You can download it from maven central repository:

```
<dependency>
    <groupId>info.kuechler.bmf.taxcalculator</groupId>
    <artifactId>taxcalculator</artifactId>
    <version>2016.1.1</version>
</dependency>
```

## Example

### With Reader / Writer

```
final TaxCalculatorFactory factory = new TaxCalculatorFactory();
final Writer writer = factory.create(factory.getYearKey(0, 2015));
writer.setAllToZero();
// 1. monthly payment
// 2. tax class
// 3. income in cent
writer.set("LZZ", 2).set("STKL", 1).set("RE4", new BigDecimal("223456"));
// 4. a half child :)
// 5. additional med insurance [percent]
writer.set("ZKF", new BigDecimal("0.5")).set("KVZ", new BigDecimal("0.90"));
// 6. pensions fund: east germany
writer.set("KRV", 1);

final Reader reader = writer.calculate();

final BigDecimal lst = reader.get("LSTLZZ");
final BigDecimal soli = reader.get("SOLZLZZ");

System.out.println("Lohnsteuer: " + lst.divide(new BigDecimal("100")) + " EUR");
System.out.println("Soli: " + soli.divide(new BigDecimal("100")) + " EUR");
```

### Direct with generated classes

```
Lohnsteuer2015DezemberBig tax = new Lohnsteuer2015DezemberBig();
        
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
