package space.itzkana.cglib;

import java.util.List;
import java.util.stream.IntStream;

public class NoInherit {

    public List<Integer> randInt() {
        return IntStream.range(0, 5).map(idx -> (int) (Math.random() * 10 * (idx + 1))).boxed().toList();
    }

    public List<Integer> skipUseFilter() {
        return IntStream.range(0, 5).map(idx -> (int) (Math.random() * 10 * (idx + 1))).boxed().toList();
    }

    public final List<Integer> skipFinal() {
        return IntStream.range(0, 5).map(idx -> (int) (Math.random() * 10 * (idx + 1))).boxed().toList();
    }
}
