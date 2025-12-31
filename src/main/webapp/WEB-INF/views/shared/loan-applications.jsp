<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Loan Applications</title>

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
                <button id="logout-btn" type="submit" class="btn btn-danger btn-sm px-3">Log out</button>
            </form>
        </nav>

        <div class="container-fluid mt-3">

            <div class="mb-3 d-flex justify-content-between align-items-center">
                <!-- Page Title -->
                <h3 class>Loan Applications</h3>
                <c:if test="${sessionScope.user.role eq 'MAKER'}">
                    <a href="${pageContext.request.contextPath}/maker/dashboard" class="btn btn-primary px-3"> Dashboard</a>
                </c:if>
            </div>

            <!-- Loan Applications Table -->
            <div class="card shadow-sm">
                <div class="card-body">
                    <table class="table table-hover align-middle">
                        <thead class="table-primary">
                            <tr>
                                <th>Application Number</th>
                                <th>Customer Name</th>
                                <th>Mobile Number</th>
                                <th>Product Type</th>
                                <th>Product</th>
                                <th>Loan Amount</th>
                                <th>Applied On</th>
                                <th>Loan Status</th>
                                <th>Record Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="loanApplication" items="${loanApplications}">
                                <tr>
                                    <td>${loanApplication.applicationNumber}</td>
                                    <td>${loanApplication.customer.customerName}</td>
                                    <td>${loanApplication.customer.contactNumber}</td>
                                    <td>${loanApplication.productType}</td>
                                    <td>${loanApplication.product}</td>
                                    <td>
                                        <fmt:formatNumber value="${loanApplication.loanAmount}" pattern="#0.00" />
                                    </td>
                                    <td>${loanApplication.applicationCreationDate}</td>

                                    <td>
                                        <span class="badge
                                        <c:choose>
                                            <c:when test=" ${loanApplication.loanStatus=='APPROVED' }">bg-success text-white</c:when>
                                            <c:when test="${loanApplication.loanStatus == 'PENDING'}">bg-warning text-dark</c:when>
                                            <c:when test="${loanApplication.loanStatus == 'REJECTED'}">bg-danger text-white</c:when>
                                            <c:otherwise>bg-light text-dark</c:otherwise>
                                            </c:choose>
                                            ">
                                            ${loanApplication.loanStatus}
                                        </span>

                                    </td>

                                    <td>
                                        <span class="badge
                                        <c:choose>
                                            <c:when test=" ${loanApplication.workflowStatus=='NEW' }">badge bg-primary text-white</c:when>
                                            <c:when test="${loanApplication.workflowStatus == 'PENDING_MODIFICATION'}">badge bg-info text-dark</c:when>
                                            <c:when test="${loanApplication.workflowStatus == 'PENDING_DELETION'}">badge bg-secondary text-white</c:when>
                                            <c:when test="${loanApplication.workflowStatus == 'AUTHORIZED'}">badge bg-success text-white</c:when>
                                            <c:when test="${loanApplication.workflowStatus == 'REJECTED_NEW'}">badge bg-danger text-white</c:when>
                                            <c:when test="${loanApplication.workflowStatus == 'REJECTED_MODIFICATION'}">badge bg-danger text-dark</c:when>
                                            <c:when test="${loanApplication.workflowStatus == 'REJECTED_DELETION'}">badge bg-danger text-white</c:when>
                                            <c:otherwise>bg-light text-dark</c:otherwise>
                                            </c:choose>
                                            ">
                                            ${loanApplication.workflowStatus}
                                        </span>

                                    </td>

                                    <td>
                                        <!-- Maker Actions -->
                                        <c:if test="${sessionScope.user.role eq 'MAKER'}">
                                            <form action="${pageContext.request.contextPath}/maker/loan/applications/action" method="post" class="d-flex gap-2 align-items-center">
                                                <input type="hidden" name="applicationNumber" value="${loanApplication.applicationNumber}">
                                                <button type="submit" name="action" value="MODIFY" class="btn btn-sm btn-secondary">Modify</button>
                                                <button type="submit" name="action" value="DELETE" class="btn btn-sm btn-danger">Delete</button>
                                            </form>
                                        </c:if>

                                        <!-- Checker Actions -->
                                        <c:if test="${sessionScope.user.role eq 'CHECKER'}">
                                            <form action="${pageContext.request.contextPath}/checker/dashboard/action" method="post" class="d-flex gap-2 align-items-center">
                                                <input type="hidden" name="applicationNumber" value="${loanApplication.applicationNumber}">
                                                <c:if test="${loanApplication.workflowStatus == 'NEW' || loanApplication.workflowStatus == 'PENDING_MODIFICATION' || loanApplication.workflowStatus == 'PENDING_DELETION'}">
                                                    <button type="submit" name="action" value="APPROVE" class="btn btn-sm btn-success">Approve</button>
                                                    <button type="submit" name="action" value="REJECT" class="btn btn-sm btn-danger">Reject</button>
                                                </c:if>
                                            </form>
                                        </c:if>
                                    </td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- jQuery, Bootstrap, Toastr -->
        <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

        <!-- Flash Message through request -->
        <c:if test="${not empty flashMessage}">
            <script>
                $(function() {
                    toastr.options = {
                        "closeButton": true,
                        "progressBar": true,
                        "positionClass": "toast-bottom-right",
                        "timeOut": "5000"
                    };
                    var type = "<c:out value='${flashType}'/>";
                    var msg = "<c:out value='${flashMessage}'/>";
                    if (type === "error") toastr.error(msg);
                    else toastr.success(msg);
                });

            </script>
            <c:remove var="flashMessage" scope="request" />
            <c:remove var="flashType" scope="request" />
        </c:if>
        <c:if test="${not empty sessionScope.flashMessage}">
            <script>
                $(function() {
                    toastr.options = {
                        "closeButton": true,
                        "progressBar": true,
                        "positionClass": "toast-bottom-right",
                        "timeOut": "5000"
                    };
                    var type = "<c:out value='${sessionScope.flashType}'/>";
                    var msg = "<c:out value='${sessionScope.flashMessage}'/>";
                    if (type === 'success') toastr.success(msg);
                    else toastr.warn(msg);
                });

            </script>
            <c:remove var="flashMessage" scope="session" />
            <c:remove var="flashType" scope="session" />
        </c:if>

    </body>

</html>
