package com.osayijoy.common_utils_library.recibo.model;

import lombok.*;




@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReciboBody {

    private String senderName;
    private String beneficiaryName;

    private String currency = "NGN";

    private String transactionDate;

    private String senderAccountNumber;

    private String beneficiaryAccountNumber;
    private String beneficiaryBankName;
    private String senderBankName;

    private String transactionReference;

    private String transactionAmount;

    private String narration;

    private String paymentMethod;

    private String paymentChannel;

    private String headMessage;

    private String footMessage;

    private String receiptLogo;

    private String merchantEmail;
}
