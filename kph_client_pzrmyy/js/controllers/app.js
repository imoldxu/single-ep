/**
 * 建立angular.module
 */


define(['angular','ng-route','ng-cookies'], function (angular) {

    var app = angular.module('fnApp', ['ui.router','ngCookies'])

    .run(function($rootScope,$location,$window,$state,$http) {

        $rootScope.cssVer = '1.0.25';

    });

    return app;

});
