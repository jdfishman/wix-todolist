$(document).ready(function() {
	
	$('#addItem').on('click', function() {
		description = $('#add_desciption').val();
		$('#add_desciption').val('');
		$.ajax('/addItem', {
			method: 'POST',
			data: {'description': description},
			success: function(data) {
				if (data) {
					alert('Item Added. ID: ' + data);
				} else {
					alert('Item not Added');
				}
			},
			error: function() {
				alert('Request Error.');
			}
		});
	});
	
	$('#deleteItem').on('click', function() {
		id = $('#delete_id').val();
		$('#delete_id').val('');
		$.ajax('/deleteItem', {
			method: 'POST',
			data: {'id': id},
			success: function(data) {
				if (data) {
					alert('Item Deleted: ' + data);
				} else {
					alert('Item not found.');
				}
			},
			error: function() {
				alert('Request Error.');
			}
		});
	});
	
	$('#getAllItems').on('click', function() {
		$.ajax('/getAllItems', {
			method: 'GET',
			success: function(data) {
				str = 'Items:\n';
				for (index in data) {
					str += data[index] + '\n';
				}
				alert(str);
			},
			error: function() {
				alert('Request Error.');
			}
		});
	});
	
	$('#showHistory').on('click', function() {
		$.ajax('/showHistory', {
			method: 'GET',
			success: function(data) {
				str = 'History:\n';
				for (index in data) {
					str += data[index] + '\n';
				}
				alert(str);
			},
			error: function() {
				alert('Request Error.');
			}
		});
	});
});