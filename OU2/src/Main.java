public class Main {

    public static void main(String[] args){

        /*Model m = new Model();
        View v = new View();

        Controller c = new Controller(m,v);
        c.init();

        m.runningLoop();
        */
        System.out.println(happiness(4));


    }

    private static int happiness(int cup){

        return cup == 0 ? 0 : 3 + happiness(cup-1);

    }


}
