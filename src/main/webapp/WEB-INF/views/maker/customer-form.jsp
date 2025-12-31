 <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Customer Form</title>

        <!-- Bootstrap, Bootstrap Icons CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css" rel="stylesheet" integrity="sha512-3pIirOrwegjM6erE5gPSwkUzO+3cTjpnV9lexlNZqvupR64iZBnOOTiiLPb9M36zpMScbmUNIcHUqKD47M719g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body class="bg-light">
        <!-- Header -->
        <nav class="navbar navbar-dark bg-dark px-3">
            <span class="navbar-brand fw-bold mb-0">Mini CAS Application</span>
            <form action="${pageContext.request.contextPath}/logout" method="post" class="m-0 d-flex align-items-center">
                <span class="text-white fw-bold me-3">Welcome, ${sessionScope.user.username} (${sessionScope.user.role})</span>
                <button type="submit" class="btn btn-danger btn-sm px-3">Log out</button>
            </form>
        </nav>

        <!-- Tabs -->
        <div class="d-flex justify-content-between align-items-center px-3">
            <ul class="nav nav-tabs mt-3">
                <li class="nav-item">
                    <div class="nav-link active" style="cursor: pointer;">Applicant Information</div>
                </li>
                <li class="nav-item">
                    <div class="nav-link" style="cursor: pointer;">Loan Details</div>
                </li>
            </ul>

            <c:if test="${sessionScope.user.role eq 'MAKER'}">
                <a href="${pageContext.request.contextPath}/maker/dashboard" class="btn btn-primary px-3"> Dashboard</a>
            </c:if>
        </div>

        <!-- Additional Header For Tabs And Expanded Options -->
        <div class="d-flex justify-content-between align-items-center w-100 p-2 border bg-light">
            <!-- Left aligned section title -->
            <h5 class="mb-0">Applicant Details</h5>

            <!-- Right aligned toggle buttons -->
            <div class="btn-group" role="group" aria-label="View Mode">
                <button type="button" class="btn btn-primary active btn-sm">Tabs</button>
                <button type="button" class="btn btn-outline-primary btn-sm">Expanded</button>
            </div>
        </div>

        <!-- Main Body Container -->
        <div class="container-fluid">
            <div class="row">

                <!-- Left Panel -->
                <aside class="col-md-2 bg-light border-end p-3">
                    <h5 class="d-block border bg-primary text-white rounded p-2">Personal Information</h5>
                    <!-- You can extend this with more nav links later if needed -->
                </aside>

                <!-- Right Content -->
                <main class="col-md-10 p-4">

                    <!-- Content Header -->
                    <div class="d-flex align-items-end mb-3">
                        <h3 class="mb-0">Personal Information</h3>
                        <div class="mx-3 d-flex align-items-center gap-2">
                            <span class="badge bg-secondary-subtle text-dark border fw-bold">All Fields</span>
                            <span>|</span>
                            <span class="fw-bold text-primary small">
                                Required <span class="text-danger">*</span>
                            </span>
                        </div>

                    </div>
                    <hr />

                    <!-- Main Form -->
                    <c:url var="saveCustomerUrl" value="/maker/loan/new/customer" />
                    <form id="customerForm" method="post" action="${saveCustomerUrl}" class="needs-validation" novalidate>

                        <!-- Personal Information Form Section -->
                        <section id="personalInformationSection" class="mb-4">
                            <div class="card">
                                <div class="card-body">

                                    <!-- Host ID (wider + disabled) -->
                                    <div class="mb-3">
                                        <label for="hostId" class="form-label">Host ID</label>
                                        <input type="text" id="hostId" name="hostId" class="form-control w-75" placeholder="Host ID" disabled>
                                    </div>

                                    <!-- Uniform Grid Layout -->
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label for="applicantGender" class="form-label">Applicant's Gender <span class="text-danger">*</span></label>
                                            <select id="applicantGender" name="gender" class="form-select" required>
                                                <option value="" disabled selected>Select Gender</option>
                                                <option value="Male">Male</option>
                                                <option value="Female">Female</option>
                                                <option value="Other">Other</option>
                                            </select>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="salutation" class="form-label">Salutation</label>
                                            <select id="salutation" name="salutation" class="form-select">
                                                <option value="" selected>Select Salutation</option>
                                                <option value="Mr.">Mr.</option>
                                                <option value="Ms.">Ms.</option>
                                                <option value="Mrs.">Mrs.</option>
                                            </select>
                                        </div>

                                        <div class="col-md-6">
                                            <label for="firstName" class="form-label">First Name <span class="text-danger">*</span></label>
                                            <input type="text" id="firstName" name="firstName" class="form-control" required placeholder="First Name">
                                        </div>
                                        <div class="col-md-6">
                                            <label for="middleName" class="form-label">Middle Name</label>
                                            <input type="text" id="middleName" name="middleName" class="form-control" placeholder="Middle Name">
                                        </div>
                                        <div class="col-md-6">
                                            <label for="lastName" class="form-label">Last Name <span class="text-danger">*</span></label>
                                            <input type="text" id="lastName" name="lastName" class="form-control" required placeholder="Last Name">
                                        </div>
                                        <div class="col-md-6">
                                            <label for="dateOfBirth" class="form-label">Date of Birth <span class="text-danger">*</span></label>
                                            <input type="date" id="dateOfBirth" name="dateOfBirth" class="form-control" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="nationality" class="form-label">Nationality <span class="text-danger">*</span></label>
                                            <select id="nationality" name="nationality" class="form-select" required>
                                                <option value="">Select</option>
                                            </select>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="contactNumber" class="form-label">Primary Mobile Number</label>
                                            <div class="input-group">
                                                <!-- Country ISO Code dropdown -->
                                                <select id="countryCode" name="countryCode" class="form-select" style="max-width: 120px;">
                                                    <option value="IN" data-dial="+91" selected>IN</option>
                                                </select>

                                                <!-- Editable dial code -->
                                                <input type="text" id="dialCode" name="dialCode" class="form-control" value="+91" style="max-width: 90px;">

                                                <!-- Phone number -->
                                                <input type="text" id="contactNumber" name="contactNumber" class="form-control" minlength="4" maxlength="20" placeholder="Mobile Phone">
                                            </div>
                                        </div>

                                    </div>

                                    <!-- Proceed button -->
                                    <div class="mt-4 text-end">
                                        <button type="button" id="personal-detail-pcd-btn" class="btn btn-primary">Proceed</button>
                                    </div>

                                </div>
                            </div>
                        </section>

                        <!-- Address Information Form Section -->
                        <section id="addressInformationSection" class="mb-4">
                            <div class="card">

                                <div class="card-header d-flex align-items-center">
                                    <i class="bi bi-chevron-right me-2 dropdown-icon"></i>
                                    <span>Address</span>
                                </div>

                                <div class="card-body">

                                    <!-- Row 1 -->
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label for="flatPlotNumber" class="form-label">Flat / Plot Number <span class="text-danger">*</span></label>
                                            <input type="text" id="flatPlotNumber" name="flatPlotNumber" class="form-control" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="addressLine2" class="form-label">Address Line 2</label>
                                            <input type="text" id="addressLine2" name="addressLine2" class="form-control">
                                        </div>
                                    </div>

                                    <!-- Row 2 -->
                                    <div class="row g-3 mt-2">
                                        <div class="col-md-6">
                                            <label for="country" class="form-label">Country <span class="text-danger">*</span></label>
                                            <select id="country" name="country" class="form-select" required>
                                                <option value="">Select</option>
                                            </select>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="pin-code" class="form-label">PinCode <span class="text-danger">*</span></label>
                                            <input type="text" id="pin-code" name="pinCode" class="form-control" required>
                                        </div>
                                    </div>

                                    <!-- Row 3 -->
                                    <div class="row g-3 mt-2">
                                        <div class="col-md-6">
                                            <label for="state" class="form-label">State <span class="text-danger">*</span></label>
                                            <select id="state" name="state" class="form-select" required></select>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="city" class="form-label">City</label>
                                            <select id="city" name="city" class="form-select"></select>
                                        </div>
                                    </div>

                                    <!-- Proceed button -->
                                    <div class="mt-4 text-end">
                                        <button type="button" id="address-detail-pcd-btn" class="btn btn-primary">Proceed</button>
                                    </div>

                                </div>
                            </div>
                        </section>

                        <!-- Education Information Form Section -->
                        <section id="educationInformationSection" class="mb-4">
                            <div class="card">

                                <div class="card-header d-flex align-items-center">
                                    <i class="bi bi-chevron-right me-2 dropdown-icon"></i>
                                    <span>Education</span>
                                </div>

                                <div id="educationInformationForm" class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered align-middle">
                                            <thead class="table-light text-center">
                                                <tr>
                                                    <th style="width: 18%;">Qualification Type <span class="text-danger">*</span></th>
                                                    <th style="width: 15%;">Classification</th>
                                                    <th style="width: 15%;">Specialization</th>
                                                    <th style="width: 18%;">Board/University</th>
                                                    <th style="width: 12%;">Year of Passing</th>
                                                    <th style="width: 10%;">Is Highest Degree</th>
                                                    <th style="width: 8%;">Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>
                                                        <select name="qualificationType[]" class="form-select" required>
                                                            <option value="">Select</option>
                                                            <option value="Graduation">Graduation</option>
                                                            <option value="Intermediate">Intermediate</option>
                                                            <option value="Matriculation">Matriculation</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <select name="classification[]" class="form-select">
                                                            <option value="">Select</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <select name="specialization[]" class="form-select">
                                                            <option value="">Select</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="boardName[]" class="form-control" placeholder="Board">
                                                    </td>
                                                    <td>
                                                        <input type="text" name="yearOfPassing[]" class="form-control" placeholder="YYYY">
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="radio" class="highest-degree-radio" name="isHighestDegree" value="0">
                                                    </td>
                                                    <td class="text-center">
                                                        <button type="button" class="btn p-0 border-0 bg-transparent text-danger">
                                                            <i class="bi bi-trash fs-5"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>

                                    <!-- Add Row Button -->
                                    <div class="mt-3">
                                        <button id="addRowBtn" type="button" class="btn btn-sm btn-success">
                                            + Add Row
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <!-- Bottom Buttons -->
                        <div class="text-end">
                            <button type="button" class="btn btn-secondary" disabled>Save</button>
                            <button id="saveNextBtn" type="submit" class="btn btn-primary" disabled>Save & Next</button>
                            <button type="button" class="btn btn-outline-danger">Cancel</button>
                        </div>
                    </form>
                </main>
            </div>
        </div>

        <!-- jQuery, Bootstrap, Toastr -->
        <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
        <script src="${pageContext.request.contextPath}/script/customer-form.js"></script>
    </body>

</html>
