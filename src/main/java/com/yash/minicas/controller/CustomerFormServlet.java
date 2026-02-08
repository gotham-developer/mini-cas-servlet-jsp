package com.yash.minicas.controller;

import com.yash.minicas.entity.Customer;
import com.yash.minicas.entity.Education;
import com.yash.minicas.util.DataValidationUtilities;
import com.yash.minicas.util.ServletFilterUtilities;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@WebServlet(urlPatterns = {"/maker/loan/new/customer"})
public class CustomerFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletFilterUtilities.forwardToUrl("/WEB-INF/views/maker/customer-form.jsp", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String   gender                = req.getParameter("gender").toUpperCase().substring(0, 1);
        String   salutation            = req.getParameter("salutation");
        String   firstName             = req.getParameter("firstName");
        String   middleName            = req.getParameter("middleName");
        String   lastName              = req.getParameter("lastName");
        String   dateOfBirthStr        = req.getParameter("dateOfBirth");
        String   nationality           = req.getParameter("nationality");
        String   contactNumber         = req.getParameter("contactNumber");
        String   flatPlotNumber        = req.getParameter("flatPlotNumber");
        String   addressLine2          = req.getParameter("addressLine2");
        String   country               = req.getParameter("country");
        String   state                 = req.getParameter("state");
        String   city                  = req.getParameter("city");
        String   pinCode               = req.getParameter("pinCode");
        String[] qualificationTypes    = req.getParameterValues("qualificationType[]");
        String[] classifications       = req.getParameterValues("classification[]");
        String[] specializations       = req.getParameterValues("specialization[]");
        String[] boardUniversities     = req.getParameterValues("boardName[]");
        String[] yearsOfPassing        = req.getParameterValues("yearOfPassing[]");
        String   selectedHighestDegree = req.getParameter("isHighestDegree");

        String    fullName           = firstName + " " + middleName + " " + lastName;
        LocalDate dateOfBirth        = LocalDate.parse(dateOfBirthStr);
        int       highestDegreeIndex = DataValidationUtilities.parseInt(selectedHighestDegree, -1);

        Set<Education> educationSet = new HashSet<>();
        int            rowCount     = qualificationTypes.length;
        for (int i = 0; i < rowCount; i++) {
            boolean isHighestDegree = (i == highestDegreeIndex);
            int     passYear        = DataValidationUtilities.parseInt(yearsOfPassing[i], 1947);
            Education education = Education.EducationBuilder.anEducation(qualificationTypes[i])
                                                            .withClassification(classifications[i])
                                                            .withSpecialization(specializations[i])
                                                            .withBoardUniversity(boardUniversities[i])
                                                            .withYearOfPassing(passYear)
                                                            .withIsHighestDegree(isHighestDegree ? "Y" : "N")
                                                            .build();
            educationSet.add(education);
        }

        Customer customer = Customer.CustomerBuilder.aCustomer(fullName)
                                                    .withGender(gender)
                                                    .withDateOfBirth(dateOfBirth)
                                                    .withNationality(nationality)
                                                    .withFlatPlotNumber(flatPlotNumber)
                                                    .withCity(city)
                                                    .withState(state)
                                                    .withCountry(country)
                                                    .withPinCode(pinCode)
                                                    .withSalutation(salutation)
                                                    .withContactNumber(contactNumber)
                                                    .withAddressLine2(addressLine2)
                                                    .withEducationSet(educationSet)
                                                    .build();

        HttpSession session = req.getSession();
        session.setAttribute("customer", customer);
        session.setAttribute("flashMessage", "Customer details saved successfully");

        ServletFilterUtilities.redirectToUrl("/maker/loan/new/application", req, resp);
    }
}
