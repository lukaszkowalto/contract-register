package controlling.contractRegister.service;

import controlling.contractRegister.model.VerificationToken;
import controlling.contractRegister.web.UserDTO;

import java.util.UUID;

public interface VerificationTokenService {

    void save(UserDTO user) throws Exception;

    VerificationToken getVerificationTokenByToken(UUID token);

    void sendVerificationEmail(UserDTO user, VerificationToken token) throws Exception;
}