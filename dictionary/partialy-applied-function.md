```java
import java.util.function.BiFunction;
import java.util.function.Function;

public class PartialApplicationExample {

    // 두 정수를 곱하는 함수
    public static int multiply(int a, int b) {
        return a * b;
    }

    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> multiplyFunc = PartialApplicationExample::multiply;

        // 첫 번째 인자(예: 2)를 고정해서 부분 적용 함수를 생성
        Function<Integer, Integer> multiplyBy2 = b -> multiplyFunc.apply(2, b);

        // multiplyBy2는 이제 인자 하나만 받아 2를 곱한 결과를 반환
        System.out.println("2 x 5 = " + multiplyBy2.apply(5)); // 출력: 2 x 5 = 10
    }
}
```
