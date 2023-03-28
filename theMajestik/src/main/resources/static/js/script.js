const form = document.querySelector('#register-form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const customer_name = form.elements['customer_name'].value;
    const email_id = form.elements['email_id'].value;
    const phone_number = form.elements['phone_number'].value;
    const address = form.elements['address'].value;
    const country = form.elements['country'].value;
    const password = form.elements['password'].value;
    const passport = form.elements['passport'].value;

    const data = {
        customer_name,
        email_id,
        phone_number,
        address,
        country,
        passport,
        password
        
        
    };

    fetch('app/v1/customers', {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
			console.log(response);
			localStorage.setItem("customerName",form.elements['customer_name'].value);
			console.log(form.elements['customer_name'].value)
            window.location.href = 'http://localhost:5000/success.html'
            form.reset();
        } else {
			console.log(response);
			window.location.href = 'http://localhost:5000/error.html',
            ('#error').html(response);
            
        }
    })
    .catch(error => {
        alert('An error occurred. Please try again later.');
        console.error(error);
    });
});
