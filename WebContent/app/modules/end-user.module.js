var module = angular.module('endUserModule',[]);
module.config(function($routeProvider,$httpProvider){
	$routeProvider.when('/end-user-home', {
		templateUrl:'pages/end-user-home.html'
		
	}).when('/end-usr-trxn-list',{
		templateUrl:'pages/end-user-trxn-list.html'
	}).when('/end-usr-view-points',{
		templateUrl:'pages/end-user-view-points.html'
	})
	
});