package service;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> users = new ArrayList<>();

    public void addUser(String id, String name) {
        if (userExists(id)) {
            throw new IllegalArgumentException("Bu ID ile kullanıcı zaten var");
        }
        users.add(new User(id, name));
    }

    public User findUserById(String id) {
        for (User u : users) {
            if (u.getId().equals(id)) return u;
        }
        return null;
    }

    public boolean userExists(String id) {
        return findUserById(id) != null;
    }

    /**
     * Kullanıcı listesinin boş olup olmadığını kontrol eder.
     *
     * @return Kullanıcı listesi boş ise true, aksi halde false
     */
    public boolean hasUsers() {
        return !users.isEmpty();
    }

    public List<User> getAllUsers() {
        return users;
    }

    /**
     * Belirtilen ID'ye sahip kullanıcıyı siler.
     *
     * @param id Silinecek kullanıcının ID'si
     * @return Kullanıcı bulunup silindi ise true, aksi halde false
     */
    public boolean deleteUser(String id) {
        User user = findUserById(id);
        if (user == null) {
            return false;
        }
        users.remove(user);
        return true;
    }

    /**
     * Tüm kullanıcıları siler.
     */
    public void deleteAllUsers() {
        users.clear();
    }
}
