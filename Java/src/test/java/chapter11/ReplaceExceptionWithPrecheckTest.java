package chapter11;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import chapter11.ReplaceExceptionWithPrecheck.ResourcePool;

class ReplaceExceptionWithPrecheckTest {
    @Test
    void client1() {
        ResourcePool resourcePool = new ResourcePool(2);
        resourcePool.get();
        assertThat(resourcePool.availableSize()).isEqualTo(1);
        assertThat(resourcePool.allocatedSize()).isEqualTo(1);

        resourcePool.get();
        assertThat(resourcePool.availableSize()).isEqualTo(0);
        assertThat(resourcePool.allocatedSize()).isEqualTo(2);

        resourcePool.get();
        assertThat(resourcePool.availableSize()).isEqualTo(0);
        assertThat(resourcePool.allocatedSize()).isEqualTo(3);
    }
}
