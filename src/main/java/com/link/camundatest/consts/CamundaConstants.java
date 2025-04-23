package com.link.camundatest.consts;

import java.math.BigDecimal;

public interface CamundaConstants {

    /**
     * checkCard流程开始时保存的金额的字段名，设置在 variables
     */
    String CHECK_CARD_VARIABLE_KEY = "amount";

    /**
     * camunda指定需要检查的金额字段，设置在 extension properties
     */
    String CHECK_AMOUNT = "checkAmount";

    String AMOUNT_WHETHER_ENOUGH = "amountWhetherEnough";

}
