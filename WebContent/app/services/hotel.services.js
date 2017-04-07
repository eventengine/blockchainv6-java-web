module.service('HotelDataService',
    function($http){
        this.makePayment=function(paymentInfo,callbackMethod,$scope){
            $scope.spinner=true
            $http({
			method  : 'POST',
			url     : 'makePaymentHotel.wss',
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
                console.log("Success getLoyalityPointForHotel:");
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
                alert("Error response for getLoyalityPointForHotel:"+ errorResponse);
            }); //End of $http

        
        }//End of getLoyalityPointForHotel
	    this.search_roorms=function($scope,callbackMethod,$scope){
            $scope.spinner=true
            $http({
                method  : 'GET',
                url     : 'searchRooms.wss',
                params	: {'location':$scope.location,'checkinDate': $scope.checkinDate,
                          'checkoutDate': $scope.checkoutDate, 'guestCount':$scope.guestCount }
            })
            .then(function(response) {
                //Success callback
                $scope.spinner=false
                console.log("Success searchRooms:");
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
                alert("Error response for searchRooms:"+ errorResponse);
            }); //End of $http

        }
        this.view_bookings_user=function(userId,callbackMethod,$scope){
            $scope.spinner=true
            $http({
                method  : 'GET',
                url     : 'viewHotelBookingsUser.wss',
                params	: {'userId':userId}
            })
            .then(function(response) {
                //Success callback
                $scope.spinner=false
                console.log("Success viewHotelBookingsUser:");
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
                alert("Error response for searchRooms:"+ errorResponse);
            }); //End of $http

        }
});
