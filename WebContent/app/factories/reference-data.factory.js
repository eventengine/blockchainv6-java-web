angular.module('appCommonModule').factory('ReferenceDataFactory',function($http){
	var refDataSet = {};
	refDataSet.airportList=new Array();
	//Somehow load this data
	refDataSet.airportList.push({'code':'CCU','display':'KOLKATA'});
	refDataSet.airportList.push({'code':'BOM','display':'MUMBAI'});
	refDataSet.airportList.push({'code':'MAA','display':'CHENNAI'});
	refDataSet.airportList.push({'code':'DEL','display':'DELHI'});
	refDataSet.getAirportList=function(){
		return refDataSet.airportList;
	}
	refDataSet.passengerCount = new Array();
	refDataSet.passengerCount.push({'code':'1','display':'1 Adult'});
	refDataSet.passengerCount.push({'code':'2','display':'2 Adults'});
	refDataSet.passengerCount.push({'code':'3','display':'3 Adults'});
	refDataSet.passengerCount.push({'code':'4','display':'4 Adults'});
	refDataSet.passengerCount.push({'code':'5','display':'5 Adults'});
	refDataSet.passengerCount.push({'code':'6','display':'6 Adults'});
	
	refDataSet.getPassengerCount=function(){
		return refDataSet.passengerCount;
	}
	return refDataSet;
})