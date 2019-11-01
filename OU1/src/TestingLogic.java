import se.umu.cs.unittest.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TestingLogic {

    /**
     * Execute valid test methods of given className.
     * Returns result in formated String
     *
     * @param  className Name of class to execute
     * @return String representation of test result
     */
    public static String performTest(String className){

        try {
            Class c = Class.forName(className);

            if(isValidTestClass(c)){
                System.out.println(c + " is a valid TestClass.");

                // Getting correct TestMethods
                List<Method> ValidMethods = parseValidMethods(c);

                // Invoking testMethods and summarize result in resultStruct
                List<ResultStruct> resultStruct = new ArrayList<ResultStruct>();

                for(int j = 0; j < ValidMethods.size(); j++){

                    try {

                        Object ClassInstance = c.newInstance();

                        // Run setUp if it exists
                        runSetUp(ClassInstance);

                        // Run TestMethod if it exists
                        resultStruct.add(runTestMethod(ClassInstance,
                                ValidMethods.get(j)));

                        // Run tearDown if it exists
                        runTearDown(ClassInstance);

                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                return buildStringOfResultStruct(ValidMethods, resultStruct);

            }else{
                return c + " is NOT a valid TestClass.";
            }

        } catch (ClassNotFoundException e) {
            return "Could not find class: " + className;
        }
    }

    /**
     * Filter out valid test methods from a given class.
     *
     * @param  c Class to filter for valid test methods
     * @return List of valid methods
     */
    private static List<Method> parseValidMethods(Class c) {
        Method[] methods = c.getMethods();
        List<Method> ValidMethods = new ArrayList<Method>();

        for(int i = 0; i<methods.length;i++){
            if(isValidTestMethod(methods[i])){
                ValidMethods.add(methods[i]);
            }
        }

        return ValidMethods;
    }

    /**
     * Construct a presentable String representation of an array of ResultStruct:s
     *
     * @param  validMethods Array of corresponding methods
     * @param resultStruct Array of results from test method calls.
     * @return String representation of test result
     */
    private static String buildStringOfResultStruct(
            List<Method> validMethods, List<ResultStruct> resultStruct) {

        int NumberOfSuccess = 0;
        int NumberOfError = 0;
        int NumberOfExceptionError = 0;
        String OutputMessage = "";

        // Summarying resultStructs
        for(int k = 0; k<resultStruct.size(); k++){
            if(resultStruct.get(k).success){
                OutputMessage += validMethods.get(k).getName() + ": SUCCESS\n";
                NumberOfSuccess++;
            }else{
                NumberOfError++;
                if(resultStruct.get(k).exception){
                    OutputMessage += validMethods.get(k).getName()
                            + ": FAIL Generated a " +
                            resultStruct.get(k).exceptionName +"\n";

                    NumberOfExceptionError++;
                }else{
                    OutputMessage += validMethods.get(k).getName() + ": FAIL\n";
                }
            }
        }

        OutputMessage += "\n";
        OutputMessage += NumberOfSuccess + " tests succeded\n";
        OutputMessage += NumberOfError + " tests failed\n";
        OutputMessage += NumberOfExceptionError +
                " tests failed because of exceptions\n";

        return OutputMessage;
    }


    /**
     * Run SetUp on class if it exists
     *
     * @param  ClassInstance  Instance of class to call setup on
     */
    private static void runSetUp(Object ClassInstance){

        try {
            Method runSetup = ClassInstance.getClass().getMethod("setUp");
            runSetup.invoke(ClassInstance);
        } catch (NoSuchMethodException e) {
            System.out.println("NoSuchMethodException when executing setUp");
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException when executing setUp");
        } catch (InvocationTargetException e) {
            System.out.println(e.getCause() + " when executing setUp");
        }
    }

    /**
     * Run TearDown on class if it exists
     *
     * @param  CI  Instance of class to call TearDown on
     */
    private static void runTearDown(Object CI){

        try {
            Method tearDown = CI.getClass().getMethod("tearDown");
            tearDown.invoke(CI);
        } catch (NoSuchMethodException e) {
            System.out.println("NoSuchMethodException when executing tearDown");
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException when executing tearDown");
        } catch (InvocationTargetException e) {
            System.out.println(e.getCause() + " when executing tearDown");
        }
    }


    /**
     * Runs a specific test method on a class instance.
     * The result is then stored in a ResultStruct.
     *
     * @param  classInstance  Instance of class to invoke test method on
     * @param m Method to invoke
     * @return ResultStruct object storing test result
     */
    private static ResultStruct runTestMethod(Object classInstance, Method m){

        ResultStruct resultStr = new ResultStruct();
        resultStr.success = false;
        resultStr.exception = false;

        try {

            Object returnedObject = m.invoke(classInstance);
            Boolean returnVal = (Boolean)returnedObject;
            resultStr.success = returnVal.booleanValue();

        } catch (IllegalAccessException e) {

            resultStr.exceptionName = String.valueOf(e.getCause());
            resultStr.exception = true;
        } catch (InvocationTargetException e) {

            resultStr.exceptionName = String.valueOf(e.getCause());
            resultStr.exception = true;
        }

        return resultStr;
    }

    /**
     * Tests if a method is a valid test method
     * 1. Starts with "test.."
     * 2. Have no parameters
     * 3. Return a boolean
     *
     * @param  m  Method to test
     * @return Valid method - True / Invalid method - False
     */
    private static boolean isValidTestMethod(Method m){

        String MethodName = m.getName();

        // Check length of method
        if(MethodName.length() <= 3){
            return false;
        }

        // Start with "test.."
        if(MethodName.startsWith("test")){
            // No parameters
            if(m.getParameterTypes().length == 0){
                // returns bool
                if(m.getReturnType().getName().equals(boolean.class.getName())){
                    return true;
                }

            }
        }

        return false;
    }


    /**
     * Tests if a class is a valid TestClass
     * 1. Implements TestClass
     * 2. Have constructor with no parameters
     *
     * @param  c  Class to perform tests on
     * @return Valid class - True / Invalid class - False
     */
    private static boolean isValidTestClass(Class c){

        // Check for interfacing TestClass
        Class[] interfaces = c.getInterfaces();
        boolean foundTestClassInterface = false;

        // Check if class implements TestClass
        for(int interfaceNr = 0; interfaceNr<interfaces.length; interfaceNr++){
            if(interfaces[interfaceNr].getName().equals(TestClass.class.getName())){
                foundTestClassInterface = true;
            }
        }

        // Check constructor to take no parameters
        Constructor[] constr = c.getConstructors();
        boolean foundCorrectConstructor = false;
        for(int constrNr = 0; constrNr< constr.length; constrNr++){

            int nrOfParam = constr[constrNr].getParameterTypes().length;

            if(nrOfParam == 0){
                foundCorrectConstructor = true;
            }
        }

        return foundTestClassInterface && foundCorrectConstructor;
    }
}
