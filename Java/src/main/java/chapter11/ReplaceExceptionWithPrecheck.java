package chapter11;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
            Resource result = available.isEmpty() ? Resource.create() : available.pop();
            allocated.add(result);
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
