package com.coffee.shop.service.impl;

import com.coffee.shop.constants.Constants;
import com.coffee.shop.dto.response.PaymentMonthlyChargeDto;
import com.coffee.shop.dto.response.PaymentRenewMonthlyChargeDto;
import com.coffee.shop.entity.Payment;
import com.coffee.shop.enums.PaymentState;
import com.coffee.shop.security.repository.PaymentRepository;
import com.coffee.shop.service.PaymentService;
import com.coffee.shop.service.SubscriptionService;
import com.coffee.shop.util.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, @Lazy SubscriptionService subscriptionService){
        this.paymentRepository = paymentRepository;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    @Override
    public PaymentMonthlyChargeDto monthlyCharge() {
        List<Payment> payments = getPaymentsCurrentMonth();
        if (payments.isEmpty()){
            return null;
        }
        return createPaymentMonthlyChargeDto(payments);
    }

    private List<Payment> getPaymentsCurrentMonth() {
        return this.paymentRepository.findAllByPaymentStateAndCreatedBetween(PaymentState.PENDING, getStartMonth(), getEndMonth());
    }

    private LocalDate getStartMonth() {
        return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000) ).withDayOfMonth(1);
    }

    private LocalDate getEndMonth() {
        return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000) ).plusMonths(1).withDayOfMonth(1).minusDays(1);
    }

    private PaymentMonthlyChargeDto createPaymentMonthlyChargeDto(List<Payment> payments) {
        PaymentMonthlyChargeDto paymentMonthlyCharge = new PaymentMonthlyChargeDto();
        for (Payment payment : payments) {
            getTotalAmount(paymentMonthlyCharge,payment);
            getPaymentInvoices(paymentMonthlyCharge,payment);
            getNumberOfCharges(paymentMonthlyCharge,payment);
            payment.setPaymentState(PaymentState.PAID);
            savePayment(payment);
        }
        paymentMonthlyCharge.setMonth(getStartMonth().getMonth().name());
        paymentMonthlyCharge.setYear(String.valueOf(getStartMonth().getYear()));
        return paymentMonthlyCharge;
    }

    private void getTotalAmount(PaymentMonthlyChargeDto paymentMonthlyCharge, Payment payment) {
        double total = paymentMonthlyCharge.getTotalPayment() != null ? paymentMonthlyCharge.getTotalPayment() : 0;
        total = total + payment.getAmountTotal();
        paymentMonthlyCharge.setTotalPayment(total);
    }

    private void getPaymentInvoices(PaymentMonthlyChargeDto paymentMonthlyCharge, Payment payment) {
        if (paymentMonthlyCharge.getPaymentInvoices() == null){
            paymentMonthlyCharge.setPaymentInvoices(new ArrayList<>());
        }
        paymentMonthlyCharge.getPaymentInvoices().add(payment.getPaymentInvoice());
    }

    private void getNumberOfCharges(PaymentMonthlyChargeDto paymentMonthlyCharge, Payment payment) {
        paymentMonthlyCharge.setNumberOfPayments((long) paymentMonthlyCharge.getPaymentInvoices().size());
    }

    @Override
    public void savePayment(Payment payment) {
        this.paymentRepository.save(payment);
    }

    @Override
    public PaymentRenewMonthlyChargeDto renewMonthlyCharge() {
        List<Payment> payments = this.paymentRepository.findAll();
        return renewCharge(payments);
    }

    private PaymentRenewMonthlyChargeDto renewCharge(List<Payment> payments) {
        PaymentRenewMonthlyChargeDto paymentRenewMonthlyCharge = new PaymentRenewMonthlyChargeDto();
        paymentRenewMonthlyCharge.setPaymentInvoices(new ArrayList<>());
        for (Payment payment : payments) {
            if (verifySubscriptionExist(payment.getSubscription().getId())){
                Payment renewPayment = createRenewPayment(payment);
                setValuesToPaymentRenewMonthlyCharge(paymentRenewMonthlyCharge, renewPayment);
            }
        }
        return paymentRenewMonthlyCharge;
    }

    private boolean verifySubscriptionExist(Long subscriptionId) {
        return this.subscriptionService.getSubscriptionById(subscriptionId) != null;
    }

    private Payment createRenewPayment(Payment payment) {
        Payment renewPayment = new Payment();
        renewPayment.setSubscription(payment.getSubscription());
        renewPayment.setPaymentInvoice(Constants.PAYMENT_PREFIX.concat(Text.getAlphaNumericString(Constants.RANDOM_PAYMENT_LENGTH)));
        renewPayment.setAmountTotal(payment.getSubscription().getPlan().getChargePerMonth());
        renewPayment.setPaymentState(PaymentState.PENDING);
        renewPayment.setCreated(payment.getCreated().plusMonths(1));
        savePayment(renewPayment);
        return renewPayment;
    }

    private void setValuesToPaymentRenewMonthlyCharge(PaymentRenewMonthlyChargeDto paymentRenewMonthlyCharge, Payment renewPayment) {
        paymentRenewMonthlyCharge.getPaymentInvoices().add(renewPayment.getPaymentInvoice());
        paymentRenewMonthlyCharge.setMonth(renewPayment.getCreated().getMonth().name());
        paymentRenewMonthlyCharge.setYear(String.valueOf(renewPayment.getCreated().getYear()));
    }
}
