angular.module('endUserModule').controller('EndUserHomeCtrl', 
		function($scope, $rootScope, $location,$http){
		
	console.log('At EndUserHomeCtrl@endUserModule');
	console.log('User details : '+ $rootScope.user);

});
angular.module("endUserModule").controller("EndUserViewTrxnListCtrl",
	function($scope, $rootScope, $location,$http,EndUserDataService){
		console.log("Executing EndUserViewTrxnListCtrl@endUserModule");
		console.log('User details : ');
		console.log($rootScope.user);
		$scope.initInprogress=true;
		$scope.display_trxnlist=function(data){
			$scope.initInprogress=false;
			$scope.trxnList = data.payload;
			if($scope.trxnList.length>0){
				$scope.hasRecords = true;
			}
		};
		
		EndUserDataService.getAllTransactions($rootScope.user.ffId,$scope.display_trxnlist,$scope);
		
});
angular.module("endUserModule").controller("EndUserRegCtrl",
	function($scope, $rootScope, $location,$http,ReferenceDataFactory){
		console.log("Inside EndUserRegCtrl@endUserModule");
	
	$scope.register_user=function(){
		$scope.spinner=true;
		$http({
				method  : 'POST',
				url     : 'registerUser.wss',
				data    : $scope.user, 
				headers : {'Content-Type': 'application/json'} 
			})
			.then(function(response) {
			//Success callback
				console.log("Registation Response:"+ response.data);
				$scope.spinner=false;
				if(response.data.status == 0)
				{
					//Move to nw page
					var userDetails = response.data.payload;
					//TODO: Show an alert box here.
					alert("Your Loyalty Id is :"+ userDetails.ffId);
					$location.path("/login");
				}
				else
				{
					alert(response.data.statusText);
				}
			},function(errorResponse){
				//Error callback
				$scope.spinner=false;
				console.log("Login Response:"+ errorResponse);
			});
	}
});
angular.module("endUserModule").controller("EndUserViewPointsCtrl",
	function($scope, $rootScope, $location,$http,EndUserDataService){
		EndUserDataService.retrieve_points($rootScope.user.ffId,function(data){
			if(data.payload!=null){
				$scope.totalPoint = data.payload.totalPoint;
			}
			else{
				$scope.totalPoint="No data found"
			}
		},$scope);
});

