package controlling.contractRegister;

import controlling.contractRegister.web.ContractController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ContractRegisterApplicationTests {

    @Autowired
    private ContractController contractController;

    @Test
    void contextLoads() {
        assertThat(contractController).isNotNull();
    }
}
