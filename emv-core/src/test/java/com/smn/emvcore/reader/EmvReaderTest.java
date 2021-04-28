package com.smn.emvcore.reader;

import com.smn.emvcore.Constants;
import com.smn.emvcore.interfaces.ResultsListener;
import com.smn.emvcore.model.EmvResponse;
import com.smn.emvcore.utils.HexUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class EmvReaderTest {

    @Mock
    EmvReader mockEmvReader;

    @Mock
    ExecutorService mockExecutorService;

    @Before
    public void setUp() {

        List<byte[]> commAppFileList = Arrays.asList(
                HexUtil.hexadecimalToBytes("00B2010C00"),
                HexUtil.hexadecimalToBytes("00B2011400"),
                HexUtil.hexadecimalToBytes("00B2011C00"),
                HexUtil.hexadecimalToBytes("00B2021C00"),
                HexUtil.hexadecimalToBytes("00B2012400"),
                HexUtil.hexadecimalToBytes("00B2022400"));

        List<byte[]> respAppFileList = Arrays.asList(Constants.bytes1, Constants.bytes2,
                Constants.bytes3,Constants.bytes4,Constants.bytes5,
                Constants.bytes6);

        EmvResponse emvResponse = new EmvResponse();
        emvResponse.setAid("A0000000043060");
        emvResponse.setCardNumber("6799998900000060158F");
        emvResponse.setCardHolderName("/");
        emvResponse.setCardExpirationDate("491231");
        emvResponse.setCommAppFileList(commAppFileList);
        emvResponse.setRespAppFileList(respAppFileList);

        doAnswer(invocation -> {
            ResultsListener listener = invocation.getArgument(1);
            listener.onSuccess(emvResponse);
            listener.onError("Cannot read card");
            return null;
        }).when(this.mockEmvReader)
                .startEmvReader(any(), any());
    }

    @Test
    public void testErrorMessageNotNull() throws InterruptedException {
        final String[] messages = {null};
        final CountDownLatch lock = new CountDownLatch(1);
        mockEmvReader.startEmvReader(this.mockExecutorService, new ResultsListener() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void onSuccess(EmvResponse emvResponse) {
                lock.countDown();
            }

            @Override
            public void onError(String message) {
                messages[0] = message;
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertNotNull(messages[0]);
    }

    @Test
    public void testErrorMessageCannotReadCard() throws InterruptedException {
        final String[] messages = {null};
        final CountDownLatch lock = new CountDownLatch(1);
        mockEmvReader.startEmvReader(this.mockExecutorService, new ResultsListener() {
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
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertEquals("Cannot read card", messages[0]);
    }

    @Test
    public void testEmvResponseIsNotNull() {
        final CountDownLatch lock = new CountDownLatch(1);
        final EmvResponse[] emvResponses = {null};
        mockEmvReader.startEmvReader(this.mockExecutorService, new ResultsListener() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void onSuccess(EmvResponse emvResponse) {
                emvResponses[0] = emvResponse;
                lock.countDown();
            }

            @Override
            public void onError(String message) {
                lock.countDown();
            }
        });
        Assert.assertNotNull("EMV response not null", emvResponses[0]);
    }

    @Test
    public void testEmvResponseIsAidIsNotNull() {
        final CountDownLatch lock = new CountDownLatch(1);
        final EmvResponse[] emvResponses = {null};
        mockEmvReader.startEmvReader(this.mockExecutorService, new ResultsListener() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void onSuccess(EmvResponse emvResponse) {
                emvResponses[0] = emvResponse;
                lock.countDown();
            }

            @Override
            public void onError(String message) {
                lock.countDown();
            }
        });
        Assert.assertNotNull("EMV response not null", emvResponses[0].getAid());
    }

}