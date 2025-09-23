package com.libraryManagement.project.constants;


import com.libraryManagement.project.exception.InsufficientFundsException;
import com.libraryManagement.project.exception.InvalidPaymentTypeException;
import com.libraryManagement.project.exception.InvalidPaymentTypePinException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// --- Model Classes (No changes needed here) ---


record CreditCard(String cardNumber, int pin, float balance) {
}


record DebitCard(String cardNumber, int pin, float balance) {
}

record Upi(String upiId, int pin, float balance) {
}


// --- Constants Class with Hard-coded Data ---

/**
 * A final utility class holding hard-coded payment details.
 * This class cannot be instantiated or extended.
 */
public final class PaymentConstants {

    // Lists to hold the hard-coded payment details
    private static final List<CreditCard> CREDIT_CARDS = new ArrayList<>();
    private static final List<DebitCard> DEBIT_CARDS = new ArrayList<>();
    private static final List<Upi> UPI_ACCOUNTS = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(PaymentConstants.class);

    private PaymentConstants() {}

    static {
        // Credit Cards
        CREDIT_CARDS.add(new CreditCard("4111-2222-3333-4444", 1234, 25000.0f));
        CREDIT_CARDS.add(new CreditCard("4555-6666-7777-8888", 5678, 150000.0f));
        CREDIT_CARDS.add(new CreditCard("4999-8888-7777-6666", 9012, 7500.50f));

        //  Debit Cards
        DEBIT_CARDS.add(new DebitCard("5011-2233-4455-6677", 9876, 12345.67f));
        DEBIT_CARDS.add(new DebitCard("5088-7766-5544-3322", 5432, 8500.00f));
        DEBIT_CARDS.add(new DebitCard("5099-1111-0000-2222", 1122, 500.25f));

        // UPI Accounts
        UPI_ACCOUNTS.add(new Upi("john.doe@okbank", 1111, 987.0f));
        UPI_ACCOUNTS.add(new Upi("jane.smith@ybl", 2222, 10500.75f));
        UPI_ACCOUNTS.add(new Upi("testuser@payapp", 9999, 1200.0f));
    }


    //Validate Payment
    public static boolean validatePayment(String type, String identifier, int pin, double amount) {
        logger.info("--- \nAttempting validation for {} payment of ${}...", type.toUpperCase(Locale.getDefault()), amount);

        boolean flag = false;
        switch (type.toLowerCase()) {
            case "credit":
                for (CreditCard card : CREDIT_CARDS) {
                    if (card.cardNumber().equals(identifier)) {
                        if (card.pin() == pin) {
                            if (card.balance() >= amount) {
                                logger.info("Credit Card payment validated.");
                                flag = true;
                            } else {
                                throw new InsufficientFundsException("Insufficient funds in credit card: " + identifier);
                            }
                        } else {
                            throw new InvalidPaymentTypePinException("Invalid PIN of credit card: " + identifier);
                        }
                    }
                }
                break;

            case "debit":
                for (DebitCard card : DEBIT_CARDS) {
                    if (card.cardNumber().equals(identifier)) {
                        if (card.pin() == pin) {
                            if (card.balance() >= amount) {
                                logger.info("Debit Card payment validated.");
                                flag = true;
                            } else {
                                throw new InsufficientFundsException("Insufficient funds in debit card: " + identifier);
                            }
                        } else {
                            throw new InvalidPaymentTypePinException("Invalid PIN of debit card: " + identifier);
                        }
                    }
                }
                break;

            case "upi":
                for (Upi account : UPI_ACCOUNTS) {
                    if (account.upiId().equals(identifier)) {
                        if (account.pin() == pin) {
                            if (account.balance() >= amount) {
                                logger.info("UPI payment validated.");
                                flag = true;
                            } else {
                                throw new InsufficientFundsException("Insufficient funds in UPI Id: " + identifier);
                            }
                        } else {
                            throw new InvalidPaymentTypePinException("Invalid PIN of debit card: " + identifier);
                        }
                    }
                }
                break;

            default:
                throw new InvalidPaymentTypeException("Invalid payment type specified.");
        }

        logger.error("Payment identifier not found.");
        return flag;
    }
}
