
document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const username = document.getElementById('email').value;
    const password = document.getElementById('pwd').value;

    const data = {
        "emailId": username,
        "password": password
    };

    try{
        const response= await fetch('http://localhost:8081/LeaveManagement/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            if(response.ok){
                const data=await response.json();
                console.log(data['message']);
                if(data['message']==="true"){
                    localStorage.setItem('userName',data['name'])
                    window.location.href = 'http://localhost:8081/LeaveManagement/dashboard.html';
                }
                else{
                    document.getElementById('message').innerText='Invalid Credentials';
                }
            }

    }
    catch(error) {
        console.error('Error:', error);
        alert('An error occurred while processing your request');
    }
});

