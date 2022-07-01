package controlling.contractRegister.service;

import controlling.contractRegister.model.User;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.pagination.Paging;
import controlling.contractRegister.repository.UserRepository;
import controlling.contractRegister.web.UserDTO;
import controlling.contractRegister.web.UserRoleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(UserDTO user) {
        User userDB = new User();
        BeanUtils.copyProperties(user, userDB);
        userDB.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(userDB);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsUserById(Integer userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Paged<User> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<User> userPage = userRepository.findAll(request);
        return new Paged<>(userPage, Paging.of(userPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public Paged<User> getPageFiltered(String pattern, int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<User> userPage = userRepository.getAllUsersFiltered(pattern, request);
        return new Paged<>(userPage, Paging.of(userPage.getTotalPages(), pageNumber, size));
    }

    @Override
    @Transactional
    public void setRoleAndActivateAccount(UserRoleDTO userRole) {
        userRepository.updateRoleAndActivateAccount(userRole.getId(), userRole.getRole());
    }
}