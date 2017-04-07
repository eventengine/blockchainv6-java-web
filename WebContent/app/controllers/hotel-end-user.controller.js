angular.module("hotelEndUserModule").controller("HotelEndUserHomeCtrl",
    function($scope, $rootScope, $location,$http){
        console.log("Working @hotelEndUserModule@HotelEndUserHomeCtrl")
        $scope.goto_page=function(link){
            $location.path(link);
        }

}); 

angular.module("hotelEndUserModule").controller("HotelEndUserSearchCtrl",
    function($scope, $rootScope, $location,$http,HotelDataService){
        console.log("Working @HotelEndUserSearchCtrl@HotelEndUserHomeCtrl")
        $scope.showResults= false;
        //Doing a quick load
        $scope.quick_load=function(){
            $scope.checkinDate = "12/04/2017"
            $scope.checkoutDate = "16/04/2017"
        }
        $scope.quick_load();
        $scope.search_rooms = function(){
            console.log("Searching rooms");
            HotelDataService.search_roorms($scope,function(data){
                $scope.rooms = data.payload;
                $scope.showResults= true;
            },$scope);
        }
        $scope.goto_paymenet=function(room){
            room['checkinDate'] = $scope.checkinDate;
            room['checkoutDate'] = $scope.checkoutDate;
            room['guestCount'] = $scope.guestCount;
            
            $rootScope.selectedRoom=room;
            $location.path("/hotel-end-user-payment");
        }
        
}); 
angular.module("hotelEndUserModule").controller("HotelEndUserPaymentCtrl",

    function($scope, $rootScope, $location,$http,HotelDataService){
        console.log("Working HotelEndUserPaymentCtrl@hotelEndUserModule ")
        
        //Defining a quickLoad
        //TODO: I should comment this portion out 
        $scope.quick_load=function(){
            $scope.cardnumber='44458889010011'
            $scope.cardExpDate='12/2018'
            $scope.cardCVV='555'
           
        }
        if($rootScope.selectedRoom==null){
            alert("No room selected");
            $location.path('hotel-end-user-book-hotel')
        }else{
            $scope.roomDetails= $rootScope.selectedRoom;
            $scope.redemPoints = 0;
            $scope.showCardDetails = false;
            $scope.showRewardPoints = false;
            $scope.showConfirmation = false;
            $scope.quick_load();
            console.log( $scope.roomDetails)
        }

        $scope.retrieve_points=function(){
            if($scope.ffid !=null && $scope.ffid != ''){
                HotelDataService.retrieve_points($scope.ffid,$scope.update_points,$scope);
            }
            else{
                alert('Please enter your loyalty id')
            } 
        } 
        $scope.update_points=function(data){
            var availablePoints =parseInt(data.payload.totalPoint);
            var price = parseInt($scope.roomDetails.total)
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
        $scope.confirm_payment=function(){
            var paymentInfo={};
		//Flatten the obj
		for(var key in $scope.roomDetails){
			paymentInfo[key]=$scope.roomDetails[key]+"";
		}
		
		paymentInfo.cardnumber = $scope.cardnumber;
		paymentInfo.cardExpDate = $scope.cardExpDate;
		paymentInfo.cardCVV = $scope.cardCVV;
		paymentInfo.redemPoints = $scope.redemPoints+"";
		paymentInfo.ccamt = $scope.roomDetails.total-$scope.redemPoints;
		paymentInfo.ffid = $scope.ffid;
        paymentInfo.userId = $rootScope.user.userId;
        delete paymentInfo.objType 
		HotelDataService.makePayment(paymentInfo,function(data){
			console.log("After make payment");
			console.log(data);
			$rootScope.paymentConf=data.payload;
			$location.path("/hotel-payment-confirmation");
		},$scope);
        }
});

angular.module('hotelEndUserModule').controller('HotelPaymentConfCtrl',
    function($scope,$rootScope,$location){
       $scope.paymentConf=$rootScope.paymentConf;  
})
angular.module('hotelEndUserModule').controller('HotelEndUserViewBookingsCtrl',
    function($scope,$rootScope,$location,HotelDataService){
       console.log("Working @HotelEndUserViewBookingsCtrl@hotelEndUserModule")
       
       $scope.noRecords = false;
       $scope.showResults = false;
       HotelDataService.view_bookings_user($rootScope.user.userId,
        function(data){
            if(data.payload==null || data.payload.length==0){
                $scope.showResults = false;
                $scope.noRecords = true;
            }else{
                $scope.bookingRecords= data.payload;
                $scope.showResults = true;
                $scope.noRecords = false;
            }
       },$scope);
       
})