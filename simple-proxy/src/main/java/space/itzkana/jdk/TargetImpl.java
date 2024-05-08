package space.itzkana.jdk;

import java.util.List;
import java.util.stream.IntStream;

public class TargetImpl implements AnInterface {
    @Override
    public List<Integer> randInt(int count) {
        return IntStream.range(0, count).map(idx -> (int) (Math.random() * 10 * (idx + 1))).boxed().toList();
    }
}
