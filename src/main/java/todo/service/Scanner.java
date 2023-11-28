package todo.service;

public class Scanner {
    java.util.Scanner scanner = new java.util.Scanner(System.in);

    public String printString(String s) {
        System.out.print(s);
        return scanner.nextLine();
    }

}
