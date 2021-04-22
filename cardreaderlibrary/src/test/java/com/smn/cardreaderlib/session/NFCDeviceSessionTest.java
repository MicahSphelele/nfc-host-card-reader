package com.smn.cardreaderlib.session;

import android.app.Activity;

import com.smn.cardreaderlib.enums.NfcState;
import com.smn.cardreaderlib.interfaces.EmvResultsListener;
import com.smn.cardreaderlib.models.EmvResults;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class NFCDeviceSessionTest {

    @Mock
    NFCDeviceSession mockNfcDeviceSession;

    @Mock
    Activity mockActivity;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        EmvResults emvResults = new EmvResults("A0000000043060","6799998900000060158F");

        doAnswer(x -> {
            EmvResultsListener listener = x.getArgument(1,EmvResultsListener.class);
            listener.onNfcState(NfcState.NFC_ENABLED);
            listener.onEmvResults(emvResults);
            return null;
        }).when(this.mockNfcDeviceSession)
                .startCardReader(any(), any());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testStartCardReaderNfcStateIsNull() throws InterruptedException {
        doAnswer(x -> {
            EmvResultsListener listener = x.getArgument(1,EmvResultsListener.class);
            listener.onNfcState(null);
            return null;
        }).when(this.mockNfcDeviceSession)
                .startCardReader(any(), any());
        CountDownLatch lock = new CountDownLatch(1);
        final NfcState [] nfcStates = {null};

        this.mockNfcDeviceSession
                .startCardReader
                        (this.mockActivity,
                                new EmvResultsListener() {
                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onEmvResults(EmvResults emvResults) {
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onNfcState(NfcState nfcState) {
                                        nfcStates[0] = nfcState;
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onError(String message) {
                                        lock.countDown();
                                    }
                                });
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertNull(nfcStates[0]);
    }

    @Test
    public void testStartCardReaderNfcStateNotNull() throws InterruptedException {

        CountDownLatch lock = new CountDownLatch(1);
        final NfcState [] nfcStates = {null};

        this.mockNfcDeviceSession
                .startCardReader
                        (this.mockActivity,
                                new EmvResultsListener() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void onEmvResults(EmvResults emvResults) {
               lock.countDown();
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void onNfcState(NfcState nfcState) {
                nfcStates[0] = nfcState;
                lock.countDown();
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void onError(String message) {
                lock.countDown();
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertNotNull(nfcStates[0]);
    }

    @Test
    public void testStartCardReaderNfcStateEnabled() throws InterruptedException {

        CountDownLatch lock = new CountDownLatch(1);
        final NfcState [] nfcStates = {null};
        this.mockNfcDeviceSession
                .startCardReader
                        (this.mockActivity,
                                new EmvResultsListener() {
                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onEmvResults(EmvResults emvResults) {
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onNfcState(NfcState nfcState) {
                                        nfcStates[0] = nfcState;
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onError(String message) {
                                        lock.countDown();
                                    }
                                });
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(NfcState.NFC_ENABLED,nfcStates[0]);
    }

    @Test
    public void testStartCardReaderNfcStateNotEnabled() throws InterruptedException {

        doAnswer(x -> {
            EmvResultsListener listener = x.getArgument(1,EmvResultsListener.class);
            listener.onNfcState(NfcState.NFC_NOT_ENABLED);
            return null;
        }).when(this.mockNfcDeviceSession)
                .startCardReader(any(), any());

        CountDownLatch lock = new CountDownLatch(1);
        final NfcState [] nfcStates = {null};
        this.mockNfcDeviceSession
                .startCardReader
                        (this.mockActivity,
                                new EmvResultsListener() {
                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onEmvResults(EmvResults emvResults) {
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onNfcState(NfcState nfcState) {
                                        nfcStates[0] = nfcState;
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onError(String message) {
                                        lock.countDown();
                                    }
                                });
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(NfcState.NFC_NOT_ENABLED,nfcStates[0]);
    }

    @Test
    public void testStartCardReaderNfcStateNotSupported() throws InterruptedException {

        doAnswer(x -> {
            EmvResultsListener listener = x.getArgument(1,EmvResultsListener.class);
            listener.onNfcState(NfcState.NFC_NOT_SUPPORTED);
            return null;
        }).when(this.mockNfcDeviceSession)
                .startCardReader(any(), any());

        CountDownLatch lock = new CountDownLatch(1);
        final NfcState [] nfcStates = {null};
        this.mockNfcDeviceSession
                .startCardReader
                        (this.mockActivity,
                                new EmvResultsListener() {
                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onEmvResults(EmvResults emvResults) {
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onNfcState(NfcState nfcState) {
                                        nfcStates[0] = nfcState;
                                        lock.countDown();
                                    }

                                    @SuppressWarnings("NullableProblems")
                                    @Override
                                    public void onError(String message) {
                                        lock.countDown();
                                    }
                                });
        lock.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(NfcState.NFC_NOT_SUPPORTED,nfcStates[0]);
    }
}