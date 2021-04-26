package com.smn.emvcore.reader;

import com.smn.emvcore.interfaces.ResultsListener;
import com.smn.emvcore.mocks.MockEmvLogger;
import com.smn.emvcore.mocks.MockTransceiver;
import com.smn.emvcore.model.EmvResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(MockitoJUnitRunner.class)
public class EmvReaderTest {


    @Mock
    EmvReader mockEmvReader;

    @Mock
    MockTransceiver mockTransceiver;

    @Mock
    MockEmvLogger mockEmvLogger;

    @Before
    public void setUp() {

        mockTransceiver = new MockTransceiver();

    }

    @Test(expected = AssertionError.class)
    public void testErrorMessageNotNull() {
        final String [] messages = {null};
        final CountDownLatch lock = new CountDownLatch(1);

        mockEmvReader = new EmvReader(mockTransceiver, mockEmvLogger, new ResultsListener() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void onSuccess(EmvResponse emvResponse) {
                lock.countDown();
            }

            @Override
            public void onError(String message) {
                messages[0] = message;
                lock.countDown();
            }
        });
        new Thread(mockEmvReader).start();
        Assert.assertNotNull(messages[0]);
    }

    @Test(expected = AssertionError.class)
    public void testErrorMessageCannotReadCard() {
        final String [] messages = {null};
        final CountDownLatch lock = new CountDownLatch(1);

        mockEmvReader = new EmvReader(mockTransceiver, mockEmvLogger, new ResultsListener() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void onSuccess(EmvResponse emvResponse) {
                lock.countDown();
            }

            @Override
            public void onError(String message) {
                messages[0] = message;
                lock.countDown();
            }
        });
        new Thread(mockEmvReader).start();
        Assert.assertEquals("Cannot read card",messages[0]);
    }
}