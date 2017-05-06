import exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;


public class Log4j2Test {

    private static final Logger logger = LogManager.getLogger(Log4j2Test.class);

    @Test
    public void loggingException() throws Exception {
        logger.error("hoge error", new RuntimeException("hoge"));
    }

    @Test
    public void loggingCustomException() throws Exception {
        logger.error("hoge error", new CustomException("hogecode", "hoge"));
    }
    @Test
    public void loggingNestCustomException() throws Exception {
        CustomException ex4 = new CustomException("code4", "msg4");
        RuntimeException ex3 = new RuntimeException("msg3", ex4);
        CustomException ex2 = new CustomException("code2", "msg2", ex3);
        RuntimeException ex1 = new RuntimeException("msg1", ex2);
        logger.error("hoge error", ex1);
    }
}
