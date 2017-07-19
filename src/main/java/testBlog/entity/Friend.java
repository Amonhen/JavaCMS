//package testBlog.entity;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Entity
//@Table(name = "friends")
//public class Friend {
//
//    private Integer id;
//    private Integer userId;
//    private String email;
//   // private Set<User> friendUsers;
//
//    public Friend() {
//    }
//
//    public Friend(Integer userId, String email) {
//        this.userId = userId;
//        this.email = email;
//        //this.friendUsers = friendUsers;
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    @Column(nullable = false)
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
//
//    @Column(nullable = false)
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    @ManyToMany(mappedBy = "friends")
//    public Set<User> getFriendUsers() {
//        return friendUsers;
//    }
//
//    public void setFriendUsers(Set<User> friendUsers) {
//        this.friendUsers = friendUsers;
//    }
//}
