package todo;


import todo.service.Service;

public class ToDOMain {

    public static void main(String[] args) {

        Service service=new Service();

        service.start();
    }
}
