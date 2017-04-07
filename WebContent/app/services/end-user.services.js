angular.module('endUserModule').service('EndUserDataService', function ($http,$rootScope) {

        this.getAllTransactions=function(ffid,callbackMethod,$scope){

        console.log("Inside getAllTransactions");
			$scope.spinner=true
		   $http({
				method  : 'GET',
				url     : 'getAllTransactionUser.wss',
				params  : {
							'ffid':ffid
						 },
				headers : {'Content-Type': 'application/json'} 
			})
			.then(function(response) {
				//Success callback
				$scope.spinner=false
				console.log("getAllTransactions Response:");
                console.log( response.data);
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
				console.log("getAllTransactions Response:"+ errorResponse);
				$scope.spinner=false
                alert(errorResponse);
			});    //End of $http
        }
		this.retrieve_points=function(ffid,callbackMethod,$scope){
            $scope.spinner=true
            $http({
                method  : 'GET',
                url     : 'getLoyalityPointForHotel.wss',
                params	: {'ffid':ffid }
            })
            .then(function(response) {
                //Success callback
                $scope.spinner=false
                console.log("Success retrieve_points:");
                console.log(response.data);
                if(response.data.status == 0)
                {
                  
                    callbackMethod(response.data)
                }
                else
                {   $scope.spinner=false
                    alert(response.data.statusText);
                    
                }
            },function(errorResponse){
                $scope.spinner=false
                alert("Error response for retrieve_points:"+ errorResponse);
            }); //End of $http

        
        }//End of retrieve_points
});

