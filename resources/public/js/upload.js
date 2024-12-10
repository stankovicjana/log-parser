document.getElementById('upload-form').onsubmit = function(event) {
    event.preventDefault();
    var formData = new FormData(document.getElementById('upload-form'));
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/upload', true);
    xhr.onload = function () {
      if (xhr.status === 200) {
        console.log(xhr.responseText); 
        try {
          var data = JSON.parse(xhr.responseText);  
          console.log(data);
          var tableBody = document.getElementById('log-table-body');
          tableBody.innerHTML = '';  
          if (data.length > 0) {
            data.forEach(function(log) {
              var row = document.createElement('tr');
              if (log.error) {  
                row.style.backgroundColor = 'red';
              }
              row.innerHTML = '<td>' + log.timestamp + '</td><td>' + log.owner + '</td><td>' + log.message + '</td>';
              tableBody.appendChild(row);
            });
            Array.from(tableBody.getElementsByTagName('tr')).forEach(function(row) {
              row.addEventListener('click', function() {
                if (this.style.backgroundColor === 'yellow') {
                  this.style.backgroundColor = '';
                } else {
                  this.style.backgroundColor = 'yellow';
                }
              });
            });
          } else {
            var row = document.createElement('tr');
            row.innerHTML = '<td colspan="3">No data available</td>';
            tableBody.appendChild(row);
          }
        } catch (e) {
          alert('Error parsing response: ' + e.message);
        }
      } else {
        alert('An error occurred while uploading the file.');
      }
    };
    xhr.send(formData);
  };
  