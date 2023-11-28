package todo.jdbc;


import todo.model.TaskStatus;
import todo.model.ToDo;
import todo.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class JDBC {
   static Connection connection = null;

   public void createUser(User user){

       try {
           connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_jon", "user_jon", "12345"); // <2>
           Statement statement = connection.createStatement(); // <3>
           String sql = "insert into users(name,surname,password ) values('%s','%s','%s')";
           sql = String.format(sql, user.getName(),user.getSurname(),user.getPassword());

           int effectedRows = statement.executeUpdate(sql); // <4>
       } catch (SQLException e) {
           throw new RuntimeException(e);
       } finally {

           try {
               if (connection != null) {
                   connection.close();
               }
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       }

   }


    public void createTask(ToDo toDo, User user) {


        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_jon", "user_jon", "12345"); // <2>
            Statement statement = connection.createStatement(); // <3>
            String sql = "insert into todo2(user_id,title,content,status) values('%s','%s','%s','%s')";
            sql = String.format(sql,user.getId(), toDo.getTitle(), toDo.getContent(), TaskStatus.ACTIVE);

            int effectedRows = statement.executeUpdate(sql); // <4>
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {

            try {
                if (connection != null) {
                    System.out.println("new task was created✏️✏️✏️✏️✏️");
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<ToDo> getListTasks(User user) {
        List<ToDo> dtoList = new LinkedList<>();
        try {
             connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_jon", "user_jon", "12345"); // <2>
            Statement statement = connection.createStatement(); // <3>
            ResultSet rs = statement.executeQuery("select id,todo2.user_id , title,content,status,todo2.create_date,finished_date from todo2" +
                    " inner join users on users.user_id=todo2.user_id where todo2.user_id="+user.getId()); // <4>

            while (rs.next()) {
                ToDo dto = new ToDo();
                dto.setId(rs.getInt("id"));
                dto.setUser_id(rs.getInt("user_id"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setStatus(TaskStatus.valueOf(rs.getString("status")));
                dto.setCreatedDate(rs.getTimestamp("create_date").toLocalDateTime());
                dto.setFinishedDate(rs.getTimestamp("finished_date"));

                dtoList.add(dto);
            }
            connection.close(); // <5>
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dtoList;
    }

    public List<User> getListOFUser(){
        List<User> userList = new LinkedList<>();
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_jon", "user_jon", "12345"); // <2>
            Statement statement = connection.createStatement(); // <3>
            ResultSet rs = statement.executeQuery("select user_id,name, surname,password,create_date from users"); // <4>

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setPassword(rs.getString("password"));
                user.setCreate_date(rs.getTimestamp("create_date"));

                userList.add(user);
            }
            connection.close(); // <5>
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void update(Integer id, ToDo dto) {
        try {
             connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_jon", "user_jon", "12345"); // <2>
            Statement statement = connection.createStatement(); // <3>
//            String sql = "update student set name = '"+dto.getName()+"', surname ='"+dto.getSurname()+"' where id = "+id;
            String sql = "update todo2 set title = '%s', content ='%s',status='%s'  where id = %d";
            sql = String.format(sql, dto.getTitle(), dto.getContent(), TaskStatus.ACTIVE, id);
            int effectedRows = statement.executeUpdate(sql); // <4>
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Integer id) {
        try {
              connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_jon", "user_jon", "12345"); // <2>
            Statement statement = connection.createStatement(); // <3>

            String sql = "delete from todo2 where id = " + id;
            int effectedRows = statement.executeUpdate(sql); // <4>
            connection.close();
            return effectedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void markAsDone(Integer id) {
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
             connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/user_jon", "user_jon", "12345"); // <2>
            Statement statement = connection.createStatement(); // <3>
//            String sql = "update student set name = '"+dto.getName()+"', surname ='"+dto.getSurname()+"' where id = "+id;
            String sql = "update todo2 set status='%s',finished_date=now()  where id = %d";
            sql = String.format(sql, TaskStatus.DONE, id);
            int effectedRows = statement.executeUpdate(sql); // <4>
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
