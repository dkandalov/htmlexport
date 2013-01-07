package htmlexport.functest_input;

import java.util.Date;

import com.sun.corba.se.spi.legacy.interceptor.UnknownType;

/* Block comment */
import java.util.Date;

/**
 * Doc comment here for <code>SomeClass</code>
 *
 * @see Math#sin(double)
 */
@Annotation(name = value)
public class SomeClass<T extends Runnable> { // some comment
    private T field = null;
    private double unusedField = 12345.67890;
    private UnknownType anotherString = "Another\nStrin\g";
    public static int staticField = 0;

    public SomeClass(AnInterface param, int[] reassignedParam) {
        int localVar = "IntelliJ"; // Error, incompatible types
        System.out.println(anotherString + field + localVar);
        long time = Date.parse("1.2.3"); // Method is deprecated
        int reassignedValue = this.staticField;
        reassignedValue++;
        new SomeClass() {
            {
                int a = localVar;
            }
        };
        reassignedParam = new int[2];
    }
}

interface AnInterface {
    int CONSTANT = 2;

    void method();
}

abstract class SomeAbstractClass {
}
