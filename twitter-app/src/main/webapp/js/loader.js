/**
 * 
 */

		$(document).ready(function() {
			$('#accountList').empty();
			loadMainAccounts();
		});
	
		$('#save').click(function() {
			addAccount();
		});
		
		$('#reset').click(function() {
			deleteAll();
		});
		
		$('#accountList').click(function(event) {
			$('#followerList').empty();
			loadFollowerAccounts(event.target.id);
		});
		
		$('#followerList').click(function(event) {
			//clean 
			$('#detail').empty();
			loadDetail(event.target.id);
		});		
		
		//function to load main accounts
		function loadMainAccounts() {
			//clean
			$('#accountList').empty();
			$.ajax({
				url:'./twitterAccount',
				type:'GET',
				success:function(data) {
					//loop and add to the accountlist
					$.each(data,function(index,obj){
						$('#accountList').append('<li class="list-group-item" id="' + obj.id + '"><a class="btn ' + obj.username + '</li>');
					});
				},
				error:function(errMsg) {
					console.log("couldn't load ");
					console.log(errMsg);
				}
				
			});
		}
		
		function loadFollowerAccounts(id) {
			$.ajax({
				url:'./twitterAccount/' + id + '/followers',
				type:'GET',
				success:function(data) {
					//loop and add to the accountlist
					$.each(data,function(index,obj){
						$('#followerList').append('<li class="list-group-item" id="' + obj.twitterId + '">' + obj.username + '</li>');
					});
				},
				error:function(errMsg) {
					console.log(errMsg);
				}
			});
		}		
		
		function loadDetail(id) {
			$.ajax({
				url:'./twitterAccount/' + id + '/detail',
				type:'GET',
				contentType:'application/json',
				success:function(data) {
					//display the details
					$('#detail').append('<h3>Name: ' + data.name + '</h3>');
					$('#detail').append('<h4>Follower Count ' + data.followerCount + '</h4>');
				},
				error:function(errMsg) {
					console.log(errMsg);
				}
			});
		}
		
		
		function addAccount() {
			
			var formData = new Object;
			formData['username'] = $('#newTwitterScreenName').val();
			
			//build the object
			$.ajax({
				url:'./twitterAccount',
				type:'PUT',
				contentType:'application/json',
				data:JSON.stringify(formData),
				success:function(data) {
					$('#addTwitterModal').modal('hide');
					//refresh
					loadMainAccounts();
					//clean up
					$('#newTwitterScreenName').val('');
					//launch load of the follower
				},
				error:function(errMsg) {
					console.log('error');
					console.log(errMsg);
				}
			});
		}
		
		function deleteAll() {
			$.ajax({
				url:'./twitterAccount',
				type:'DELETE',
				success:function(data) {
					//refresh
					loadMainAccounts();
				}
			})
		}