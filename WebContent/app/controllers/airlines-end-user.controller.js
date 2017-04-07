angular.module('airtlinesEndUserModule').controller('AirlinesEndUserHomeCtrl', 
		function($scope, $rootScope, $location,$http){
		
	console.log('At EndUserHomeCtrl');
	$scope.open_link=function(link){
		$location.path(link);
	};
});

angular.module('airtlinesEndUserModule').controller('AirlinesPaymentConfCtrl', 
		function($scope, $rootScope){
				console.log('Starting AirlinesPaymentConfCtrl@airtlinesEndUserModule');
				$scope.paymentConf=$rootScope.paymentConf;
});
angular.module('airtlinesEndUserModule').controller('AirlinesEndUserBookFlightCtrl', 
		function($scope, $rootScope, $location,$http,ReferenceDataFactory){
	$scope.searchResultsMode=false;
	$scope.refData={};
	$scope.itenary = {};
	$scope.flightList = new Array();
	$scope.refData.airportList = ReferenceDataFactory.getAirportList();
	$scope.refData.passengerCount = ReferenceDataFactory.getPassengerCount();
	
	//Initialize the form --START
	$scope.itenary.originAirportOpt=$scope.refData.airportList[0];
	$scope.itenary.destAirportOpt=$scope.refData.airportList[1] ;
	$scope.itenary.adultPassengerCountOpt=$scope.refData.passengerCount[0]; 
	$scope.itenary.travelDate="12/04/2017";
	
	//Initialize the form --END
	
	console.log($scope.refData.airportList)
	console.log('At AirlinesEndUserBookFlightCtrl');
	
	$scope.search_flights=function(){
		//Return a list of flights 
		$scope.searchResultsMode=false;
		$scope.sanitizeBeforeSubmit();
		$http({
			method  : 'POST',
			url     : 'searchFlights.wss',
			data	: $scope.itenary
		})
		.then(function(response) {
			//Success callback
			console.log("Success searchFlights:");
			console.log(response.data);
			if(response.data.status == 0)
			{
				//callback(response.data.payload);
				$scope.flightList=response.data.payload;
				$scope.searchResultsMode=true;
			}
			else
			{
				alert(response.data.statusText);
				
			}
		},function(errorResponse){
			
			alert("Error response:"+ errorResponse);
		});
	}
	$scope.sanitizeBeforeSubmit=function(){
		$scope.itenary.originAirport = $scope.itenary.originAirportOpt.code ;
		$scope.itenary.destAirport = $scope.itenary.destAirportOpt.code ;
		$scope.itenary.adultPassengerCount = $scope.itenary.adultPassengerCountOpt.code ;
		
	}
	$scope.goto_booking=function(itinerary){
		console.log(itinerary);
		$rootScope.selectedItinerary = itinerary;
		$location.path('/airlines-user-payment')
	}
});
/*
 * Payment controller module
 */
angular.module("airtlinesEndUserModule").controller("AirlinesEndUserPaymentCtrl",
		function($scope, $rootScope, $location,$http,ReferenceDataFactory,AirlinesDataService){
	$scope.spinner=false
	$scope.showCardDetails = false;
	$scope.showRewardPoints = false;
	$scope.showConfirmation = false;
	$scope.redemPoints=0;
	//Defining a quickLoad
	//TODO: I should comment this portion out 
	$scope.quick_load=function(){
		$scope.cardnumber='44458889010011'
		$scope.cardExpDate='12/2018'
		$scope.cardCVV='555'
		
	}
	
	console.log("Inside AirlinesEndUserPaymentCtrl");
	if($rootScope.selectedItinerary == null){
		alert('No itinerary selected. You will be redirected to search page ');
		$location.path('/airlines-user-flight-book');
	}
	else
	{
		$scope.itinerary = $rootScope.selectedItinerary;
		$scope.quick_load();
	}
	
	$scope.confirm_payment = function(){
		var paymentInfo={};
		//Flatten the obj
		for(var key in $scope.itinerary){
			paymentInfo[key]=$scope.itinerary[key]+"";
		}
		//paymentInfo.itenary = $scope.itinerary;
		paymentInfo.cardnumber = $scope.cardnumber;
		paymentInfo.cardExpDate = $scope.cardExpDate;
		paymentInfo.cardCVV = $scope.cardCVV;
		paymentInfo.redemPoints = $scope.redemPoints+"";
		paymentInfo.ccamt = $scope.itinerary.price-$scope.redemPoints;
		paymentInfo.ffid = $scope.ffid;
		paymentInfo.userId=$rootScope.user.userId;
		AirlinesDataService.makePayment(paymentInfo,function(data){
			console.log("After make payment");
			console.log(data);
			$rootScope.paymentConf=data.payload;
			$location.path("/airline-payment-confirmation");
		},$scope);

		
	}
	$scope.retriveLoyalityPoints=function(){
		if($scope.ffid !=null && $scope.ffid != ''){
		$scope.spinner=true	
		console.log("retriveLoyalityPoints: "+ $scope.ffid);
			$http({
			method  : 'GET',
			url     : 'getRedemableLoyalityPoint.wss',
			params	: {"ffid": $scope.ffid }
		})
		.then(function(response) {
			//Success callback
			console.log("Success getRedemableLoyalityPoint:");
			console.log(response.data);
			$scope.spinner=false	
			if(response.data.status == 0)
			{
				var availablePoints =parseInt(response.data.payload.totalPoint);
				var price = parseInt($scope.itinerary.price)
				if(price<=availablePoints){
						$scope.redemPoints = price
						$scope.maxRedemPoints = price
				} 
				else{
					$scope.redemPoints = availablePoints
					$scope.maxRedemPoints = availablePoints
				}
				
				$scope.showPoints = true;
				
			}
			else
			{
				alert(response.data.statusText);
				
			}
		},function(errorResponse){
			
			alert("Error response:"+ errorResponse);
			$scope.spinner=false	
		});
		}else{
			alert('Please enter your loyalty id ')
		}
	}
});
angular.module("airtlinesEndUserModule").controller("AirlinesEndUserViewBookingsCtrl",
	function($scope, $rootScope, $location,$http,AirlinesDataService){
		console.log("working @AirlinesEndUserViewBookingsCtrl@airtlinesEndUserModule")
		$scope.showResults = false;
		$scope.noRecords = false;
		AirlinesDataService.getBookingsByUser($rootScope.user.userId,function(data){
			console.log("Success of getBookingsByUser()");
			console.log(data);
			var records = data.payload;
			if(records.length>0){
				$scope.bookingRecords = records;
				$scope.showResults = true;
				$scope.noRecords = false;
			}
			else{
				$scope.showResults = false;
				$scope.noRecords = true;
			}

		},$scope);
});

