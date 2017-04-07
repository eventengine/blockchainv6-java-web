angular
    .module('airtlinesEndUserModule')
    .service('AirlinesDataService', function ($http) {
	this.getBookingsByUser = function(userId,callbackMethod,$scope){
        console.log("Inside@ getBookingsByUser@AirlinesDataService@airtlinesEndUserModule");
        console.log("Input received"+userId);
       
		$scope.spinner=true
        $http({
			method  : 'POST',
			url     : 'viewAirlineBookingsUser.wss',
			params	: {'userId': userId}
		})
		.then(function(response) {
			//Success callback
			$scope.spinner=false
			console.log("Success getBookingsByUser:");
			console.log(response.data);
			if(response.data.status == 0)
			{
				//callback(response.data.payload);
                callbackMethod(response.data)
			}
			else
			{
				alert(response.data.statusText);
				
			}
		},function(errorResponse){
			$scope.spinner=false
			alert("Error response for getBookingsByUser:"+ errorResponse);
		});
    }	
    this.makePayment = function(paymentInfo,callbackMethod,$scope){
        console.log("Inside@ makePayment@AirlinesDataService@airtlinesEndUserModule");
        console.log("Input received");
        console.log(paymentInfo);
		$scope.spinner=true
        $http({
			method  : 'POST',
			url     : 'makePaymentAirlines.wss',
			data	: paymentInfo
		})
		.then(function(response) {
			//Success callback
			$scope.spinner=false
			console.log("Success makePaymentAirlines:");
			console.log(response.data);
			if(response.data.status == 0)
			{
				//callback(response.data.payload);
                callbackMethod(response.data)
			}
			else
			{
				alert(response.data.statusText);
				
			}
		},function(errorResponse){
			$scope.spinner=false
			alert("Error response for makePaymentAirlines:"+ errorResponse);
		});
    }
});

