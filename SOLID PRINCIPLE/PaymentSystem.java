/* 
 Here, Interface is Payable and which is kind base to every class
 which implements it i.e whatever class implements it, it has 
 the functionality in that calss. Here we can add any other payment method
 without touching the PaymentProcess.
 */

// Interface for Payment Methods  as every payment method does paying
interface Pay {
    void pay(double amount);
}

//Payment Method
class CreditCardPayment implements Pay {
    @Override
    public void pay(double amount) {
        System.out.println("Customer paid the amount of Rs" + amount + " using Credit Card.");
    }
}

class PaytmPayment implements Pay {
    @Override
    public void pay(double amount) {
        System.out.println("Custmer paid the amount of Rs" + amount + " using Paytm.");
    }
}

class GooglePayPayment implements Pay {
    @Override
    public void pay(double amount) {
        System.out.println("Custmer paid the amount of Rs" + amount + " using Google Pay.");
    }
}
class PhonePayPayment implements Pay {
    @Override
    public void pay(double amount) {
        System.out.println("Custmer paid the amount of Rs" + amount + " using Phone Pay.");
    }
}

//No need to touch here ..
class PaymentMode {
    private Pay ModeOfPayment;

    public PaymentMode(Pay ModeOfPayment) {
        this.ModeOfPayment =ModeOfPayment;  
    }

    public void modePayment(double amount) {
        ModeOfPayment.pay(amount);
    }
}

//Main Class 
public class PaymentSystem {
    public static void main(String[] args) {
        Pay creditCard = new CreditCardPayment();
        Pay paytm = new PaytmPayment();
        Pay googlePay = new GooglePayPayment();
        Pay phonePay = new PhonePayPayment();


        PaymentMode mode1 = new  PaymentMode(creditCard);
        mode1.modePayment(100.50);

        PaymentMode mode2 = new  PaymentMode(paytm);
        mode2.modePayment(200.75);

        PaymentMode mode3 = new  PaymentMode(googlePay);
        mode3.modePayment(50.25);

        PaymentMode mode4 = new  PaymentMode(phonePay);
        mode4.modePayment(150.25);
    }
}
