/**
 * 建立angular.module
 */


define(['angular','ng-route','ng-cookies','io-barcode'], function (angular) {

    var app = angular.module('fnApp', ['ui.router','ngCookies','io-barcode'])

    .run(function($rootScope,$location,$window,$state,$http) {

        $rootScope.cssVer = '1.0.30';

    });

    return app;

});
