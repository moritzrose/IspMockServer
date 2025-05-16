package de.fi.IspMockServer;

import de.fi.IspMockServer.service.SoftphoneService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SoftphoneServiceTests {


    @Test
    public void testToBinary() {
        int[] arr = SoftphoneService.toBinary(255, 8);
        Assertions.assertEquals(arr[0], 1);
        Assertions.assertEquals(arr[1], 1);
        Assertions.assertEquals(arr[2], 1);
        Assertions.assertEquals(arr[3], 1);
        Assertions.assertEquals(arr[4], 1);
        Assertions.assertEquals(arr[5], 1);
        Assertions.assertEquals(arr[6], 1);
        Assertions.assertEquals(arr[7], 1);

        int[] arr1 = SoftphoneService.toBinary(163, 8);
        Assertions.assertEquals(arr1[0], 1);
        Assertions.assertEquals(arr1[1], 1);
        Assertions.assertEquals(arr1[2], 0);
        Assertions.assertEquals(arr1[3], 0);
        Assertions.assertEquals(arr1[4], 0);
        Assertions.assertEquals(arr1[5], 1);
        Assertions.assertEquals(arr1[6], 0);
        Assertions.assertEquals(arr1[7], 1);
    }
}
