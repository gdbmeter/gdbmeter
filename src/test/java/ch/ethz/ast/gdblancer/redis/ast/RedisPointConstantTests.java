package ch.ethz.ast.gdblancer.redis.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedisPointConstantTests {

    @Test
    void testPointConstants() {
        assertEquals(
                new RedisPointConstant(1D, 2D).getTextRepresentation(),
                "point({ longitude: 1.000000, latitude: 2.000000 })");
        assertEquals(
                new RedisPointConstant(-99D, -98D).getTextRepresentation(),
                "point({ longitude: -99.000000, latitude: -98.000000 })");
    }

}
