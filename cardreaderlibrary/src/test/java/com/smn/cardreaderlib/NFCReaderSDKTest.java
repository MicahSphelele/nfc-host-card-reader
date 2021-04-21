package com.smn.cardreaderlib;

import android.content.Context;

import com.smn.cardreaderlib.logger.NFCReaderSDKLogger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class NFCReaderSDKTest {

    private NFCReaderSDK sdk;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        Field instanceField = NFCReaderSDK.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        sdk = new NFCReaderSDK(
                Mockito.mock(Context.class),
                Mockito.mock(NFCReaderSDKLogger.class));
    }

    @Test
    public void testContextNotNull() {

        Assert.assertNotNull(sdk.getContext());
    }

    @Test
    public void testLoggerNotNull() {

        Assert.assertNotNull(sdk.getContext());
    }

}