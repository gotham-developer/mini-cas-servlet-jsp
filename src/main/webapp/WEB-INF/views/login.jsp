<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Mini CAS</title>

        <!-- Bootstrap, Toastr CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css" rel="stylesheet" integrity="sha512-3pIirOrwegjM6erE5gPSwkUzO+3cTjpnV9lexlNZqvupR64iZBnOOTiiLPb9M36zpMScbmUNIcHUqKD47M719g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body class="bg-light d-flex justify-content-center align-items-center vh-100">
        <div class="card shadow-lg rounded-4 w-100" style="max-width: 400px;">
            <div class="card-body p-4">
                <h4 class="text-center mb-4">Login</h4>

                <form id="loginForm" action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input id="username" name="username" class="form-control" type="text" required autocomplete="username">
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input id="password" name="password" class="form-control" type="password" required autocomplete="current-password">
                    </div>

                    <div class="form-check mb-3">
                        <input id="rememberMe" name="rememberMe" class="form-check-input" type="checkbox">
                        <label class="form-check-label" for="rememberMe">Keep me signed in</label>
                    </div>

                    <div class="mb-3 text-center">
                        <h6 class="form-label mt-4 mb-3">Role</h6>

                        <div class="form-check form-check-inline me-4">
                            <input id="maker" name="userRole" class="form-check-input" type="radio" value="Maker" checked>
                            <label class="form-check-label" for="maker">Maker</label>
                        </div>

                        <div class="form-check form-check-inline">
                            <input id="checker" name="userRole" class="form-check-input" type="radio" value="Checker">
                            <label class="form-check-label" for="checker">Checker</label>
                        </div>
                    </div>

                    <div class="d-grid gap-2">
                        <button id="login-btn" type="submit" class="btn btn-primary">Login</button>
                        <button id="signupBtn" type="button" class="btn btn-outline-secondary">Sign Up</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- jQuery, Bootstrap, Toastr Bundle -->
        <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

        <script>
            $(function() {
                $('#signupBtn').on('click', function() {
                    toastr.options = {
                        "closeButton": true,
                        "progressBar": true,
                        "positionClass": "toast-top-center",
                        "timeOut": "3000",
                        "extendedTimeOut": "1000"
                    };
                    toastr.info('Coming Soon!', 'Notice')
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
                        "timeOut": "3000",
                        "extendedTimeOut": "1000"
                    };
                    var type = "<c:out value='${sessionScope.flashType}'/>";
                    var msg = "<c:out value='${sessionScope.flashMessage}'/>";
                    if (type === "error") toastr.error(msg, 'Login Failed');
                    else toastr.success(msg, 'Success');
                });

            </script>
            <c:remove var="flashMessage" scope="session" />
            <c:remove var="flashType" scope="session" />
        </c:if>
    </body>

</html>
