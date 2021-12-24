import java.util.ArrayList;
import java.util.List;

import org.jetbrains.kotlinx.lincheck.LinChecker;
import org.jetbrains.kotlinx.lincheck.LoggingLevel;
import org.jetbrains.kotlinx.lincheck.Options;
import org.jetbrains.kotlinx.lincheck.annotations.Operation;
import org.jetbrains.kotlinx.lincheck.annotations.Param;
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen;
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingCTestConfiguration;
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions;
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressCTest;
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressCTestConfiguration;
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions;
import org.junit.Test;

@Param(name = "key", gen = IntGen.class, conf = "1:3")
@StressCTest()
public class SetTest {
    private final Set<Integer> set = new SetImpl<>();

    @Operation
    public boolean add(@Param(name = "key") Integer key) {
        return set.add(key);
    }

    @Operation
    public boolean remove(@Param(name = "key") Integer key) {
        return set.remove(key);
    }

    @Operation
    public boolean contains(@Param(name = "key") Integer key) {
        return set.contains(key);
    }

    @Operation
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Operation
    public List<Integer> iterator() {
        List<Integer> ans = new ArrayList<>();
        set.iterator().forEachRemaining(ans::add);
        return ans;
    }

    @Test
    public void runStressTest() {
        Options<StressOptions, StressCTestConfiguration> opts = new StressOptions()
                .threads(2)
                .actorsBefore(3)
                .actorsPerThread(5)
                .actorsAfter(3)
                .iterations(10)
                .logLevel(LoggingLevel.INFO);
        LinChecker.check(SetTest.class, opts);
    }

    @Test
    public void runModuleCheckerTest() {
        Options<ModelCheckingOptions, ModelCheckingCTestConfiguration> opts = new ModelCheckingOptions()
                .minimizeFailedScenario(true)
                .requireStateEquivalenceImplCheck(false)
                .threads(2)
                .actorsBefore(3)
                .actorsPerThread(5)
                .actorsAfter(3)
                .iterations(10)
                .logLevel(LoggingLevel.INFO);
        LinChecker.check(SetTest.class, opts);
    }
}