package controlling.contractRegister.service;

import controlling.contractRegister.model.User;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.web.UserDTO;
import controlling.contractRegister.web.UserRoleDTO;

public interface UserService {

    void save(UserDTO user);

    void update(User user);

    User getUserByEmail(String email);

    boolean existsUserById(Integer userId);

    void deleteUserById(Integer userId);

    void setRoleAndActivateAccount(UserRoleDTO userRole);

    Paged<User> getPage(int pageNumber, int size);

    Paged<User> getPageFiltered(String pattern, int pageNumber, int size);
}