package LogIn;

public class Test {
    private Test(){

    }
    private void test1(){
        System.out.println(" Batman");
    }
    public static void main(String [] args){
        Test test = new Test();
        test.test1();
        //System.out.println(test);
        Test test1 = new Test();
        System.out.println(test1);
        System.out.println(test);
    }
}
