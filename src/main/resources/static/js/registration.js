document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registrationForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // Prevent default form submission behavior

        // Collect form data
        const formData = {
            name: document.getElementById('name').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        };

        // Send the form data to the server
        fetch('/api/v1/users/registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.json()) // Parse JSON response
            .then(data => {
                if (data.message === "Registration successful") {
                    // If registration is successful, redirect to the login page
                    window.location.href = '/login';
                } else {
                    // Handle error and display the message in an alert or on the page
                    alert('Registration failed: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error during registration:', error);
                alert('Something went wrong. Please try again.');
            });
    });
});
