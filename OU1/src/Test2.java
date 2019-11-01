import se.umu.cs.unittest.TestClass;

public class Test2 implements TestClass {

    private MyString myString;

    public Test2() {
    }

    public void setUp() {
        myString=new MyString();
    }

    public void tearDown() {
        myString=null;
    }

    //Test that should succeed
    public boolean testInitialisation() {
        return myString.getLength() == 0;
    }

    //Test that should fail and throw exception
    public boolean testFailingByException(){
        myString.getCharAt(5);

        return true;
    }

    //Test that should succeed
    public boolean testAppendString(){
        myString.appendString("Hello");
        myString.appendString(" ");
        myString.appendString("world!");

        return (myString.returnString().equalsIgnoreCase(
                "Hello world!"));
    }

    // Test that should fail
    public boolean testFailing(){
        return false;
    }
}
