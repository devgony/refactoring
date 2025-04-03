package chapter11;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

class ReplaceExceptionWithPrecheck {
    static class Resource {
        public static Resource create() {
            return new Resource();
        }
    }

    static class ResourcePool {
        ResourcePool(int availableSize) {
            available = new ArrayDeque<Resource>(availableSize);
            allocated = new ArrayList<Resource>();

            // Create resources based on availableSize
            for (int i = 0; i < availableSize; i++) {
                available.add(Resource.create());
            }
        }

        public Resource get() {
            Resource result;
            if (available.isEmpty()) {
                result = Resource.create();
                allocated.add(result);
            } else {
                try {
                    result = available.pop();
                    allocated.add(result);
                } catch (NoSuchElementException e) {
                    throw new AssertionError("unreachable");
                }
            }
            return result;
        }

        private Deque<Resource> available;
        private List<Resource> allocated;

        public int availableSize() {
            return available.size();
        }

        public int allocatedSize() {
            return allocated.size();
        }
    }
}
