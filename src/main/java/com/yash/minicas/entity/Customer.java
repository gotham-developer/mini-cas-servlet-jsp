package com.yash.minicas.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer implements Serializable {

    @EqualsAndHashCode.Include
    private int       customerId;
    private String    customerName;
    private String    gender;
    private LocalDate dateOfBirth;
    private String    contactNumber;
    private String    salutation;
    private String    nationality;

    // Address fields
    private String flatPlotNumber;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    private Set<Education> educationSet;

    private Customer() { }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", customerName='" + customerName + '\'' + ", gender='" + gender + '\'' +
               ", dateOfBirth=" + dateOfBirth + ", contactNumber='" + contactNumber + '\'' + ", salutation='" + salutation + '\'' +
               ", nationality='" + nationality + '\'' + ", flatPlotNumber='" + flatPlotNumber + '\'' + ", addressLine2='" + addressLine2 +
               '\'' + ", city='" + city + '\'' + ", state='" + state + '\'' + ", country='" + country + '\'' + ", pinCode='" + pinCode +
               '\'' + ", educationSet=" + educationSet + '}';
    }

    public static final class CustomerBuilder {
        private final String         customerName;
        private       int            customerId;
        private       String         gender;
        private       LocalDate      dateOfBirth;
        private       String         contactNumber;
        private       String         salutation;
        private       String         nationality;
        private       String         flatPlotNumber;
        private       String         addressLine2;
        private       String         city;
        private       String         state;
        private       String         country;
        private       String         pinCode;
        private       Set<Education> educationSet;

        private CustomerBuilder(String customerName) { this.customerName = customerName; }

        public static CustomerBuilder aCustomer(String customerName) { return new CustomerBuilder(customerName); }

        public CustomerBuilder withCustomerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public CustomerBuilder withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public CustomerBuilder withDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public CustomerBuilder withContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public CustomerBuilder withSalutation(String salutation) {
            this.salutation = salutation;
            return this;
        }

        public CustomerBuilder withNationality(String nationality) {
            this.nationality = nationality;
            return this;
        }

        public CustomerBuilder withFlatPlotNumber(String flatPlotNumber) {
            this.flatPlotNumber = flatPlotNumber;
            return this;
        }

        public CustomerBuilder withAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public CustomerBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public CustomerBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public CustomerBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public CustomerBuilder withPinCode(String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public CustomerBuilder withEducationSet(Set<Education> educationSet) {
            this.educationSet = educationSet;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            customer.setCustomerName(customerName);
            customer.setGender(gender);
            customer.setDateOfBirth(dateOfBirth);
            customer.setContactNumber(contactNumber);
            customer.setSalutation(salutation);
            customer.setNationality(nationality);
            customer.setFlatPlotNumber(flatPlotNumber);
            customer.setAddressLine2(addressLine2);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setPinCode(pinCode);
            customer.setEducationSet(educationSet);
            return customer;
        }
    }
}
