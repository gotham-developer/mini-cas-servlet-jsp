package com.yash.minicas.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Education {

    @EqualsAndHashCode.Include
    private int    id;
    private String qualificationType;  // SSC, HSC, Graduation, postGrad
    private String classification;     // 1st Division, 2nd Division
    private String specialization;     // Science, Commerce, CS
    private String boardUniversity;
    private int    yearOfPassing;
    private String isHighestDegree;
    private int    customerId;

    private Education() { }

    @Override
    public String toString() {
        return "Education{" + "id=" + id + ", qualificationType='" + qualificationType + '\'' + ", classification='" + classification +
               '\'' + ", specialization='" + specialization + '\'' + ", boardUniversity='" + boardUniversity + '\'' + ", yearOfPassing=" +
               yearOfPassing + ", isHighestDegree='" + isHighestDegree + '\'' + ", customerId=" + customerId + '}';
    }

    public static final class EducationBuilder {
        private final String qualificationType;
        private       int    id;
        private       String classification;
        private       String specialization;
        private       String boardUniversity;
        private       int    yearOfPassing;
        private       String isHighestDegree;
        private       int    customerId;

        public EducationBuilder(String qualificationType) {
            this.qualificationType = qualificationType;
        }

        public EducationBuilder(Education other) {
            this.id = other.id;
            this.qualificationType = other.qualificationType;
            this.classification = other.classification;
            this.specialization = other.specialization;
            this.boardUniversity = other.boardUniversity;
            this.yearOfPassing = other.yearOfPassing;
            this.isHighestDegree = other.isHighestDegree;
            this.customerId = other.customerId;
        }

        public static EducationBuilder anEducation(String qualificationType) {
            return new EducationBuilder(qualificationType);
        }

        public EducationBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public EducationBuilder withClassification(String classification) {
            this.classification = classification;
            return this;
        }

        public EducationBuilder withSpecialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public EducationBuilder withBoardUniversity(String boardUniversity) {
            this.boardUniversity = boardUniversity;
            return this;
        }

        public EducationBuilder withYearOfPassing(int yearOfPassing) {
            this.yearOfPassing = yearOfPassing;
            return this;
        }

        public EducationBuilder withIsHighestDegree(String isHighestDegree) {
            this.isHighestDegree = isHighestDegree;
            return this;
        }

        public EducationBuilder withCustomerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public Education build() {
            Education education = new Education();
            education.id = id;
            education.qualificationType = qualificationType;
            education.classification = classification;
            education.specialization = specialization;
            education.boardUniversity = boardUniversity;
            education.yearOfPassing = yearOfPassing;
            education.customerId = customerId;
            education.isHighestDegree = isHighestDegree;
            return education;
        }
    }

}
