package chapter12;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import chapter12.ReplaceSuperclassWithDelegate.Scroll;

class ReplaceSuperclassWithDelegateTest {
    @Test
    void client1() {
        List<Map<String, Object>> aDocument = Arrays.asList(
                new HashMap<String, Object>() {
                    {
                        put("id", "1");
                        put("catalogData", new HashMap<String, Object>() {
                            {
                                put("title", "Scroll 1");
                                put("tags", Arrays.asList("tag1", "tag2"));
                            }
                        });
                        put("lastCleaned", "2023-01-01");
                    }
                },
                new HashMap<String, Object>() {
                    {
                        put("id", "2");
                        put("catalogData", new HashMap<String, Object>() {
                            {
                                put("title", "Scroll 2");
                                put("tags", Arrays.asList("tag3", "revered"));
                            }
                        });
                        put("lastCleaned", "2023-02-01");
                    }
                });

        List<Scroll> scrolls = aDocument
                .stream()
                .map(record -> new Scroll(record.get("id").toString(),
                        ((Map<String, String>) record.get("catalogData")).get("title").toString(),
                        (List<String>) ((Map<String, Object>) record.get("catalogData")).get("tags"),
                        LocalDate.parse(record.get("lastCleaned").toString())))
                .collect(Collectors.toList());

        assertThat(scrolls).hasSize(2);
        assertThat(scrolls.get(0).id()).isEqualTo("1");
        assertThat(scrolls.get(0).title()).isEqualTo("Scroll 1");
        assertThat(scrolls.get(0).tags()).containsExactly("tag1", "tag2");
        assertThat(scrolls.get(0).daysSinceLastCleaning(LocalDate.now())).isEqualTo(830L);
        assertThat(scrolls.get(0).needsCleaning(LocalDate.now())).isFalse();

        assertThat(scrolls.get(1).id()).isEqualTo("2");
        assertThat(scrolls.get(1).title()).isEqualTo("Scroll 2");
        assertThat(scrolls.get(1).tags()).containsExactly("tag3", "revered");
        assertThat(scrolls.get(1).daysSinceLastCleaning(LocalDate.now())).isEqualTo(799L);
        assertThat(scrolls.get(1).needsCleaning(LocalDate.now())).isTrue();
    }
}
