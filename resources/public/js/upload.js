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
        var selectedCheckboxes = [];

        if (data.length > 0) {
          data.forEach(function(log) {
            var row = document.createElement('tr');
            if (log.error) {
              row.style.backgroundColor = 'red';
            }
            row.innerHTML = '<td><input type="checkbox" class="row-selector"></td>' +
                            '<td>' + log.timestamp + '</td>' +
                            '<td>' + log.owner + '</td>' +
                            '<td>' + log.message + '</td>';
            tableBody.appendChild(row);
          });

          Array.from(tableBody.getElementsByTagName('tr')).forEach(function(row) {
            row.addEventListener('click', function() {
              if (this.style.backgroundColor === 'yellow') {
                this.style.backgroundColor = '';
                if (this.hasAttribute('data-error')) {
                  this.style.backgroundColor = 'red'; 
                }
              } else {
                this.style.backgroundColor = 'yellow';
              }
            });
          });

          Array.from(document.getElementsByClassName('row-selector')).forEach(function(checkbox, index) {
            checkbox.addEventListener('change', function() {
              if (checkbox.checked) {
                selectedCheckboxes.push(index);
                if (selectedCheckboxes.length > 2) {
                  var first = selectedCheckboxes.shift(); 
                  document.getElementsByClassName('row-selector')[first].checked = false;
                }
              } else {
                selectedCheckboxes = selectedCheckboxes.filter(i => i !== index);
              }

              var rows = tableBody.getElementsByTagName('tr');
              Array.from(rows).forEach(function(row, i) {
                if (selectedCheckboxes.length === 2 && i >= Math.min(...selectedCheckboxes) && i <= Math.max(...selectedCheckboxes)) {
                  row.style.backgroundColor = 'lightgray';
                } else {
                  if (!row.hasAttribute('data-error')) {
                    row.style.backgroundColor = '';
                  } else {
                    row.style.backgroundColor = 'red';
                  }
                }
              });
            });
          });
          var sendLogsElement = document.querySelector('.sendLogs');
            if (sendLogsElement) {
            sendLogsElement.style.display = 'block';
          }
        } else {
          var row = document.createElement('tr');
          row.innerHTML = '<td colspan="4">No data available</td>';
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
document.getElementById('file-upload').addEventListener('change', function(event) {
  var fileName = event.target.files.length > 0 ? event.target.files[0].name : "No file chosen";
  document.getElementById('file-name').textContent = fileName;
});

document.getElementById('custom-button').addEventListener('click', function() {
    var email = document.getElementById('custom-textbox').value;
    var selectedLogs = [];
    var rows = document.querySelectorAll('#log-table-body tr');
    rows.forEach(function(row) {
      var checkbox = row.querySelector('input[type=\"checkbox\"]');
      if (checkbox && checkbox.checked) {
        var timestamp = row.cells[1].innerText;
        var owner = row.cells[2].innerText;
        var message = row.cells[3].innerText;
        selectedLogs.push('Timestamp: ' + timestamp + '\\nOwner: ' + owner + '\\nMessage: ' + message);
      }
    });

    var logContent = selectedLogs.join('\\n\\n');
      if (email && logContent) {
      var formData = new FormData();

      formData.append('email', email);
      formData.append('logContent', logContent);
      var xhr = new XMLHttpRequest();
      xhr.open('POST', '/send-email', true);
      xhr.onload = function() {
        if (xhr.status === 200) {
          alert('Email sent successfully!');
        } else {
          alert('Failed to send email.');
        }
      };
      xhr.send(formData);
    } else {
      alert('Please enter an email and select logs to share.');
    }
  });