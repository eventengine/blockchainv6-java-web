var app=angular.module('blockchainApp', ['ngRoute','ngAnimate', 'ngSanitize',
	'appCommonModule', 'loginModule','ui.bootstrap']);

app.config(function($routeProvider,$httpProvider){
	
	$routeProvider.when('/',{
		template :'<div></div>',
		controller : 'ApplicationEntryCtrl' 
	})
	.when("/login",{
		templateUrl: 'pages/login-form.html'
		
	}).otherwise({ redirectTo: '/' })
});
app.controller('ApplicationEntryCtrl',function($scope,$window, $rootScope, $location){
	console.log('applicationEntryController');
	if($rootScope.user ==null)
	{
		$location.path("/login");
	}
	else
	{
		//$location.path("/home");
	}
	console.log('applicationEntryController Changed Router');
	
});
