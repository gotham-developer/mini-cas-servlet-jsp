 <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Loan Application Form</title>

        <!-- Bootstrap, Bootstrap Icons, Toastr CSS -->
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
                    <div class="nav-link" style="cursor: pointer;">Applicant Information</div>
                </li>
                <li class="nav-item">
                    <div class="nav-link active" style="cursor: pointer;">Loan Details</div>
                </li>
            </ul>

            <c:if test="${sessionScope.user.role eq 'MAKER'}">
                <a href="${pageContext.request.contextPath}/maker/dashboard" class="btn btn-primary px-3"> Dashboard</a>
            </c:if>
        </div>

        <!-- Additional Header For Tabs And Expanded Options -->
        <div class="d-flex justify-content-between align-items-center w-100 p-2 border bg-light">
            <!-- Left aligned section title -->
            <h5 class="mb-0">Loan Application Details</h5>

            <!-- Right aligned toggle buttons -->
            <div class="btn-group" role="group" aria-label="View Mode">
                <button type="button" class="btn btn-primary active btn-sm">Tabs</button>
                <button type="button" class="btn btn-outline-primary btn-sm">Expanded</button>
            </div>
        </div>

        <!-- Main Body -->
        <div class="container-fluid">
            <div class="row">

                <!-- Left Panel -->
                <aside class="col-md-2 bg-light border-end p-3">
                    <h5 class="d-block border bg-primary text-white rounded p-2">Sourcing Details</h5>
                </aside>

                <!-- Right Content -->
                <main class="col-md-10 p-4">

                    <!-- Content Header -->
                    <div class="d-flex align-items-end mb-3">
                        <h3 class="mb-0">Loan Information</h3>
                        <div class="mx-3 d-flex align-items-center gap-2">
                            <span class="badge bg-secondary-subtle text-dark border fw-bold">All Fields</span>
                            <span>|</span>
                            <span class="fw-bold text-primary small">
                                Required <span class="text-danger">*</span>
                            </span>
                        </div>
                    </div>
                    <hr>

                    <!-- Form -->
                    <c:url var="saveLoanApplicationUrl" value="/maker/loan/new/application/submit" />
                    <form id="loan-application-form" method="post" action="${saveLoanApplicationUrl}" class="needs-validation" novalidate>

                        <!-- Metadata / hidden fields -->
                        <input type="hidden" name="customerId" value="${customerId}">

                        <!-- Loan Info Section -->
                        <section class="card mb-4">
                            <div class="card-body">

                                <!-- Application ID (wider + disabled) -->
                                <div class="mb-3">
                                    <label for="application-id" class="form-label">Application ID</label>
                                    <input type="text" id="application-id" name="applicationID" class="form-control w-75" placeholder="Application ID" disabled>
                                </div>

                                <!-- Uniform Grid Layout -->
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="product-type" class="form-label">Product Type <span class="text-danger">*</span></label>
                                        <select id="product-type" name="productType" class="form-select" required>
                                            <option value="" selected>Select Product Type</option>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="product" class="form-label">Product <span class="text-danger">*</span></label>
                                        <select id="product" name="product" class="form-select" required>
                                            <option value="" selected hidden>Select Product</option>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="scheme" class="form-label">Scheme <span class="text-danger">*</span></label>
                                        <select id="scheme" name="scheme" class="form-select" required>
                                            <option value="" selected hidden>Select Scheme</option>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="amount-requested" class="form-label">Amount Requested <span class="text-danger">*</span></label>
                                        <div class="input-group">
                                            <select id="country-currency" class="form-select" style="max-width: 120px;">
                                                <option value="INR" selected>INR</option>
                                            </select>
                                            <input type="text" id="amount-requested" name="loanAmount" class="form-control" placeholder="Amount Requested" required>
                                        </div>
                                    </div>

                                    <!-- Tenure and Tenure In combined with labels -->
                                    <div class="col-md-6">
                                        <div class="d-flex gap-2">
                                            <div class="flex-fill">
                                                <label for="tenure" class="form-label">Tenure <span class="text-danger">*</span></label>
                                                <input name="tenure" type="text" id="tenure" class="form-control" placeholder="Tenure" required>
                                            </div>
                                            <div class="flex-fill">
                                                <label for="tenure-in" class="form-label">Tenure In</label>
                                                <select name="tenureIn" id="tenure-in" class="form-select">
                                                    <option value="">Select</option>
                                                    <option value="Months">Months</option>
                                                    <option value="Years">Years</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <label for="rate" class="form-label">Rate <span class="text-danger">*</span></label>
                                        <input type="text" id="rate" name="rate" class="form-control" placeholder="Annual Rate" required>
                                    </div>

                                    <div class="col-md-6 d-flex align-items-end mt-5">
                                        <button type="button" id="generate-button" class="btn btn-primary w-100 btn-lg">Generate Repayment
                                            Schedule</button>
                                    </div>
                                </div>

                            </div>
                        </section>

                        <!-- Submit Options -->
                        <div class="card mb-4 p-3">
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="d-flex align-items-center">
                                    <span class="form-label me-3">Do you want to submit?</span>
                                    <div class="form-check form-check-inline">
                                        <input type="radio" class="form-check-input" name="submit-form" id="form-submit-yes" required>
                                        <label class="form-check-label" for="form-submit-yes">Yes</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input type="radio" class="form-check-input" name="submit-form" id="form-submit-no" required>
                                        <label class="form-check-label" for="form-submit-no">No</label>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary">Submit</button>
                            </div>
                        </div>
                    </form>
                </main>
            </div>
        </div>

        <!-- Repayment Schedule Modal -->
        <div class="modal fade" id="repayment-modal" tabindex="-1" aria-labelledby="repaymentModalLabel">
            <div class="modal-dialog modal-xl modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title" id="repayment-modal-label">Repayment Schedule</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped text-center" id="repayment-table">
                                <thead class="table-light">
                                    <tr>
                                        <th>Installment No.</th>
                                        <th>Opening Amount</th>
                                        <th>EMI</th>
                                        <th>Interest Component</th>
                                        <th>Principal Component</th>
                                        <th>Closing Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Rows will be appended here dynamically -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- jQuery, Bootstrap, Toastr -->
        <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
        <script src="${pageContext.request.contextPath}/script/loan-application-form.js"></script>

        <c:if test="${not empty sessionScope.flashMessage}">
            <script>
                $(function() {
                    toastr.options = {
                        "closeButton": true,
                        "progressBar": true,
                        "positionClass": "toast-bottom-right",
                        "timeOut": "5000"
                    };
                    toastr.success("<c:out value='${sessionScope.flashMessage}' />");
                });

            </script>
            <c:remove var="flashMessage" scope="session" />
        </c:if>
    </body>

</html>
