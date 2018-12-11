# taxcalculator [![Build Status](https://travis-ci.org/admiralsmaster/taxcalculator.svg?branch=master)](https://travis-ci.org/admiralsmaster/taxcalculator) [![pipeline status](https://gitlab.com/taxcalculator/taxcalculator/badges/master/pipeline.svg)](https://gitlab.com/taxcalculator/taxcalculator/commits/master) [![Codacy Badge](https://api.codacy.com/project/badge/grade/7170c566b7024b55ab256165c4fd4bec)](https://www.codacy.com/app/github-ariel/taxcalculator) [![Known Vulnerabilities](https://snyk.io/test/github/admiralsmaster/taxcalculator/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/admiralsmaster/taxcalculator?targetFile=pom.xml) [![version](https://maven-badges.herokuapp.com/maven-central/info.kuechler.bmf.taxcalculator/taxcalculator/badge.svg)](http://search.maven.org/#search|gav|1|g%3A%22info.kuechler.bmf.taxcalculator%22%20AND%20a%3A%22taxcalculator%22) 

```diff
- The 2018 version will be changed to Java 8. 
```

Generated code for german tax from https://www.bmf-steuerrechner.de/

Project: http://admiralsmaster.github.io/taxcalculator/  
Javadoc: http://admiralsmaster.github.io/taxcalculator/apidocs/

JDK 9 module name: *info.kuechler.bmf.taxcalculator*

For an API to use the test interfaces at the BMF see [taxapi](https://github.com/admiralsmaster/taxapi/).

## Usage

You can download it from maven central repository:

```xml
<dependency>
    <groupId>info.kuechler.bmf.taxcalculator</groupId>
    <artifactId>taxcalculator</artifactId>
    <version>2019.0.0</version>
</dependency>
```

Versions are backwards compatible. You can use the latest version and can use the calculations since 2006.

## Example

### With Reader / Writer

```java
Writer writer = TaxCalculatorFactory.createWithWriter(0, 2019);
// 1. monthly payment
// 2. tax class
// 3. income in cent
writer.set("LZZ", 2).set("STKL", 1).set("RE4", new BigDecimal("223456"));
// 4. a half child :)
// 5. additional med insurance [percent]
// 6. pensions fund: east germany
final Map<String, Object> parameter = new HashMap<>();
parameter.put("ZKF", 0.5);
parameter.put("KVZ", 1.10);
parameter.put("KRV", 1);
writer.setAll(parameter); // with setAll the correct type is detected

// calculate result and return a Reader to read values
Reader reader = writer.calculate();

BigDecimal lst = reader.get("LSTLZZ");
BigDecimal soli = reader.get("SOLZLZZ");

Assert.assertEquals(23350, lst.longValue());
Assert.assertEquals(825, soli.longValue());

System.out.println("Lohnsteuer: " + lst.divide(new BigDecimal("100")) + " EUR");
System.out.println("Soli: " + soli.divide(new BigDecimal("100")) + " EUR");
```

### With Accessor / Direct with generated classes

Both variants can found in this [Test](https://github.com/admiralsmaster/taxcalculator/blob/development/src/test/java/info/kuechler/bmf/taxcalculator/DocumentationExampleTest.java)

## License

[MIT License](http://opensource.org/licenses/mit-license.php)
