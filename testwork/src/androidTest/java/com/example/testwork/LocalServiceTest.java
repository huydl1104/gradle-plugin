package com.example.testwork;

import android.content.Intent;
import android.os.IBinder;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ServiceTestRule;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

/**
 * @author yudongliang
 * create time 2021-08-31
 * describe : 本地服务测试
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class LocalServiceTest {

    @Rule
    public final ServiceTestRule mRules = new ServiceTestRule();

    @Test
    public void testWithBindService() throws TimeoutException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),LocalService.class);
        intent.putExtra(LocalService.SEED_KEY, 42L);
        IBinder iBinder = mRules.bindService(intent);
        LocalService service = ((LocalService.LocalBinder) iBinder).getService();
        System.out.println("first  ->"+(service.getRandomInt())+" , two ->"+(CoreMatchers.is(CoreMatchers.any(Integer.class))));
        Assert.assertThat(service.getRandomInt(), CoreMatchers.is(CoreMatchers.any(Integer.class)));
    }
}
