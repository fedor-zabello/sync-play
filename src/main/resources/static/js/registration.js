document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registrationForm');
    const messageContainer = document.getElementById('messageContainer');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = {
            name: document.getElementById('name').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        };

        fetch('/api/v1/users/registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.error || 'Failed to register.');
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.message === "Registration successful") {
                    displayMessage('Registration successful! Redirecting to login...');
                    setTimeout(() => {
                        window.location.href = '/login';
                    }, 1500);
                } else {
                    displayMessage('Registration failed: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error during registration:', error);
                displayMessage(error.message || 'Something went wrong. Please try again.');
            });
    });

    function displayMessage(message) {
        if (!messageContainer) {
            console.warn("Message container not found on the page.");
            alert(message);
            return;
        }

        messageContainer.textContent = message;
        messageContainer.style.display = 'block';
    }

    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('input', () => {
            messageContainer.style.display = 'none';
        });
    });
});
