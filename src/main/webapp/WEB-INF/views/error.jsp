<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Error</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    </head>

    <body class="d-flex justify-content-center align-items-center vh-100 bg-light">
        <div class="text-center">
            <h1 class="display-4 text-danger mb-3">Oops!</h1>
            <p class="lead mb-3">
                Something went wrong.
            </p>
            <c:choose>
                <c:when test="${not empty requestScope['jakarta.servlet.error.status_code']}">
                    <p>Error Code: ${requestScope['jakarta.servlet.error.status_code']}</p>
                </c:when>
            </c:choose>
            <c:choose>
                <c:when test="${not empty requestScope['jakarta.servlet.error.message']}">
                    <p>Message: ${requestScope['jakarta.servlet.error.message']}</p>
                </c:when>
            </c:choose>
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit" class="btn btn-primary mt-3">Go to Login</a>
            </form>
        </div>
    </body>

</html>
