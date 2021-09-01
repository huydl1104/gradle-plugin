
package com.example.testwork;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.example.testwork.calculator.Calculator;
import com.example.testwork.calculator.CalculatorActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * JUnit4 unit tests for the calculator logic.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class CalculatorTest {

    private Calculator mCalculator;

    @Before
    public void setUp() {
        mCalculator = new Calculator();
    }

    @Test
    public void addTwoNumbers() {
        double resultAdd = mCalculator.add(1d, 1d);
        System.out.println("first  ->"+(resultAdd));
        assertThat(resultAdd, is(equalTo(2d)));
    }

    @Test
    public void subTwoNumbers() {
        double resultSub = mCalculator.sub(1d, 1d);
        assertThat(resultSub, is(equalTo(0d)));
    }

    @Test
    public void subWorksWithNegativeResult() {
        double resultSub = mCalculator.sub(1d, 17d);
        assertThat(resultSub, is(equalTo(-16d)));
    }

    @Test
    public void divTwoNumbers() {
        double resultDiv = mCalculator.div(32d,2d);
        assertThat(resultDiv, is(equalTo(16d)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void divDivideByZeroThrows() {
        mCalculator.div(32d,0d);
    }

    @Test
    public void mulTwoNumbers() {
        double resultMul = mCalculator.mul(32d, 2d);
        assertThat(resultMul, is(equalTo(64d)));
    }

    @Before
    public void launcherActivity(){
        System.out.println("CalculatorTest  launcherActivity ");
        ActivityScenario.launch(CalculatorActivity.class);
    }

    @Test
    public void typeOperandsAndPerformAddOperation() {
        performOperation(R.id.operation_add_btn, "16.0", "16.0", "32.0");
    }

    @Test
    public void typeOperandsAndPerformSubOperation() {
        performOperation(R.id.operation_sub_btn, "32.0", "16.0", "16.0");
    }

    @Test
    public void typeOperandsAndPerformDivOperation() {
        performOperation(R.id.operation_div_btn, "128.0", "16.0", "8.0");
    }

    @Test
    public void divZeroForOperandTwoShowsError() {
        final String expectedResult = ApplicationProvider.getApplicationContext().getString(R.string.computationError);
        performOperation(R.id.operation_div_btn, "128.0", "0.0", expectedResult);
    }

    @Test
    public void typeOperandsAndPerformMulOperation() {
        performOperation(R.id.operation_mul_btn, "16.0", "16.0", "256.0");
    }

    private void performOperation(int btnOperationResId, String operandOne,
                                  String operandTwo, String expectedResult) {
        Espresso.onView(ViewMatchers.withId(R.id.operand_one_edit_text)).perform(ViewActions.typeText(operandOne),
                ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.operand_two_edit_text)).perform(ViewActions.typeText(operandTwo),
                ViewActions.closeSoftKeyboard());

        // Click on a given operation button
        Espresso.onView(ViewMatchers.withId(btnOperationResId)).perform(ViewActions.click());
        // Check the expected test is displayed in the Ui
        Espresso.onView(ViewMatchers.withId(R.id.operation_result_text_view)).check(ViewAssertions.matches(ViewMatchers.withText(expectedResult)));
    }

    @After
    public void destroyActivity(){
        System.out.println("CalculatorTest  destroyActivity ");
    }

}