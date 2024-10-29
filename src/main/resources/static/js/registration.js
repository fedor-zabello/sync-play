document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registrationForm');

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
                    throw new Error('Failed to register.'); // или верните сообщение, если нужно
                }
                return response.json(); // только если успешный ответ
            })
            .then(data => {
                if (data.message === "Registration successful") {
                    window.location.href = '/login';
                } else {
                    alert('Registration failed: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error during registration:', error);
                alert('Something went wrong. Please try again.');
            });
    });
});
