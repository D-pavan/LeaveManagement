// JavaScript code to append a new row to the table
function create(leave){
    const newRow = document.createElement('tr');
    newRow.classList.add('table-success');

    // Create and append table cells with content
    const cellsData = [
    
        leave['leaveType'],
        leave['fromDate'],
        leave['toDate'],
        leave['reason'],
        leave['appliedDate'],
        leave['status']
    ];

    for(let i=0;i<cellsData.length;i++){
        const cell = document.createElement('td');
        cell.textContent = cellsData[i];
        if(i==cellsData.length-1) cell.id='status'+leave['leaveId'];
        newRow.appendChild(cell);
    };
    const tableBody = document.querySelector('#my-container tbody');
    tableBody.appendChild(newRow);
}

function createTeamsLeave(leave) {
    // Create a new table row
    const newRow = document.createElement('tr');
    newRow.id='row'+leave['leaveId'];
    newRow.classList.add('table-success');
    
    // Create and append table cells with content
    const cellsData = [
        leave['name'],
        leave['leaveType'],
        leave['fromDate'],
        leave['toDate'],
        leave['reason'],
        leave['appliedDate'],
        leave['status']
    ];

    for(let i=0;i<cellsData.length;i++){
        const cell = document.createElement('td');
        cell.textContent = cellsData[i];
        if(i==cellsData.length-1) cell.id='status'+leave['leaveId'];
        newRow.appendChild(cell);
    };

    const acceptBtn=document.createElement('button');
    acceptBtn.id='accept'+leave['leaveId'];
    acceptBtn.className='btn btn-success m-1';
    acceptBtn.textContent='Accept';
    acceptBtn.onclick=function(){
        approveLeave(acceptBtn.id,false);
    }
    const rejectBtn=document.createElement('button');
    rejectBtn.id='reject'+leave['leaveId'];
    rejectBtn.className='btn btn-success m-1'
    rejectBtn.textContent='Reject';
    rejectBtn.onclick=function(){
        rejectLeave(rejectBtn.id);
    }
    
    const cell = document.createElement('td');
    cell.appendChild(acceptBtn);
    cell.appendChild(rejectBtn);
    newRow.appendChild(cell);
      
    const userData= document.createElement('td');
    userData.id = 'data'+leave['leaveId'];
    userData.setAttribute('data', JSON.stringify(leave));
    newRow.appendChild(userData);

    const tableBody = document.querySelector('#team-container tbody');
    tableBody.appendChild(newRow);
}
async function fetchMyLeaves(){
    const myLeaveContainer=document.querySelector('#my-container tbody');
    myLeaveContainer.innerHTML='';
    try{
        const response=await fetch('http://localhost:8081/LeaveManagement/MyLeaves',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        if(response.ok){
            const data=await response.json();
            const leaves=Object.values(data);
            leaves.forEach(leave=>{
                create(leave);
            });
        }
     }
     catch(err){
        console.log("error");
    }
    document.getElementById('myContainer').style.display='';
    document.getElementById('pendingBtn').style.display='';
    document.getElementById('rejectedBtn').style.display=''
    document.getElementById('allBtn').style.display=''
    document.getElementById('my-team').style.display='none';
    document.getElementById('my-team').className='';
    document.getElementById('teamContainer').style.display='none';
    document.getElementById('leavesContainer').style.display='none';
    document.getElementById('approvedLeaves').style.display='none';
    
    document.getElementById('leavesSummaryBtn').classList.remove('activeStatus');
    document.getElementById('leaveRequestsBtn').classList.add('activeStatus');


}
async function fetchTeamLeaves(){
    const teamLeaveContainer=document.querySelector('#team-container tbody');
    teamLeaveContainer.innerHTML='';
   try{
        const response=await fetch('http://localhost:8081/LeaveManagement/pendingLeaves',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        if(response.ok){
            const data=await response.json();
            const leaves=Object.values(data);
            leaves.forEach(leave=>{
                createTeamsLeave(leave);
            });
        }
     }
     catch(err){
        console.log("error",err);
    }

    document.getElementById('teamContainer').style.display='';
    document.getElementById('myContainer').style.display='none';
    document.getElementById('leavesContainer').style.display='none';
    document.getElementById('approvedLeaves').style.display='none';
    document.getElementById('my-team').style.display='none';
    document.getElementById('my-team').className='';

    document.getElementById('teamLeavesBtn').classList.add('activeStatus');
    document.getElementById('myTeamBtn').classList.remove('activeStatus');
    document.getElementById('onLeave').classList.remove('activeStatus');
    closeBtns();
}

function applyLeave() {
     document.getElementById('leave-form').style.display='block';
     validateGender(true);
}
function cancel(){
    document.getElementById('leave-form').style.display='none';
}
function changeLeaveCardDetails(){
    document.getElementById('cardLeaveType').innerText=document.getElementById('leaveType').value;
    document.getElementById('availableLeaves').innerText=2;
    document.getElementById('leavesTaken').innerText=5;
}
async function submitForm(){
    console.log('form submit');
    const leave={
            "employeeId" : -1,
            "fromDate": document.getElementById('fromDate').value,
            "reason": document.getElementById('comment').value,
            "leaveType": document.getElementById('leaveType').value,
            "toDate": document.getElementById('toDate').value,
            "appliedDate": formatDateToYYYYMMDD(new Date()),
            "status": "pending",
            "leaveId":-1
    };
    try{
       const response=await fetch('http://localhost:8081/LeaveManagement/MyLeaves',{
                 method:'POST',
                 headers: {
                     'Content-Type': 'application/json'
                 },
                 body:JSON.stringify(leave)
       });
       if(response.ok){
           const leave=await response.json();
           create(leave);
       }
    }
    catch(error){
           console.log('error',error)
    }
    document.getElementById('leave-form').style.display='none';
}
let isEnabled=false;
function enableProfileCard(){
    if(!isEnabled){
        document.getElementById('profile-card').style.display='flex';
        isEnabled=true;
    }
    else{
        document.getElementById('profile-card').style.display='none';
        isEnabled=false;
    }
}

async function logOut(){
    console.log('logout');

   try{
      const response=await fetch('http://localhost:8081/LeaveManagement/logout',{
                method:'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
      });
      if(response.ok){
          const message=await response.json();
          if(message['message']==='logged out'){
              window.location.href = 'http://localhost:8081/LeaveManagement/index.html';
          }
          else{
              alert('Something went wrong');
          }
      }
   }
   catch(error){
          console.log('error',error)
   }
}
function formatDateToYYYYMMDD(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
async function rejectLeave(leaveId){
       try{
        const json={
            "leaveId":leaveId.substring(6),
            "status":"Rejected"
        };
        const response = await fetch('http://localhost:8081/LeaveManagement/TeamLeaves',{
            method:'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify(json)
        })
        if(response.ok){
            document.getElementById('status'+json['leaveId']).innerText=json['status'];
        }
        else{
            alert('something went wrong');
        }
    }
    catch(err){
        console.log(err);
    }
    const row = document.getElementById('row'+leaveId.substring(6));
    if (row) {
        const cells = row.querySelectorAll('td');
        if (cells.length >= 7) {
            row.removeChild(cells[6]);
        }
    }
    const approvedContainer = document.querySelector('#approved-container tbody');
    approvedContainer.appendChild(row);
    row.parentNode.removeChild(row);
}

async function approveLeave(leaveId,isConfirmed){
    console.log(leaveId);
    if(!isConfirmed){
    const userCard = document.getElementById('userCard');
    const cellData = document.getElementById('data'+leaveId.substring(6))
    const userData = JSON.parse(cellData.getAttribute('data'));
    userCard.innerHTML = `
        <h4 class="text-center text-white">Employee Details</h4>
         <p class="text-white"><b>Name  : </b> ${userData.name} </p>
         <p class="text-white"><b>Email : </b>${userData.email}</p>
         <p class="text-white"><b>Date Of Birth : </b> ${userData.dateOfBirth}</p>
         <p class="text-white"><b>Phone Number: </b>${userData.phoneNumber}</p>
         <p class="text-white"><b>Leave Type : </b>${userData.leaveType}</p>
         <p class="text-white"><b>Leaves Taken : </b>${userData.leavesTaken}</p>
         <p class="text-white"><b>Available Leaves: </b>${userData.availableLeaves}</p>
         <button class="btn btn-success float-end" onclick="closeUserCard()">Close</button>
         <button class="btn btn-success float-end mx-1 " onclick="confirmLeave(${userData.leaveId})">Confirm</button>
    `;
     userCard.style.display='block';
    }
    else{
      
        const cellData = document.getElementById('data'+leaveId);
        const userData = JSON.parse(cellData.getAttribute('data'));
        const from = new Date(userData.fromDate);
        const to = new Date(userData.toDate);
        console.log(to-from);
        if(to-from<=userData.availableLeaves){
                try{
                    const json={
                        "leaveId":leaveId,
                        "status":"Approved"
                    };
                
                        const response = await fetch('http://localhost:8081/LeaveManagement/TeamLeaves',{
                            method:'PUT',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body:JSON.stringify(json)
                        })
                        if(response.ok){
                            document.getElementById('status'+json['leaveId']).innerText=json['status'];
                        }
                        else{
                            alert('something went wrong');
                        }
                }    
                catch(err){
                    console.log(err);
                }
                console.log(leaveId);
                const row = document.getElementById('row'+leaveId);
                if (row) {
                    const cells = row.querySelectorAll('td');
                    if (cells.length >= 7) {
                        row.removeChild(cells[6]);
                    }
                }
                const approvedOrRejectedContainer = document.querySelector('#approved-container tbody');
                approvedOrRejectedContainer.appendChild(row);
                row.parentNode.removeChild(row);
        } 
        else{
            document.getElementById('msg').innerText='Leaves Insufficient!!';
            document.getElementById('messageDialogue').style.display='flex';

        }
    }

}
function ok(){
    document.getElementById('messageDialogue').style.display='none';
}
function createLeaves(leave){
    
        const newRow = document.createElement('tr');
        newRow.classList.add('table-success');
    
        // Create and append table cells with content
        const cellsData = [
            leave['name'],
            leave['leaveType'],
            leave['fromDate'],
            leave['toDate'],
            leave['reason'],
            leave['appliedDate'],
            leave['status']
        ];
    
        for(let i=0;i<cellsData.length;i++){
            const cell = document.createElement('td');
            cell.textContent = cellsData[i];
            if(i==cellsData.length-1) cell.id='status'+leave['leaveId'];
            newRow.appendChild(cell);
        };
        const tableBody = document.querySelector('#approved-container tbody');
        tableBody.appendChild(newRow);
    
    
}
async function fetchApprovedOrRejectedLeaves(){
 
    const teamLeaveContainer=document.querySelector('#approved-container tbody');
    teamLeaveContainer.innerHTML='';
   try{
        const response=await fetch('http://localhost:8081/LeaveManagement/TeamLeaves',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        if(response.ok){
            const data=await response.json();
            const leaves=Object.values(data);
            leaves.forEach(leave=>{
                createLeaves(leave);
            });
        }
     }
     catch(err){
        console.log("error",err);
    }

    document.getElementById('teamContainer').style.display='none';
    document.getElementById('myContainer').style.display='none';
    document.getElementById('leavesContainer').style.display='none';
    document.getElementById('approvedLeaves').style.display='';
    document.getElementById('my-team').style.display='none';
    document.getElementById('my-team').className='';

    document.getElementById('teamLeavesBtn').classList.remove('activeStatus');
    document.getElementById('myTeamBtn').classList.remove('activeStatus');
    document.getElementById('onLeave').classList.add('activeStatus');
    closeBtns();
}

async function fetchLeavesSummary() {
   try{
        let response = await fetch('http://localhost:8081/LeaveManagement/annualLeaves',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        const annualLeaves = await response.json();
        
        response = await fetch('http://localhost:8081/LeaveManagement/appliedLeaves',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const appliedLeaves= await response.json();
        document.getElementById('leavesContainer').style.display = '';
        document.getElementById('teamContainer').style.display='none';
        document.getElementById('myContainer').style.display='none';
        document.getElementById('approvedLeaves').style.display = 'none';
        document.getElementById('my-team').style.display = 'none';
        document.getElementById('my-team').className='';
  
      
        closeBtns();
        createAppliedLeavesCard(annualLeaves,appliedLeaves);
        validateGender(false);
       // fetchUpcomingLeaves();
    }
    catch(err){
        console.log("Error while fetching annual leaves ",err);
    }
    document.getElementById('leavesSummaryBtn').classList.add('activeStatus');
    document.getElementById('leaveRequestsBtn').classList.remove('activeStatus');
   
}
function createAppliedLeavesCard(annualLeaves,appliedLeaves){
    const container = document.getElementById('leaveCards');
    container.innerHTML=` 
        <div>
            <h5 class="text-center">Sick</h5>
            <br>
            <h6>Available : <span class="float-end text-bold">${annualLeaves.sick-appliedLeaves.sick}</span></h6>
            <h6>Booked : <span class="float-end text-bold">${appliedLeaves.sick} </span></h6>
        </div>
        <div id="maternityCard">
            <h5 class="text-center">Maternity</h5>
            <br>
            <h6>Available : <span class="float-end text-bold">${annualLeaves.maternity-appliedLeaves.maternity}</span> </h6>
            <h6>Booked :<span class="float-end text-bold"> ${appliedLeaves.maternity}</span></h6>
        </div>
        <div id="paternityCard">
            <h5 class="text-center">Paternity</h5><br>
            <h6>Available : <span class="float-end text-bold">${annualLeaves.paternity-appliedLeaves.paternity} </span></h6>
            <h6>Booked : <span class="float-end text-bold">${appliedLeaves.maternity}</span></h6>
        </div>
        <div>
            <h5 class="text-center">Loss of Pay</h5><br>
            <h6>Available : <span class="float-end text-bold">${annualLeaves.lossOfPay-appliedLeaves.lossOfPay}</span></h6>
            <h6>Booked : <span class="float-end text-bold">${appliedLeaves.lossOfPay}</span></h6>
        </div>
        <div>
            <h5 class="text-center ">Personal Time Off</h5> <br>
            <h6>Available : <span class="float-end text-bold">${annualLeaves.personalTimeOff-appliedLeaves.personalTimeOff}</span></h6>
            <h6>Booked : <span class="float-end text-bold">${appliedLeaves.personalTimeOff}</span></h6>
        </div>`;

}
async function fetchProfileDetails(){
    try{
        const response = await fetch('http://localhost:8081/LeaveManagement/employee',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();
        document.getElementById('userImage').innerText=data['employeeName'].substring(0,1);
        document.getElementById('userName').innerText = data['employeeName'];
        document.getElementById('userEmail').innerText = data['email'].toLowerCase();
        document.getElementById('gender').innerHTML = "<b>Gender  : </b>"+data['gender'];
        document.getElementById('gender').setAttribute('gender',data['gender']);
        if(data['managerName']!=null) document.getElementById('userManger').innerHTML="<b>Manager : </b>"+data['managerName'];
    }
    catch(err){
        console.log(err);
    }
}
async function displayUserCard(){
    const card = document.getElementById('userCard');
    const response = await fetch('http://localhost:8081/LeaveManagement/employee',{
        method:'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    const data = await response.json();
    card.innerHTML = `
    `;
}

function closeUserCard(){
    document.getElementById('userCard').style.display = 'none';
}
async function confirmLeave(leaveId){
    await approveLeave(leaveId,true);
    closeUserCard();
}
async function myData(){
   document.getElementById('myTeamBtn').style.display ='none';
   document.getElementById('teamLeavesBtn').style.display='none';
   document.getElementById('leaveRequestsBtn').style.display='';
   document.getElementById('leaveRequestsBtn').classList.add('activeStatus');
   document.getElementById('leavesSummaryBtn').style.display='';
   document.getElementById('onLeave').style.display='none';
   document.getElementById('myData').classList.add('activeStatus');
   document.getElementById('teams').classList.remove('activeStatus');
   await fetchMyLeaves();
}
async function teams(){
    document.getElementById('leaveRequestsBtn').style.display='none';
    document.getElementById('leavesSummaryBtn').style.display='none';
    document.getElementById('myTeamBtn').style.display ='';
    document.getElementById('teamLeavesBtn').style.display='';
    document.getElementById('teamLeavesBtn').classList.add('activeStatus');
    document.getElementById('onLeave').style.display='';
    document.getElementById('teams').classList.add('activeStatus');
    document.getElementById('myData').classList.remove('activeStatus');
    await fetchTeamLeaves();
    closeBtns();
}
function closeBtns(){
    document.getElementById('pendingBtn').style.display='none';
    document.getElementById('rejectedBtn').style.display='none'
    document.getElementById('allBtn').style.display='none';
}
function validateGender(isApplying){
    const gender = document.getElementById('gender').getAttribute('gender');
    if(gender.includes("male")){
        if(isApplying) document.getElementById('maternity').style.display='none';
        else document.getElementById('maternityCard').style.display='none';
    }
    else{
       if(isApplying) document.getElementById('paternity').style.display='none';
       else document.getElementById('paternityCard').style.display='none';
    
    }
}
function fetchUpcomingLeaves(){
    const  upCommingLeavesTbody= document.querySelector('#upcomingLeaves tbody');
    const leavesTableBody = document.querySelector('#my-container tbody');
    const rows = Array.from(leavesTableBody.querySelectorAll('tr'));
    const approvedRows = rows.filter(row => row.cells[5].textContent.trim() === 'Approved');
    approvedRows.sort((rowA, rowB) => {
        const fromDateA = new Date(rowA.cells[1].textContent.trim());
        const fromDateB = new Date(rowB.cells[1].textContent.trim());
        return fromDateA - fromDateB;
    });
    approvedRows.forEach(row => upCommingLeavesTbody.appendChild(row)); 
}
async function filterRejected(){
    const myLeaveContainer=document.querySelector('#my-container tbody');
    myLeaveContainer.innerHTML='';
    try{
        const response=await fetch('http://localhost:8081/LeaveManagement/MyLeaves?status='+'rejected',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        if(response.ok){
            const data=await response.json();
            const leaves=Object.values(data);
            leaves.forEach(leave=>{
                create(leave);
            });
        }
     }
     catch(err){
        console.log("error");
    }
    document.getElementById('myContainer').style.display='';


}
async function filterPending(){
    const myLeaveContainer=document.querySelector('#my-container tbody');
    myLeaveContainer.innerHTML='';
    try{
        const response=await fetch('http://localhost:8081/LeaveManagement/MyLeaves?status='+'pending',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        if(response.ok){
            const data=await response.json();
            const leaves=Object.values(data);
            leaves.forEach(leave=>{
                create(leave);
            });
        }
     }
     catch(err){
        console.log("error");
    }
    document.getElementById('teamContainer').style.display='none';
    document.getElementById('myContainer').style.display='';
    document.getElementById('leavesContainer').style.display='none';
    document.getElementById('approvedLeaves').style.display='none';
}

async function fetchMyTeam(){
    const myTeam = document.getElementById('my-team');
    myTeam.className='d-sm-flex flex-sm-column flex-md-row myTeam flex-wrap';
    myTeam.innerHTML = '';
    try{
        const response=await fetch('http://localhost:8081/LeaveManagement/teams',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        if(response.ok){
            const data=await response.json();
            const employees=Object.values(data);
            employees.forEach(employee=>{
                createTeamCards(employee);
            });
        }
     }
     catch(err){
        console.log("error",err);
    }
    document.getElementById('my-team').style.display = '';
    document.getElementById('teamContainer').style.display='none';
    document.getElementById('myContainer').style.display='none';
    document.getElementById('leavesContainer').style.display='none';
    document.getElementById('approvedLeaves').style.display='none';

    document.getElementById('teamLeavesBtn').classList.remove('activeStatus');
    document.getElementById('myTeamBtn').classList.add('activeStatus');
    document.getElementById('onLeave').classList.remove('activeStatus');
}
function createTeamCards(employee){
  const myTeam = document.getElementById('my-team');
  const innerDiv = document.createElement('div');
  innerDiv.className='w-sm-100 employeeCard';
  innerDiv.innerHTML = `
               <div class="d-flex">
                   <div id="empImage">
                      <h1 class="text-white text-center" id="emp-logo" >${employee.employeeName.substring(0,1)}</h1>
                   </div>
                    <div class="d-flex flex-column mx-2">
                        <h4>${employee.employeeName}</h4>
                        <p>${employee.email}</p>
                        <p>${employee.phoneNumber}</p>
                        <p><b>Gender : </b>${employee.gender}</p>
                    </div> 
              </div>
           `;
  myTeam.appendChild(innerDiv);
}


document.addEventListener('DOMContentLoaded',async ()=>{
    document.getElementById('messageDialogue').style.display ='none';
    const today = formatDateToYYYYMMDD(new Date());
    document.getElementById('fromDate').min = today;
    document.getElementById('toDate').min = today;
    await myData();
    await fetchProfileDetails();
    document.getElementById('userCard').style.display = 'none';
});

