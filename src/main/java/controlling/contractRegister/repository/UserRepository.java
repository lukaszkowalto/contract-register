package controlling.contractRegister.repository;

import controlling.contractRegister.dic.UserRole;
import controlling.contractRegister.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE \n" +
            "\t\t\tLOWER(COALESCE(u.firstName, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(COALESCE(u.lastName, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))\n" +
            "\t\tOR LOWER(COALESCE(u.email, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) ")
    Page<User> getAllUsersFiltered(@Param("pattern") String pattern, Pageable pageable);

    @Modifying
    @Query("UPDATE User u set u.role = :role, u.accountActivated = true WHERE u.id = :id")
    void updateRoleAndActivateAccount(@Param(value = "id") Integer id, @Param(value = "role") UserRole role);
}