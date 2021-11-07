import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class LombokTest {

    @Data
    @AllArgsConstructor
    public static class LombokPojo {
        private String message;
    }

    @Test
    public void test01LombokPojo() {
        LombokPojo lombokPojo = new LombokPojo("world");
        Assertions.assertEquals("world", lombokPojo.getMessage());
        lombokPojo.setMessage("world!");
        Assertions.assertEquals("world!", lombokPojo.getMessage());
    }

}
