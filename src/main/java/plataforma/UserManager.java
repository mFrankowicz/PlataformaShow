package plataforma;

import java.util.HashMap;

public class UserManager {

    private HashMap<String, User> users;

    // the constructor
    // todo: every time the app loads, its going to load this class
    // todo: when loads this class its going to access the database to look for account info
    // todo: loads account info and then asks for the user to login
    // todo: when login, its going to load every user and login to the appropriated account
    // todo: the UserManager class then will manage all the users information
    // todo: this all can be refactored to a better and efficient way?
    public UserManager() {
        users = new HashMap<>();
    }

    // get a HashMap with a User class representing each user
    public HashMap<String, User> getUsers() {
        return users;
    }

    // set the users HashMap
    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    // add a new User user to the users HashMap
    // todo: implement the database link
    public void addUser(String name, String password, String tabId) {
        users.put(name, new User(name, password, tabId));
    }

    // get a user from the users HashMap
    // todo: implement the database link
    public User getUser(String name) {
        User user = users.get(name);
        return user;
    }

    // modify a user name given the user
    // todo: needs to be auth in the given user account to be able to modify only his account
    public void modifyUserName(String name, String newName) {
        User user = users.get(name);
        user.userName = newName;
        users.remove(name);
        users.put(newName, user);
    }

    // modify a user password given the user
    // todo: needs to be auth in the given user account to be able to modify only his account
    public void modifyUserPassword(String name, String newPassword) {
        User user = users.get(name);
        user.userPassword = newPassword;
        users.remove(name);
        users.put(name,user);
    }

    // a static class modeling each user account
    // todo: needs to link to the database, to exchange auth info
    public static class User {

        public String userName;
        public String userPassword;
        public String userTabId;
        public Boolean isAdmin = true;

        User(String _userName, String _userPassword, String _userTabId) {
            userName = _userName;
            userPassword = _userPassword;
            userTabId = _userTabId;
        }

    }

}
