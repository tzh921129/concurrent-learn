package net.jcip.examples;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * DynamicOrderDeadlock
 * <p/>
 * Dynamic lock-ordering deadlock
 *
 * @author Brian Goetz and Tim Peierls
 */
public class DynamicOrderDeadlock {
    // Warning: deadlock-prone!
    public static void transferMoney(Account fromAccount,
                                     Account toAccount,
                                     DollarAmount amount)
            throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException();
                } else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }

    static class DollarAmount implements Comparable<DollarAmount> {

        // Needs implementation
        private int amount;

        public DollarAmount(int amount) {
            this.amount = amount;
        }

        public DollarAmount add(DollarAmount d) {
            return new DollarAmount(d.amount + amount);
        }

        public DollarAmount subtract(DollarAmount d) {
            return new DollarAmount(amount - d.amount);
        }

        @Override
        public int compareTo(DollarAmount dollarAmount) {
            return this.amount - dollarAmount.amount;
        }
    }

    static class Account {
        private DollarAmount balance;
        private final int acctNo;
        private static final AtomicInteger sequence = new AtomicInteger();


        public Account(int initMoney) {
            acctNo = sequence.incrementAndGet();
            balance = new DollarAmount(initMoney);
        }

        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }

        void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        DollarAmount getBalance() {
            return balance;
        }

        int getAcctNo() {
            return acctNo;
        }
    }

    static class InsufficientFundsException extends Exception {
        public InsufficientFundsException() {
            super();
            System.out.println("account balance is insufficient!");
        }
    }

    public static void main(String[] args) throws InsufficientFundsException {
        Account account1 = new Account(100);
        Account account2 = new Account(200);

        transferMoney(account1, account2, new DollarAmount(50));
        transferMoney(account2, account1, new DollarAmount(60));
    }
}
