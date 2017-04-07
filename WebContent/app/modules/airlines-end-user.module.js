var module = angular.module('airtlinesEndUserModule',['appCommonModule']);
module.config(function($routeProvider,$httpProvider){
	$routeProvider.when('/airlines-user-home', {
		templateUrl:'pages/airlines-end-user-home.html'
		
	}).when('/airlines-user-flight-book',{
		templateUrl: 'pages/airlines-end-user-flight-booking.html'
	}).when('/airlines-user-payment',{
		templateUrl: 'pages/airlines-end-user-payment.html'
	}).when('/airline-payment-confirmation',{
		templateUrl: 'pages/airline-payment-conf.html'
	}).when("/airlines-user-booking",{
		templateUrl: 'pages/airlines-view-user-bookings.html'
	});
	
});