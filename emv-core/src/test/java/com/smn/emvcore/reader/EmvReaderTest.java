package com.smn.emvcore.reader;

import com.smn.emvcore.mocks.MockEmvLogger;
import com.smn.emvcore.mocks.MockTransceiver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmvReaderTest {


    @Mock
    EmvReader mockEmvReader;


    @Before
    public void setUp() {
        
    }

    @Test(expected = AssertionError.class)
    public void testErrorMessageNotNull() {
//        final String [] messages = {null};
//        final CountDownLatch lock = new CountDownLatch(1);
//
//        mockEmvReader = new EmvReader(mockTransceiver, mockEmvLogger, new ResultsListener() {
//            @SuppressWarnings("NullableProblems")
//            @Override
//            public void onSuccess(EmvResponse emvResponse) {
//                lock.countDown();
//            }
//
//            @Override
//            public void onError(String message) {
//                messages[0] = message;
//                lock.countDown();
//            }
//        });
//        new Thread(mockEmvReader).start();
//        Assert.assertNotNull(messages[0]);
    }

    @Test(expected = AssertionError.class)
    public void testErrorMessageCannotReadCard() {
//        final String [] messages = {null};
//        final CountDownLatch lock = new CountDownLatch(1);
//
//        mockEmvReader = new EmvReader(mockTransceiver, mockEmvLogger, new ResultsListener() {
//            @SuppressWarnings("NullableProblems")
//            @Override
//            public void onSuccess(EmvResponse emvResponse) {
//                lock.countDown();
//            }
//
//            @Override
//            public void onError(String message) {
//                messages[0] = message;
//                lock.countDown();
//            }
//        });
//        new Thread(mockEmvReader).start();
//        Assert.assertEquals("Cannot read card",messages[0]);
    }

    @Test(expected = AssertionError.class)
    public void testEmvResponseIsNotNull() {
//        final CountDownLatch lock = new CountDownLatch(1);
//        final EmvResponse [] emvResponses = {null};
//
//        mockEmvReader = new EmvReader(mockTransceiver, mockEmvLogger, new ResultsListener() {
//            @SuppressWarnings("NullableProblems")
//            @Override
//            public void onSuccess(EmvResponse emvResponse) {
//                emvResponses[0] = emvResponse;
//                lock.countDown();
//            }
//
//            @Override
//            public void onError(String message) {
//                lock.countDown();
//            }
//        });
//        new Thread(mockEmvReader).start();
//        Assert.assertNotNull("EMV response not null",emvResponses[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testEmvResponseIsAidIsNull() {
//        final CountDownLatch lock = new CountDownLatch(1);
//        final EmvResponse [] emvResponses = {null};
//
//        mockEmvReader = new EmvReader(mockTransceiver, mockEmvLogger, new ResultsListener() {
//            @SuppressWarnings("NullableProblems")
//            @Override
//            public void onSuccess(EmvResponse emvResponse) {
//                emvResponses[0] = emvResponse;
//                lock.countDown();
//            }
//
//            @Override
//            public void onError(String message) {
//                lock.countDown();
//            }
//        });
//        new Thread(mockEmvReader).start();
//        Assert.assertNull("EMV response aid is null",emvResponses[0].getAid());
    }

}