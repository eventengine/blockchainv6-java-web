var module = angular.module('hotelEndUserModule', []);
module.config(
    function($routeProvider,$httpProvider){
	$routeProvider.when('/hotel-end-user-home', {
		
		templateUrl:"pages/hotel-end-user-home.html"
	}).when('/hotel-end-user-book-hotel',{
        templateUrl:"pages/hotel-end-user-hotel-booking.html"
    }).when('/hotel-end-user-payment',{
        templateUrl:"pages/hotel-end-user-hotel-payment.html"
    }).when('/hotel-payment-confirmation',{
        templateUrl:"pages/hotel-payment-conf.html"
    }).when('/hotel-end-user-view-booking',{
        templateUrl:"pages/hotel-end-user-view-booking.html"
    });
});

//TODO: I need to move this to another js file 


