package com.yash.minicas.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoanApplication implements Serializable {

    @EqualsAndHashCode.Include
    private int            applicationId;
    private String         applicationNumber;
    private LocalDate      applicationCreationDate;
    private double         loanAmount;
    private int            tenure;
    private String         tenureIn;
    private String         productType;
    private String         product;
    private String         scheme;
    private double         rate;
    private LoanStatus     loanStatus;
    private WorkflowStatus workflowStatus;
    private Customer       customer;

    private LoanApplication() { }

    @Override
    public String toString() {
        return "LoanApplication{" + "applicationId=" + applicationId + ", applicationNumber='" + applicationNumber + '\'' +
               ", applicationCreationDate=" + applicationCreationDate + ", loanAmount=" + loanAmount + ", tenure=" + tenure +
               ", tenureIn='" + tenureIn + '\'' + ", productType='" + productType + '\'' + ", product='" + product + '\'' + ", scheme='" +
               scheme + '\'' + ", rate=" + rate + ", loanStatus='" + loanStatus + '\'' + ", workflowStatus='" + workflowStatus + '\'' +
               ", customer=" + customer + '}';
    }

    public static final class LoanApplicationBuilder {
        private int            applicationId;
        private String         applicationNumber;
        private LocalDate      applicationCreationDate;
        private double         loanAmount;
        private int            tenure;
        private String         tenureIn;
        private String         productType;
        private String         product;
        private String         scheme;
        private double         rate;
        private LoanStatus     loanStatus;
        private WorkflowStatus workflowStatus;
        private Customer       customer;

        private LoanApplicationBuilder() { }

        public static LoanApplicationBuilder aLoanApplication() { return new LoanApplicationBuilder(); }

        public LoanApplicationBuilder withApplicationId(int applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public LoanApplicationBuilder withApplicationNumber(String applicationNumber) {
            this.applicationNumber = applicationNumber;
            return this;
        }

        public LoanApplicationBuilder withApplicationCreationDate(LocalDate applicationCreationDate) {
            this.applicationCreationDate = applicationCreationDate;
            return this;
        }

        public LoanApplicationBuilder withLoanAmount(double loanAmount) {
            this.loanAmount = loanAmount;
            return this;
        }

        public LoanApplicationBuilder withTenure(int tenure) {
            this.tenure = tenure;
            return this;
        }

        public LoanApplicationBuilder withTenureIn(String tenureIn) {
            this.tenureIn = tenureIn;
            return this;
        }

        public LoanApplicationBuilder withProductType(String productType) {
            this.productType = productType;
            return this;
        }

        public LoanApplicationBuilder withProduct(String product) {
            this.product = product;
            return this;
        }

        public LoanApplicationBuilder withScheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public LoanApplicationBuilder withRate(double rate) {
            this.rate = rate;
            return this;
        }

        public LoanApplicationBuilder withLoanStatus(LoanStatus loanStatus) {
            this.loanStatus = loanStatus;
            return this;
        }

        public LoanApplicationBuilder withWorkflowStatus(WorkflowStatus workflowStatus) {
            this.workflowStatus = workflowStatus;
            return this;
        }

        public LoanApplicationBuilder withCustomer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public LoanApplication build() {
            LoanApplication loanApplication = new LoanApplication();
            loanApplication.applicationNumber = applicationNumber;
            loanApplication.loanStatus = loanStatus;
            loanApplication.workflowStatus = workflowStatus;
            loanApplication.customer = customer;
            loanApplication.productType = this.productType;
            loanApplication.applicationCreationDate = this.applicationCreationDate;
            loanApplication.tenureIn = this.tenureIn;
            loanApplication.rate = this.rate;
            loanApplication.scheme = this.scheme;
            loanApplication.tenure = this.tenure;
            loanApplication.product = this.product;
            loanApplication.loanAmount = this.loanAmount;
            loanApplication.applicationId = this.applicationId;

            if (loanApplication.loanStatus == null) {
                loanApplication.loanStatus = LoanStatus.PENDING;
            }
            if (loanApplication.workflowStatus == null) {
                loanApplication.workflowStatus = WorkflowStatus.NEW;
            }
            if (loanApplication.applicationNumber == null) {
                loanApplication.applicationNumber = "LA" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS"));
            }
            if (loanApplication.customer == null) {
                loanApplication.customer = Customer.CustomerBuilder.aCustomer("Roger").build();
            }

            return loanApplication;
        }
    }

}
