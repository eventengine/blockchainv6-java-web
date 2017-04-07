var module= angular.module("adminModule",[])
module.config(function($routeProvider,$httpProvider){
	$routeProvider.when('/adminViewUser', {
		
		templateUrl:"pages/admin-view-user.html"
	});
	
	
});

module.service('AdminDataService', 
function($http) {
	this.addPoints=function($scope,callbackMethod){
		console.log("Inside addPoints");
		   $http({
				method  : 'GET',
				url     : 'addLoyaltityPointsAdmin.wss',
				params  : {
							'ffid':$scope.ffid,'points':$scope.points,
							'businessEntity':$scope.businessEntity
						 },
				headers : {'Content-Type': 'application/json'} 
			})
			.then(function(response) {
			//Success callback
				console.log("Registation Response:"+ response.data);
				if(response.data.status == 0)
				{
					callbackMethod(response.data);
				}
				else
				{
					alert(response.data.statusText);
				}
			},function(errorResponse){
				//Error callback
				console.log("getAllUsersAdmin Response:"+ errorResponse);
                alert(errorResponse);
			});    //End of $http
	};
    this.getAllUsers = function(callbackMethod) {
		console.log("Inside getAll Users");
    $http({
				method  : 'POST',
				url     : 'getAllUsersAdmin.wss',
				headers : {'Content-Type': 'application/json'} 
			})
			.then(function(response) {
			//Success callback
				console.log("Registation Response:"+ response.data);
				if(response.data.status == 0)
				{
					callbackMethod(response.data);
				}
				else
				{
					alert(response.data.statusText);
				}
			},function(errorResponse){
				//Error callback
				console.log("getAllUsersAdmin Response:"+ errorResponse);
                alert(errorResponse);
			});    //End of $http
    }
});
