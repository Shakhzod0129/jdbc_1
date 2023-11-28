package todo.service;

import todo.jdbc.JDBC;
import todo.model.TaskStatus;
import todo.model.ToDo;
import todo.model.User;

public class Service {
    static Scanner scanner = new Scanner();
    static JDBC jdbc = new JDBC();

    static ToDo toDo = new ToDo();
    static User user=new User();

    public void start() {
        while (true) {

            System.out.println("""
                    1.Sign in
                    2.Sign up
                    0.Exit
                    """);
            String option = scanner.printString("Choose optiion : ");

            if(option.equals("0")){
                return;
            }

            if(option.equals("1")){
                String password = scanner.printString("Enter your password : ");
                String name = scanner.printString("Enter your name : ");

                boolean bool=false;
                for (User session_user : jdbc.getListOFUser()) {
                    if(session_user.getName().equals(name)){
                        if(session_user.getPassword().equals(password)){
                             user=session_user;
                            userCabinet(user);
                            bool=true;
                            break;
                        }
                    }
                }
                if(!bool){
                    System.out.println("Account has not found with this name❌❌❌❌");
                }
            }
            else if(option.equals("2")){
                String name = scanner.printString("Enter your name : ");
                String surname = scanner.printString("Enter your surname : ");
                String password = scanner.printString("Enter your password : ");

                user.setName(name);
                user.setSurname(surname);
                user.setPassword(password);

                jdbc.createUser(user);
                boolean bool=false;
                for (User session_user : jdbc.getListOFUser()) {
                    if(session_user.getName().equals(name)){
                        if(session_user.getPassword().equals(password)){
                            user=session_user;
                            System.out.println("You has signed up successfully✅✅✅✅");
                            userCabinet(user);
                            bool=true;
                            break;
                        }
                    }
                }

                if(!bool){
                    System.out.println("Error");
                }
//

            }

        }
    }

    private static void   userCabinet(User user) {
        while (true) {
            System.out.print("""
                    1.Create
                    2.Active task list
                    3.Finished task list
                    4.Update (by id)
                    5.Delete (by id)
                    6.Mark as done
                    0.Exit
                    """);
            String option = scanner.printString("?: ");
            if (option.equals("0")) {
                System.out.println("Exited");
                return;

            } else if (option.trim().isEmpty()) {
                System.out.println("Choose option:");
            } else {
                switch (option) {
                    case "1" -> createTask(user);
                    case "2" -> listOfActiveTask(user);
                    case "3" -> listofFinishedTask(user);
                    case "4" -> update(user);
                    case "5" -> delete(user);
                    case "6" -> markAsDone(user);
                }
            }
//            }
//        }

        }
    }

    //==================================================================================================================
    private static void createTask(User user) {

        String title = scanner.printString("Write a name for task : ");
        String content = scanner.printString("Write a description for task : ");

        toDo.setTitle(title);
        toDo.setContent(content);

        for (User user1 : jdbc.getListOFUser()) {
            if(user1.getId().equals(user.getId())){

                jdbc.createTask(toDo,user);
            }
        }



    }

    private static void listOfActiveTask(User user) {

        int count = 0;
        for (ToDo listTask : jdbc.getListTasks(user)) {
            if (listTask.getStatus().equals(TaskStatus.ACTIVE)) {
                if(listTask.getUser_id().equals(user.getId())){

                    System.out.println(listTask);
                    count++;
                }
            }
        }
        if (count == 0) {
            System.out.println("List of the active tasks is empty🗑️🗑️🗑️🗑️");
        }


    }

    private static void listofFinishedTask(User user) {

        int count = 0;
        for (ToDo listTask : jdbc.getListTasks(user)) {
            if (listTask.getStatus().equals(TaskStatus.DONE)) {
                if(listTask.getUser_id().equals(user.getId())){

                System.out.println(listTask);
                count++;
                }
            }
        }
        if (count == 0) {
            System.out.println("List of the finished tasks is empty");
        }
    }

    private static void update(User user) {

        String id = scanner.printString("Enter ID of the task to edit : ");

        boolean bool=false;
        for (ToDo listTask : jdbc.getListTasks(user)) {
            if (listTask.getId().equals(Integer.parseInt(id))) {
                if(listTask.getUser_id().equals(user.getId())){
                    String title = scanner.printString("Enter a new name for task : ");
                    String content = scanner.printString("Enter a new description for task : ");

                    toDo.setTitle(title);
                    toDo.setContent(content);

                    jdbc.update(Integer.parseInt(id), toDo);
                    bool = true;
                    break;
                }

            }
        }
        if(bool){
            System.out.println("Task is edited successfully♻️♻️♻️♻️♻️");
        }else {
            System.out.println("Task has not found with this ID👀👀👀👀");
        }



    }

    private static void delete(User user) {

        String id = scanner.printString("Enter ID of the task to edit : ");

        boolean bool=false;
        for (ToDo listTask : jdbc.getListTasks(user)) {
            if (listTask.getId().equals(Integer.parseInt(id))) {
                if(listTask.getUser_id().equals(user.getId())){
                    jdbc.delete(Integer.parseInt(id));
                    bool = true;
                    break;
                }

            }
        }
        if(bool){
            System.out.println("Task is deleted successfully❌❌❌❌❌");
        }else {
            System.out.println("Task has not found with this ID👀👀👀👀");
        }


    }

    private static void markAsDone(User user) {
        String id = scanner.printString("Enter ID of the task to mark as done : ");

        boolean bool=false;
        for (ToDo listTask : jdbc.getListTasks(user)) {
            if (listTask.getId().equals(Integer.parseInt(id))) {
                if(listTask.getUser_id().equals(user.getId())){
                    jdbc.markAsDone(Integer.parseInt(id));
                    bool = true;
                    break;
                }

            }
        }

        if(bool){
            System.out.println("Task has done successfully✅✅✅✅✅✅");
        }else {
            System.out.println("Task has not found with this ID👀👀👀👀");
        }


    }

    //==================================================================================================================

}
