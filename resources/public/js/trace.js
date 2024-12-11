
document.getElementById('trace-btn').addEventListener('click', function() {
    var logType = document.getElementById('log-type').value;
    var startDate = document.getElementById('start-date').value;
    var endDate = document.getElementById('end-date').value;

    fetch('/trace-logs', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            logType: logType,
            startDate: startDate,
            endDate: endDate
        })
    })
    .then(response => response.json())
    .then(data => {
        var tableBody = document.getElementById('event-table-body');
        tableBody.innerHTML = '';  
        
        data.logs.forEach(function(log) {
            var row = document.createElement('tr');
            row.innerHTML = `
                <td>${log.level}</td>
                <td>${log.dateTime}</td>
                <td>${log.source}</td>
                <td>${log.eventId}</td>
                <td>${log.taskCategory}</td>
            `;
            tableBody.appendChild(row);
        });
    })
    .catch(error => console.error('Error:', error));
});