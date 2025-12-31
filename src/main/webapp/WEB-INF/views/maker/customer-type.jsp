 <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Customer Type</title>

        <!-- Bootstrap, Toastr CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
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

        <!-- Main Content -->
        <div class="container-fluid mt-3" id="applicantInfo">
            <div class="row">

                <!-- Left Panel -->
                <aside class="col-md-2 bg-light border-end p-3">
                    <h5>Applicant is</h5>
                    <!-- Extendable nav links here if needed -->
                </aside>

                <!-- Right Panel -->
                <div class="col-md-10">

                    <c:url var="newCustomerUrl" value="/maker/loan/new/customer" />
                    <!-- Customer Type Form -->
                    <form action="${newCustomerUrl}" method="get" id="customerTypeForm">

                        <!-- Applicant Type Radios -->
                        <div class="mb-3">
                            <div class="form-check">
                                <input id="applicantTypeNew" name="applicantType" class="form-check-input" type="radio" value="NEW_CUSTOMER" <c:if test="${empty sessionScope.loanApplication}">checked</c:if> />
                                <label class="form-check-label" for="applicantTypeNew">A New Customer</label>
                            </div>

                            <!-- New Customer Options -->
                            <div id="newCustomerOptions" class="ms-4 mt-2">
                                <div class="form-check mb-2">
                                    <input id="customerTypeIndividual" name="newCustomerType" class="form-check-input" type="radio" value="INDIVIDUAL" />
                                    <label class="form-check-label" for="customerTypeIndividual">
                                        Individual
                                        <a href="#" class="ms-2 small">External Verification</a>
                                    </label>
                                </div>
                                <div class="form-check mb-2">
                                    <input id="customerTypeNonIndividual" name="newCustomerType" class="form-check-input" type="radio" value="NON_INDIVIDUAL" />
                                    <label class="form-check-label" for="customerTypeNonIndividual">Non-Individual</label>
                                </div>

                                <button type="submit" class="btn btn-primary btn-sm mt-2">Create New Customer</button>
                            </div>
                        </div>

                        <hr />

                        <div class="form-check">
                            <input id="applicantTypeExisting" name="applicantType" class="form-check-input" type="radio" value="EXISTING_CUSTOMER" <c:if test="${not empty sessionScope.loanApplication}">checked</c:if> />
                            <label class="form-check-label" for="applicantTypeExisting">An Existing Customer</label>
                        </div>

                    </form>

                    <!-- Existing Customer Search Block -->
                    <div id="existingCustomerBlock" class="border rounded p-3 bg-white mt-3 <c:if test='${empty loan}'>d-none</c:if>">

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="mb-0">Search Existing Customer</h5>
                            <button id="cancelSearch" type="button" class="btn btn-outline-secondary btn-sm">Cancel</button>
                        </div>

                        <!-- Customer Type Radios -->
                        <div class="mb-3">
                            <label class="form-label d-block">Customer Type:</label>
                            <div class="form-check form-check-inline">
                                <input id="customerTypeSearchIndividual" name="existingCustomerType" class="form-check-input" type="radio" value="INDIVIDUAL" checked />
                                <label class="form-check-label" for="customerTypeSearchIndividual">Individual</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input id="customerTypeSearchNonIndividual" name="existingCustomerType" class="form-check-input" type="radio" value="NON_INDIVIDUAL" />
                                <label class="form-check-label" for="customerTypeSearchNonIndividual">Non-Individual</label>
                            </div>
                        </div>

                        <c:url var="existingCustomerUrl" value="/maker/loan/search" />
                        <!-- Search Form -->
                        <form id="searchCustomerForm" action="${existingCustomerUrl}" method="post" class="row g-3">
                            <div class="col-md-6">
                                <label for="applicationNumber" class="form-label">Application ID / Deal ID</label>
                                <input id="applicationNumber" name="applicationNumber" class="form-control" type="text" placeholder="Enter Application ID / Deal ID" value="<c:out value='${sessionScope.loanApplication.applicationNumber}'/>" />
                            </div>
                            <div class="col-md-6">
                                <label for="applicationStatus" class="form-label">Status</label>
                                <input id="applicationStatus" name="applicationStatus" class="form-control bg-light fw-bold text-primary" type="text" placeholder="Status" readonly value="<c:out value='${not empty sessionScope.loanApplication.status ? sessionScope.loanApplication.status : ""}'/>" />
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-success">Search</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- jQuery, Bootstrap, Toastr -->
        <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

        <script>
            $(function() {
                function toggleExistingForm() {
                    if ($('#applicantTypeExisting').is(':checked')) {
                        $('#existingCustomerBlock').removeClass('d-none');
                        $('#newCustomerOptions').addClass('d-none');
                    } else {
                        $('#existingCustomerBlock').addClass('d-none');
                        $('#newCustomerOptions').removeClass('d-none');
                    }
                }
                // Run on page load (handles auto-selection when loan found)
                toggleExistingForm();
                // On radio change
                $('input[name="applicantType"]').change(toggleExistingForm);
                // Cancel button for search
                $('#cancelSearch').click(function() {
                    $('#existingCustomerBlock').addClass('d-none');
                    $('#applicantTypeNew').prop('checked', true).trigger('change');
                    // reset form fields
                    $('#applicationNumber').val('');
                    $('#applicationStatus').val('');
                });
            });

        </script>

        <c:if test="${not empty sessionScope.flashMessage}">
            <script>
                $(function() {
                    toastr.options = {
                        "closeButton": true,
                        "progressBar": true,
                        "positionClass": "toast-bottom-right",
                        "timeOut": "3000"
                    };
                    var type = "<c:out value='${sessionScope.flashType}'/>";
                    var msg = "<c:out value='${sessionScope.flashMessage}'/>";
                    if (type === "success") toastr.success(msg);
                    else if (type === "error") toastr.error(msg);
                });

            </script>
            <c:remove var="flashMessage" scope="session" />
            <c:remove var="flashType" scope="session" />
        </c:if>

    </body>

</html>
