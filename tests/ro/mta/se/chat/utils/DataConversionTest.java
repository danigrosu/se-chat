package ro.mta.se.chat.utils;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Dani on 2/2/2016.
 */
public class DataConversionTest {

    @Test
    public void testByteArrayToBase64() throws Exception {
        byte[] test = ("DanielGrosu").getBytes();
        Assert.assertEquals("RGFuaWVsR3Jvc3U=", DataConversion.byteArrayToBase64(test));
    }

    @Test
    public void testBase64ToByteArray() throws Exception {
        Assert.assertArrayEquals("DanielGrosu".getBytes(), DataConversion.base64ToByteArray("RGFuaWVsR3Jvc3U="));
    }
}