document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/v1/users/me', {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch user details");
            }
            return response.json();
        })
        .then(user => {
            const welcomeElement = document.getElementById('welcome-user');
            welcomeElement.textContent = `Welcome, ${user.name}!`;
        })
        .catch(error => console.error("Error:", error));
});
