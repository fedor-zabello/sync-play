document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm');

    form.addEventListener('submit', function(event) {
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
            .then(response => {
                if (response.ok) {
                    // If registration is successful, redirect to the login page
                    window.location.href = '/login';
                } else {
                    return response.json().then(data => {
                        // Handle error
                        alert('Registration failed: ' + data.message);
                    });
                }
            })
            .catch(error => {
                console.error('Error during registration:', error);
            });
    });
});
