/* 
 Here, Interface is Payable and which is kind base to every class
 which implements it i.e whatever class implements it, it has 
 the functionality in that calss. Here we can add any other payment method
 without touching the PaymentProcess.
 */

// Interface for Payment Methods  as every payment method does paying
interface Payable {
    void pay(double amount);
}

//Payment Method
class CreditCardPayment implements Payable {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card.");
    }
}

class PayPalPayment implements Payable {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal.");
    }
}

class GooglePayPayment implements Payable {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Google Pay.");
    }
}
class PhonePayPayment implements Payable {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Phone Pay.");
    }
}

//No need to touch here ..
class PaymentProcess {
    private Payable ModeOfPayment;

    public PaymentProcess(Payable ModeOfPayment) {
        this.ModeOfPayment =ModeOfPayment;  
    }

    public void processPayment(double amount) {
        ModeOfPayment.pay(amount);
    }
}

//Main Class 
public class PaymentSystem {
    public static void main(String[] args) {
        Payable creditCard = new CreditCardPayment();
        Payable paypal = new PayPalPayment();
        Payable googlePay = new GooglePayPayment();
        Payable phonePay = new PhonePayPayment();


        PaymentProcess process1 = new PaymentProcess(creditCard);
        process1.processPayment(100.50);

        PaymentProcess process2 = new PaymentProcess(paypal);
        process2.processPayment(200.75);

        PaymentProcess process3 = new PaymentProcess(googlePay);
        process3.processPayment(50.25);

        PaymentProcess process4 = new PaymentProcess(phonePay);
        process4.processPayment(150.25);
    }
}
