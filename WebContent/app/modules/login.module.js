var module = angular.module('loginModule',[]);
module.config(function($routeProvider,$httpProvider){
	$routeProvider.when('/logout', {
		template:'<div></div>',
		controller:'LogoutCtrl'
	}).when('/register',{
		templateUrl:"pages/end-user-registration.html"
	});
	
	
});

module.controller('LogoutCtrl',function($scope,$rootScope, $location){
	
	$rootScope.user = null;
	$rootScope.quote = null;
	$rootScope.application = null;
	$location.path("/");
	
});
//LOGIN CONTRL
module.controller('LoginCtrl', function($scope, $rootScope, $location,$http) {
	
		$rootScope.user = null;
		$scope.user = {};
		$scope.user.role='USER';
		$scope.invalidInput = false;

		$scope.regsiter=function(){
			$location.path("/register");
		}
		$scope.login_check=function()
		{
			console.log('Check login');
			$scope.validate_login();
			
		}
		$scope.validate_login=function(){
			$scope.spinner=true;
			$http({
				method  : 'POST',
				url     : 'login.wss',
				data    : $scope.user, 
				headers : {'Content-Type': 'application/json'} 
			})
			.then(function(response) {
			//Success callback
				console.log("Login Response:"+ response.data);
				$scope.spinner=false;
				if(response.data.status == 0)
				{
					//Move to nw page
					$rootScope.user = response.data.payload;
					if($scope.user.role=="USER")
					{
						$location.path("/end-user-home");
					}
					else if($scope.user.role=="AIR_LINES")
					{
						$location.path("/airlines-user-home");
					}
					else if($scope.user.role=="HOTEL"){
						$location.path("/hotel-end-user-home");
					}
					else
					{
						alert('Not implemented');
					}
				}
				else
				{
					alert(response.data.statusText);
				}
			},function(errorResponse){
				//Error callback
				console.log("Login Response:"+ errorResponse);
				console.log(errorResponse);
				alert("Server is not responding..");
				$scope.spinner=false;
			});
			        
		}
});
