angular.module("adminModule").controller("AdminViewUsersCtrl",
    function ($scope, $rootScope, $location, $http,AdminDataService) {
        console.log("Initializing AdminViewUsersCtrl@adminModule")
        $scope.viewUsers = false;
        $scope.addPoints = false;
        //$scope.spinner=true;
        $scope.load_users=function(){
            $scope.addPoints = false;
            AdminDataService.getAllUsers($scope.show_users);
        };
        $scope.add_points=function(){
            AdminDataService.addPoints($scope,$scope.show_add_points)
        }
        $scope.show_add_points=function(data){
            alert(data.statusText);
        }
        $scope.load_addPoints=function(){
           $scope.viewUsers = false;
           $scope.addPoints = true;
        };
        $scope.show_users=function(data){
            console.log(data.payload);
            if(data.payload.length>0){
                $scope.users = data.payload;
                 $scope.viewUsers = true;
            }
        }
    });


