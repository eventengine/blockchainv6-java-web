/*
 * Application common utility module
 * 
 */

var module = angular.module('appCommonModule',['ngAnimate', 'ngSanitize', 'ui.bootstrap'])
.directive('mainMenu',function(){
	return {
		templateUrl: 'app/directives/application-menu.html'
	};
})
.directive('mainFooter',function(){
	return {
		templateUrl: 'app/directives/application-footer.html'
	};
}).directive('spinner',function(){
	return {
		scope: true,
		templateUrl: 'app/directives/spinner.html'
	};
}).directive('spaceDebug',function(){
	return {
		templateUrl: 'app/directives/spacer.html'
	};
}).directive('showProgress',function(){
	return {
		scope: true,
		templateUrl: 'app/directives/progress.html'
	};
});


module.controller('MenuCtrl', function($scope, $rootScope, $location,$http,$window,$timeout) {
		$scope.logo="headerx.png";
		$scope.isNavCollapsed = true;
		$scope.isCollapsed = false;
		$scope.isCollapsedHorizontal = false;
		$rootScope.$watch(function() { return $rootScope.user }, function(newVal) { 
				if(newVal!=null)
				{
			    	$rootScope.login=true;
			    	if(newVal.regId=='PARTNER'){
			    		$scope.logo="partner.png";
			    	}
			    	else
			    	{
			    		$scope.logo="header.jpg";
			    	}
			    		
				}
				else
				{
			    	$rootScope.login=false;
			    	$scope.logo="headerx.png";
				}
			}, true);
		
		$scope.status = {
			    isopen: false
			  };

	    $scope.toggled = function(open) {
	   
	   };
	  $scope.toggleDropdown = function($event) {
		    $event.preventDefault();
		    $event.stopPropagation();
		    $scope.status.isopen = !$scope.status.isopen;
	  };
	  $scope.moveTo=function(toURL)
	  {
		  $location.path(toURL);
	  }
});
module.factory('referenceData',function(){
	
	var refDataSet={};
	
	return refDataSet;
});