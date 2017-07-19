package testBlog.repository;

import testBlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    User findByUserName(String userName);
    User findById(int Id);

}