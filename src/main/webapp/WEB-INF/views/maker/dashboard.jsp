 <%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard</title>

        <!-- Bootstrap, Bootstrap Icons, Toastr CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css" rel="stylesheet" integrity="sha512-3pIirOrwegjM6erE5gPSwkUzO+3cTjpnV9lexlNZqvupR64iZBnOOTiiLPb9M36zpMScbmUNIcHUqKD47M719g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body class="bg-light">

        <!-- Header -->
        <nav class="navbar navbar-dark bg-dark px-3">
            <span class="navbar-brand fw-bold mb-0">Mini CAS Application</span>
            <form action="${pageContext.request.contextPath}/logout" method="post" class="d-flex align-items-center m-0">
                <span class="text-white fw-bold me-3">Welcome, ${sessionScope.user.username} (${sessionScope.user.role})</span>
                <button id="logout-btn" type="submit" class="btn btn-danger btn-sm px-3">Log out</button>
            </form>
        </nav>

        <!-- Main container filling the viewport -->
        <div class="d-flex flex-column" style="height: calc(100vh - 56px);">
            <!-- Heading -->
            <div class="container text-center my-3">
                <h2 class="fw-bold text-dark">Maker Dashboard</h2>
            </div>
            <!-- Buttons container -->
            <div class="d-flex flex-column flex-grow-1 justify-content-center align-items-center">
                <div class="d-flex flex-column gap-3 w-100" style="max-width: 380px;">

                    <c:url var="newLoanUrl" value="/maker/loan/new" />
                    <a href="${newLoanUrl}" class="btn btn-white border border-primary rounded-3 shadow-sm d-flex align-items-center p-3 text-primary card-btn w-100 fs-5">
                        <i class="bi bi-pencil-square me-3 fs-1"></i>
                        <span>Apply for a New Loan</span>
                    </a>

                    <c:url var="loanApplicationsUrl" value="/maker/loan/applications" />
                    <a href="${loanApplicationsUrl}" class="btn btn-white border border-secondary rounded-3 shadow-sm d-flex align-items-center p-3 text-secondary card-btn w-100 fs-5">
                        <i class="bi bi-journal-text me-3 fs-1"></i>
                        <span>View All Loan Applications</span>
                    </a>
                </div>
            </div>

        </div>

        <!-- jQuery + Bootstrap, Toastr -->
        <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

        <script>
            $(function() {
                $('a.card-btn').hover(
                    function() {
                        $(this).addClass('shadow-lg');
                    },
                    function() {
                        $(this).removeClass('shadow-lg');
                    }
                );
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
                    toastr.success("<c:out value='${sessionScope.flashMessage}' />");
                });

            </script>
            <c:remove var="flashMessage" scope="session" />
        </c:if>

    </body>

</html>
