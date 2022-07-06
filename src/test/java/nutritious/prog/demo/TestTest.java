package nutritious.prog.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TestTest {
    Calculator calculator = new Calculator();
    @Test
    void test(){
        //given (dane wejściowe)
        int n1 = 10;
        int n2 = 20;

        //when (dane wyjściowe)
        int res = calculator.add(n1,n2);

        //then (sprawdzenie pokrycia się)
        int expected = 30;
        assertThat(res).isEqualTo(expected);
    }

    class Calculator{
        public int add (int a, int b) {
            return a + b;
        }
    }
}
