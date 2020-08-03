import java.math.BigDecimal;

public class JavaGrammar {

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(0E-8);
        System.out.println(bigDecimal);
        double bigDecimal2 = 0E-8;
        System.out.println(bigDecimal2);
        BigDecimal bigDecimal1 = BigDecimal.valueOf(0E-8);
        System.out.println(bigDecimal1);

    }
}
