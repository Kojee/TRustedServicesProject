package models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTypeTest {

    @Test
    void testEquals() {
        ServiceType s1 = new ServiceType("ABC");
        ServiceType s2 = new ServiceType("ABC");
        assertTrue(s1.equals(s2));

        List<ServiceType> types = new ArrayList<>();
        types.add(s1);
        types.add(s2);

        List<ServiceType> dList = types.stream().distinct().collect(Collectors.toList());
        assertTrue(dList.size() == 1);
    }
}