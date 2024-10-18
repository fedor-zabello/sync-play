$(document).ready(function() {
    $('#registrationForm').on('submit', function(event) {
        event.preventDefault(); // Prevent default form submission

        const userData = {
            name: $('#name').val(),
            email: $('#email').val(),
            password: $('#password').val()
        };

        $.ajax({
            type: 'POST',
            url: '/auth/registration', // Endpoint for registration
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function(response) {
                // Redirect to login on success
                window.location.href = '/login';
            },
            error: function(xhr, status, error) {
                alert('Registration failed: ' + xhr.responseText);
            }
        });
    });
});
