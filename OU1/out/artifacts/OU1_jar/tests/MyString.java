public class MyString {
    private String str;

    public MyString(){
        str = "";
    }

    public String appendString(String appendedString){
        str = str + appendedString;
        return str;
    }

    public char getCharAt(int index){
        return str.charAt(index);
    }

    public int getLength(){
        return str.length();
    }

    public String returnString(){
        return str;
    }

}

